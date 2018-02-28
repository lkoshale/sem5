
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string>
#include <iostream>
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


  int sockfd = socket(PF_INET,SOCK_DGRAM,IPPROTO_UDP);
  struct sockaddr_in address;
  address.sin_family = AF_INET;
  address.sin_port = htons(PORT);
  address.sin_addr.s_addr = inet_addr("127.0.0.1");
  memset(address.sin_zero, '\0', sizeof address.sin_zero); 


  if(sockfd<0)
    Error("socket creation error!! ");

  char msg[1024];
  while(cin>>msg){

    if(sendto(sockfd,msg,strlen(msg),0,(struct sockaddr *)& address,sizeof(address)) != strlen(msg))
        Error("send error different number of bbytes sent");

  }

  return 0;
}
