
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

// #define PORT 8080

int PORT;

void Error(char* cause ){
  char er[strlen(cause)+50];
  sprintf(er,"ERROR : %s :",cause);
  perror(er);
  exit(EXIT_SUCCESS);
}


int main(int argc, char const *argv[]) {

  if(argc > 1){
    
    PORT = atoi(argv[1]);
  }else{
    printf("please provide a PORT no in command line\n");
    exit(0);
  }


  int sockfd = socket(PF_INET,SOCK_STREAM,0);
  struct sockaddr_in address;
  address.sin_family = AF_INET;+
  address.sin_port = htons(PORT);
  char ipServer[] = "127.0.0.1";

//  memset(&address, '0', sizeof(address));

  if(sockfd<=0)
    Error("socket creation error!! ");

  if(inet_pton(AF_INET,ipServer,&address.sin_addr) < 0 )
    Error("error in converting server ip ");

  if( connect( sockfd,(struct sockaddr*) &address,sizeof(address)) < 0)
    Error("error in connection");

  // char msg[] = " Hi from client !! ";
  // send(sockfd,msg,strlen(msg),0);
  // printf("client : msg send \n");
  char* userSet = NULL;
  while(true){
    if(userSet==NULL)
      printf("Main-Prompt>");
    else
      printf("Sub-Prompt-%s>",userSet);

    char* cmd = (char*)malloc(sizeof(char)*1000);
    if(fgets(cmd,10000,stdin)==NULL)
      Error("input msg error ");

    cmd[strlen(cmd)-1]='\0';
    char msg[1000];

    if(strcmp(cmd,"Listusers")==0){
      strcpy(msg,"LSTU");
    }
    else if(strcmp(cmd,"Read")==0){
      strcpy(msg,"READM");
      if(userSet==NULL){
        printf("No user selected try SetUser <userid>\n");
        continue;
      }
    }
    else if(strcmp(cmd,"Delete")==0){
      strcpy(msg,"DELM");
      if(userSet==NULL){
        printf("No user selected try SetUser <userid>\n");
        continue;
      }
    }
    else if(strcmp(cmd,"Done")==0){
      strcpy(msg,"DONEU");
      userSet = NULL;
    }
    else if(strcmp(cmd,"Quit")==0){
      strcpy(msg,"QUIT");

    }
    else if(strchr(cmd,' ')!=NULL){
      int index =  (int)(strchr(cmd,' ') - cmd);
      int len = strlen(cmd)-(index+1);
      char first[100];
      char second[100];
      strncpy(first,cmd,index);
      first[index]='\0';
      strncpy(second,cmd+index+1,len);
      second[len]='\0';

      if(strcmp(first,"Adduser")==0){
        sprintf(msg,"ADDU %s",second);
      }
      else if(strcmp(first,"Send")==0){
        char Mail[10000];
        printf("Type Message:");
        fgets(Mail,10000,stdin);
        Mail[strlen(Mail)-1]='\0';
        char Send[11000];
        sprintf(Send,"SEND %s %s",second,Mail);

        if(send(sockfd,Send,strlen(Send),0) == -1)
          Error("msg sending error");

        // printf("client : msg send %s\n",msg);
        char* ack = (char*)malloc(sizeof(char)*1024);
        int s = recv(sockfd,ack,1024,0);
        if(s==0)
          Error("acknowledge error ");

        ack[s] = '\0';

        printf("%s\n",ack);

        continue;
      }
      else if(strcmp(first,"SetUser")==0){
          sprintf(msg,"USER %s",second);
          if(send(sockfd,msg,strlen(msg),0) == -1)
            Error("msg sending error");

          char* ack = (char*)malloc(sizeof(char)*1024);
          int s = recv(sockfd,ack,1024,0);
          if(s==0)
            Error("acknowledge error ");

          ack[s] = '\0';
          if(strcmp(ack,"-1")==0)
            printf("User does not Exists !!\n");
          else {
            printf("%s\n",ack);
            userSet = (char*)malloc(sizeof(char)*100);
            strcpy(userSet,second);
          }

          continue;

      }else{
        printf("Not a Valid command try again\n");
        continue;
      }

    }
    else{
      printf("Not a Valid command try again\n");
      continue;
    }

    if(send(sockfd,msg,strlen(msg),0) == -1)
      Error("msg sending error");

    // printf("client : msg send %s\n",msg);
    char* ack = (char*)malloc(sizeof(char)*1024);
    int s = recv(sockfd,ack,1024,0);
    if(s==0)
      Error("acknowledge error ");

    ack[s] = '\0';
    //exit command
    if(strcmp(msg,"QUIT")==0 && strcmp(ack,"-1")==0)
        exit(1);

    printf("%s\n",ack);
    free(cmd);
    free(ack);
  }

  return 0;
}
