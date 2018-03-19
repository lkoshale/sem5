  
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <pthread.h>
#include <math.h>
#include <time.h>
#include <string>
#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <iomanip>


using namespace std;


////////////////////
bool DEBUG ;
string IPADDRS;
int PORT,MAX_PACKET_LEN,PACKET_GEN_RATE,MAX_PACKETS,WINDOW_SIZE,BUFFER_SIZE;

//////////////////
double RTT_SUM;
int PACK_SENT;
///////////////////
int sockfd ;
struct sockaddr_in address;

///////////////////////

queue < unsigned char* >BUFFER;   //for packets
queue < struct timespec > genTime;   //for storing time when they generated
bool endF  = false;
pthread_mutex_t lock;

//////////////////
// vector<int>SenderWindowACK;
// pthread_mutex_t ack_lock;

queue<unsigned char* > SENDER_WINDOW;
pthread_mutex_t window_lock; 

queue < struct timespec > StartTime;
pthread_mutex_t time_lock;

queue<int>attemptNo;
pthread_mutex_t at_lock;     //TODO require a single num maybe
/////////////////////////
int trasmtNo;

void Error(string cause ){
  string er = "ERROR : "+cause+" : ";
  perror(er.c_str());
  exit(EXIT_SUCCESS);
}

float get_RTT_AVE(){
  return RTT_SUM/PACK_SENT ;
}


void setInput( const char* arg, const char* val ){
  if(strcmp(arg,"-d")==0)
    DEBUG = true;
  else if(strcmp(arg,"-s")==0)
    IPADDRS = val;
  else if(strcmp(arg,"-p")==0)
    PORT = atoi(val);
  else if(strcmp(arg,"-l")==0)
    MAX_PACKET_LEN = atoi(val);
  else if(strcmp(arg,"-r")==0)
    PACKET_GEN_RATE = atoi(val);
  else if(strcmp(arg,"-n")==0)
    MAX_PACKETS = atoi(val);
  else if(strcmp(arg,"-w")==0)
    WINDOW_SIZE = atoi(val);
  else if(strcmp(arg,"-b")==0)
    BUFFER_SIZE = atoi(val);
  else
    Error("Invalid command line argument ");

}


//geneartes packets based on rate in different thread
void* genratePacket(void* args){
   int count = 0;

   if(PACKET_GEN_RATE > 1000000){
    cout<<"Too HIGh PACKAGE GEN RATE \n";
    exit(0);
   }

   float onepacktime = (1.0/PACKET_GEN_RATE)*1000000;
   unsigned int microsec = (unsigned int)ceil(onepacktime);
   while(!endF){
    unsigned char* packet = (unsigned char*) malloc(sizeof(unsigned)*MAX_PACKET_LEN);
    if(BUFFER.size() < BUFFER_SIZE ){
      packet[0] = count % WINDOW_SIZE;
      struct timespec start;

      clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &start);
      pthread_mutex_lock(&lock);
        BUFFER.push(packet);
        genTime.push(start);
      pthread_mutex_unlock(&lock);
      count++;
      //sleep
      usleep(microsec);
    }
    // cout<<"packet No "<<(int)count<<"\n";
   }

}


void* recive_ACK_fn(void* args){

  socklen_t addressLen = sizeof(address);

  while(!endF){
      char recvBuff[1024] = {0};
      int num;
      struct timespec stop;
      if ((num = recvfrom(sockfd,recvBuff,1024,0,(struct sockaddr*)&address,(socklen_t*)&addressLen))== -1) {
         perror("recvfrom");
         exit(1);
      }

      //packet sent properly
      int seqNo = atoi(recvBuff);
      if(seqNo >= WINDOW_SIZE)
        Error("Sequence no out of range in ack");

      clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &stop);
      
      pthread_mutex_lock(&time_lock);
      struct timespec start = StartTime.front();
      pthread_mutex_unlock(&time_lock);

      double result = (stop.tv_sec - start.tv_sec) * 1e6 + (stop.tv_nsec - start.tv_nsec) / 1e3;    // in microseconds

      RTT_SUM+=result;

      pthread_mutex_lock(&time_lock);
      StartTime.pop();
      int at = attemptNo.front();
      attemptNo.pop();
      pthread_mutex_unlock(&time_lock);
    
      pthread_mutex_lock(&window_lock);
        SENDER_WINDOW.pop();                //ack are never lost  // commulative ack
        PACK_SENT++;
      pthread_mutex_unlock(&window_lock);

      double sTime = start.tv_sec*1e6 + start.tv_nsec/1e3 ;  //in micro sec
      char tstr[10];
      sprintf(tstr,"%d",(int)sTime % 1000);
      tstr[2]='\0';

      if(DEBUG)
        cout<<fixed<<setprecision(2)<<"Seq# "<<seqNo<<" Time Generated : "<< (int)(sTime/1000) << ":"<< tstr <<"  RTT : "<<result/1000<<"  Num of attempts: "<<at<<"\n";


  }

}

