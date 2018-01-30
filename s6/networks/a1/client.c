
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define PORT 8080

void Error(char* at , char* cause ){
  printf("%s : %s\n",at,cause);
  exit(EXIT_SUCCESS);
}


int main(int argc, char const *argv[]) {

  int sockfd = socket(PF_INET,SOCK_STREAM,0);
  struct sockaddr_in address;
  address.sin_family = AF_INET;
  address.sin_port = htons(PORT);
  char ipServer[] = "127.0.0.1";

//  memset(&address, '0', sizeof(address));

  if(sockfd<=0)
    Error("client ","socket creation error!! ");

  if(inet_pton(AF_INET,ipServer,&address.sin_addr) < 0 )
    Error("client ","error in converting server ip ");

  if( connect( sockfd,(struct sockaddr*) &address,sizeof(address)) < 0)
    Error("client","error in connection");

  char msg[] = " Hi from client !! ";
  send(sockfd,msg,strlen(msg),0);
  printf("client : msg send \n");

  return 0;

}
