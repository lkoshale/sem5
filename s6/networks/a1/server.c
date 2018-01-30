#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdbool.h>
#define PORT 8080

void Error(char* at , char* cause ){
  printf("%s : %s\n",at,cause);
  exit(EXIT_SUCCESS);
}

int main(int argc, char const *argv[]) {

  int sockid = socket(AF_INET,SOCK_STREAM,0);

  if(sockid <=0)
    Error("server","error in socket creation");

  // give port and address to server
  struct sockaddr_in address;

  address.sin_family = AF_INET;
  address.sin_port = htons(PORT);
  address.sin_addr.s_addr = INADDR_ANY;

  if( bind(sockid,(struct sockaddr*)&address,sizeof(address))==-1)
    Error("server ","error in binding ");

  if(listen(sockid,3)==-1)
    Error("server ","error in listening");

  int addressLen = sizeof(address);

  while (true) {
    int new_socket = accept(sockid,(struct sockaddr*)&address,(socklen_t*)&addressLen);
    if(new_socket < 0)
      Error("sever","error in accepting connection");

    char buffer[1024]={0};
    recv(new_socket,buffer,1024,0);
    printf("%s\n",buffer );
  }

  return 0;
}