void resend_packets(){
  //wait then resend the top packet of window

  struct timespec stop,start;
  int curAT;
  pthread_mutex_lock(&time_lock);
    start = StartTime.front();
    curAT = attemptNo.front();
  pthread_mutex_unlock(&time_lock);

  if(curAT > 10 )
    Error(to_string((int)(SENDER_WINDOW.front()[0]))+" maximum attempt for re transmitting packet exceeded");

  pthread_mutex_lock(&window_lock);
    int SENDER_WINDOW_SIZE = SENDER_WINDOW.size();
    int p_sent = PACK_SENT;
  pthread_mutex_unlock(&window_lock);


  //wait for packet
  while(true){
    
    if(SENDER_WINDOW_SIZE < WINDOW_SIZE)
      return;

    clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &stop);
    double msec = (stop.tv_sec - start.tv_sec) * 1e6 + (stop.tv_nsec - start.tv_nsec) / 1e3;    // in microseconds


    int time_limit ;
    if(p_sent <= 10)
      time_limit = 100000; //100 milli sec
    else
      time_limit = 2*get_RTT_AVE();

    if(msec > time_limit )
      break;

    pthread_mutex_lock(&window_lock);
      SENDER_WINDOW_SIZE = SENDER_WINDOW.size();
    pthread_mutex_unlock(&window_lock);

  }

  queue< struct timespec> tempQ(StartTime);
  queue< int> tempAT(attemptNo);

  pthread_mutex_lock(&time_lock);
    while(!StartTime.empty()){
      StartTime.pop();
      attemptNo.pop();
    }
  pthread_mutex_unlock(&time_lock);

  //last check
  pthread_mutex_lock(&window_lock);
    SENDER_WINDOW_SIZE = SENDER_WINDOW.size();
  pthread_mutex_unlock(&window_lock);
  if(SENDER_WINDOW_SIZE < WINDOW_SIZE){
    tempQ.pop();
    tempAT.pop();
    pthread_mutex_lock(&time_lock);
    while(!tempQ.empty()){
      StartTime.push(tempQ.front());
      tempQ.pop();
      attemptNo.push(tempAT.front());
      tempAT.pop();
    }
    pthread_mutex_unlock(&time_lock);
    return;
  }
  

  //resend
  vector<unsigned char*> vec;
   pthread_mutex_lock(&window_lock);
   for(int i=0;i<SENDER_WINDOW.size();i++){
    unsigned char* packet = SENDER_WINDOW.front();
      SENDER_WINDOW.pop();
      SENDER_WINDOW.push(packet);
      vec.push_back(packet);
   }
   pthread_mutex_unlock(&window_lock);


  
  for(int i=0;i<vec.size();i++){
    unsigned char* packet = vec[i];
    
    struct timespec start2;

    clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &start2);
    if(sendto(sockfd,packet,MAX_PACKET_LEN,0,(struct sockaddr *)& address,sizeof(address)) != MAX_PACKET_LEN)
     Error("send error");

    trasmtNo++;

    pthread_mutex_lock(&time_lock);
    StartTime.push(start2);
    attemptNo.push(tempAT.front()+1);
    tempAT.pop();
    pthread_mutex_unlock(&time_lock);

  }


}


