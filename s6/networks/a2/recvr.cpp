#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdbool.h>
#include <iostream>
#include <string>
#include <vector> 

using namespace std;

// #define PORT 8080
int PORT;

void Error(string cause ){
  string er = "ERROR : "+cause+" : ";
  perror(er.c_str());
  exit(EXIT_SUCCESS);
}


int main(int argc, char const *argv[]) {

  if(argc > 1){
    PORT = atoi(argv[1]);
  }else{
    printf("please provide a PORT no in command line\n");
    exit(0);
  }

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

  while (true) {

      char buffer[1024]={0};

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

      cout<<buffer<<"\n";
  

  }

  return 0;
}
