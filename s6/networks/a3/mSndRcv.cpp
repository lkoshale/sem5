
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
#include <iostream>
#include <string>
#include <vector>
#include <algorithm>

using namespace std;

int PID;
int n = 3;

// udp data
int PORT = 10000;
int sockid;
struct sockaddr_in address;



//create n-1 to 1 process
void create_proc(int n){
    if(n>=1){
        if(fork()==0)
            PID = n;
        else
            create_proc(n-1);
    }
    return;
}

void Error(string cause ){
  string er = "ERROR : "+cause+" : ";
  perror(er.c_str());
  exit(EXIT_SUCCESS);
}

void* send_hello(void* args);
void* recv_hello(void* args);

int main(){

    PID = 0;  //0th proc
    create_proc(n-1);

    printf("Hello from %d \n",PID);
    //create socket
    sockid = socket(AF_INET,SOCK_DGRAM,IPPROTO_UDP);

    if(sockid < 0)
        Error("error in socket creation");

    // give port and address to server
    // act as reciever
    address.sin_family = AF_INET;
    address.sin_port = htons(PORT+PID);
    address.sin_addr.s_addr = htonl(INADDR_ANY);

    if( bind(sockid,(struct sockaddr*)&address,sizeof(address))==-1)
        Error("error in binding ");
    
    pthread_t send_h ,recv_h;
    void* arg = NULL;
    int rcv = pthread_create(&recv_h,NULL,recv_hello,arg);
    if(rcv!=0)
        Error("cant create thred for recv");
    
    int snd = pthread_create(&send_h,NULL,send_hello,arg);
    if(snd!=0)
        Error("cant create thread for send");

    pthread_join(recv_h,NULL);
    pthread_join(send_h,NULL);
    return 0;
}


void* recv_hello(void* args){

    socklen_t addressLen = sizeof(address);
    char buffer[1024];
    while(true){
        int num;
        if ((num = recvfrom(sockid,buffer,1024,0,(struct sockaddr*)&address,(socklen_t*)&addressLen))== -1) {
            Error("recv_hello");
        }

        printf("%s in %d\n",buffer,PID);
    }
}

//run in different thread sends message
void* send_hello(void* args){
   while(true){
    for(int i=0;i<n;i++){
        int sockfd = socket(PF_INET,SOCK_DGRAM,IPPROTO_UDP);
        struct sockaddr_in address;
        address.sin_family = AF_INET;
        address.sin_port = htons(PORT+i);
        address.sin_addr.s_addr = inet_addr("127.0.0.1");
        memset(address.sin_zero, '\0', sizeof address.sin_zero); 

        if(sockfd<0)
            Error("socket creation error!! ");

        char msg[1024];
        sprintf(msg,"hello from %d",PID); 
        if(sendto(sockfd,msg,strlen(msg),0,(struct sockaddr *)& address,sizeof(address)) != strlen(msg))
            Error("send error different number of bbytes sent");

        close(sockfd);
    }
    usleep(400000);
   
   }
}