int main(int argc, char const *argv[]) {

 
  DEBUG = false;
  IPADDRS = "127.0.0.1";
  PORT = 8080;              //default port
  MAX_PACKET_LEN = 256 ;
  PACKET_GEN_RATE = 20;
  MAX_PACKETS = 20;
  WINDOW_SIZE = 4 ;      //Max WS = 2^(SNF-1)
  BUFFER_SIZE = 1000;


  //debug always the first arg -d
  if(argc%2==0 && argc > 1){
    setInput(argv[1],NULL);
    for(int i=2;i<argc;i+=2){
      setInput(argv[i],argv[i+1]);
    }
  }
  else if(argc>1){
    for(int i=1;i<argc;i+=2){
      setInput(argv[i],argv[i+1]);
    }
  }

  //initailize global var
  RTT_SUM = 0 ; 
  PACK_SENT = 0;
  trasmtNo = 0;

  sockfd = socket(PF_INET,SOCK_DGRAM,IPPROTO_UDP);
  address.sin_family = AF_INET;
  address.sin_port = htons(PORT);
  address.sin_addr.s_addr = inet_addr(IPADDRS.c_str());
  memset(address.sin_zero, '\0', sizeof address.sin_zero); 

  if(sockfd<0)
    Error("socket creation error!! ");

  pthread_t tid,recvThread;
  void* arg = NULL;
  int err = pthread_create(&tid,NULL,genratePacket,arg);
  if(err!=0)
    Error("cant create thred for packet gen");


  int rcvErr = pthread_create(&recvThread,NULL,recive_ACK_fn,arg);
  if(rcvErr!=0)
    Error("cant create thred to recv ack");

  int p_sent = 0;

  while(p_sent < MAX_PACKETS ){
    pthread_mutex_lock(&window_lock);
    int SENDER_WINDOW_SIZE = SENDER_WINDOW.size();
    p_sent = PACK_SENT;
    pthread_mutex_unlock(&window_lock);

    while( SENDER_WINDOW_SIZE < WINDOW_SIZE  && p_sent < MAX_PACKETS ){
      
      //get next packet in queue
      bool con = false;
      unsigned char* packet = NULL;
      struct timespec start;
      pthread_mutex_lock(&lock);
      if(!BUFFER.empty()){
        packet =  BUFFER.front();
        BUFFER.pop();
        start = genTime.front();
        genTime.pop();
      }
      else{
        con = true;
      }
      pthread_mutex_unlock(&lock);
      
      if(con)
        continue;

      //store packet and its time
      int seqNo = (int)packet[0];

      pthread_mutex_lock(&time_lock);
      StartTime.push(start);
      attemptNo.push(1);
      pthread_mutex_unlock(&time_lock);
      // clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &StartTime[seqNo]);
      if(p_sent == 0)
        packet[3] = (unsigned char)WINDOW_SIZE;


      if(sendto(sockfd,packet,MAX_PACKET_LEN,0,(struct sockaddr *)& address,sizeof(address)) != MAX_PACKET_LEN)
        Error("send error");

      // UNACK_PACK_COUNT++;
      trasmtNo++;

      pthread_mutex_lock(&window_lock);
      SENDER_WINDOW.push(packet);
      SENDER_WINDOW_SIZE = SENDER_WINDOW.size();
      p_sent = PACK_SENT;
      pthread_mutex_unlock(&window_lock);

    }



    if(SENDER_WINDOW_SIZE == WINDOW_SIZE){
      // wait reset clock send pack then continue till window
        resend_packets();
        pthread_mutex_lock(&window_lock);
        SENDER_WINDOW_SIZE = SENDER_WINDOW.size();
        p_sent = PACK_SENT;
        pthread_mutex_unlock(&window_lock);
    }

  }

  endF=true;
  usleep(1000);  //sleep for 1millisec
  cout<<"\n";
  cout<<"Output :  PktRate = " <<PACKET_GEN_RATE << " Length = "<< MAX_PACKET_LEN  << "  ";
  cout<< fixed<<setprecision(5)<<"Retran Ratio = "  << (float)trasmtNo/PACK_SENT << "  Avg RTT = "<<get_RTT_AVE()/1000 << "\n";
  pthread_join(tid,NULL);
  exit(EXIT_SUCCESS);
  return 0;

}
