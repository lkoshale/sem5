#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdbool.h>
#include <unistd.h>
#include <time.h>
#include <iostream>
#include <string>
#include <vector> 

using namespace std;

bool DEBUG;
double PACKET_ERROR_RATE;  //atof(const chr*)
int PORT,MAX_PACKETS,WINDOW_SIZE;

void Error(string cause ){
  string er = "ERROR : "+cause+" : ";
  perror(er.c_str());
  exit(EXIT_SUCCESS);
}

void setInput( const char* arg, const char* val ){
  if(strcmp(arg,"-d")==0)
    DEBUG = true;
  else if(strcmp(arg,"-p")==0)
    PORT = atoi(val);
  else if(strcmp(arg,"-n")==0)
    MAX_PACKETS = atoi(val);
  else if(strcmp(arg,"-e")==0)
    PACKET_ERROR_RATE = atof(val);
  else
    Error("Invalid command line argument ");

}

int main(int argc, char const *argv[]) {


  DEBUG = false;
  PORT = 8080;
  MAX_PACKETS = 20;
  PACKET_ERROR_RATE = 0.30;
  WINDOW_SIZE = 4;

  if(argc%2==0 && argc > 1){
    DEBUG = true;
    for(int i=2;i<argc;i+=2){
      setInput(argv[i],argv[i+1]);
    }
  }
  else if(argc > 1){
    for(int i=1;i<argc;i+=2){
      setInput(argv[i],argv[i+1]);
    }
  }

  // cout<<PACKET_ERROR_RATE<<"\n";


  int PACKET_COUNT = 0; 

  int sockid = socket(AF_INET,SOCK_DGRAM,IPPROTO_UDP);

  if(sockid < 0)
    Error("error in socket creation");

  // give port and address to server
  struct sockaddr_in address;

  address.sin_family = AF_INET;
  address.sin_port = htons(PORT);
  address.sin_addr.s_addr = htonl(INADDR_ANY);

  if( bind(sockid,(struct sockaddr*)&address,sizeof(address))==-1)
    Error("error in binding ");

  socklen_t addressLen = sizeof(address);

  int EXPECTING_SEQ = 0;
  bool first = true;
  while (PACKET_COUNT < MAX_PACKETS) {

    char buffer[1024]={0};
    struct timespec start;

    int num;
    if ((num = recvfrom(sockid,buffer,1024,0,(struct sockaddr*)&address,(socklen_t*)&addressLen))== -1) {
     perror("recvfrom");
     exit(1);
    }
    else if (num == 0) {
     printf("Connection closed\n");
     // wait for another client
     break;
    }

    clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &start);

    if(first){
      WINDOW_SIZE = (int)buffer[3];
      first = false;
    }
  
    int v = rand()%10000000;
    int seqNo = (int)buffer[0];
    string drop ;
    if( (PACKET_COUNT % WINDOW_SIZE) == seqNo){
    
      if( v < PACKET_ERROR_RATE*10000000 ){
        //Error drop the packet no ack
        drop = "true ";
      }
      else{
        char sendmsg[15];
        sprintf(sendmsg,"%d", (int)buffer[0] );
        if(sendto(sockid,sendmsg,strlen(sendmsg),0,(struct sockaddr *)& address,sizeof(address)) != strlen(sendmsg))
          Error("send error");

       PACKET_COUNT++;
       // cout<<(int)buffer[0]<<"\n";
       drop = "false";
      }
    
    }else{
        drop = "true";
      // cout<<" dropped -- expecting "<<(PACKET_COUNT % WINDOW_SIZE)<<"  got "<<(int)buffer[0]<<"\n";
      
    }

    double sTime = start.tv_sec*1e6 + start.tv_nsec/1e3 ;  //in micro sec
    char tstr[10];
    sprintf(tstr,"%d",(int)sTime % 1000);
    tstr[2]='\0';

    if(DEBUG)
      cout<<"Seq# "<<(int)buffer[0]<<" Time Received: "<<(int)(sTime/1000) << ":"<< tstr <<"  Packet Dropped "<<drop<<"\n";
  
  }

  return 0;
}

