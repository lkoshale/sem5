#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdbool.h>
#include "cprocessor.h"
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

  //fill user ids
  initializeSeverFile();
  fillUserIdsInArray();




  int sockid = socket(AF_INET,SOCK_STREAM,0);

  if(sockid <=0)
    Error("error in socket creation");

  // give port and address to server
  struct sockaddr_in address;

  address.sin_family = AF_INET;
  address.sin_port = htons(PORT);
  address.sin_addr.s_addr = INADDR_ANY;

  if( bind(sockid,(struct sockaddr*)&address,sizeof(address))==-1)
    Error("error in binding ");

  if(listen(sockid,3)==-1)
    Error("error in listening");

  int addressLen = sizeof(address);

  while (true) {
    int new_socket = accept(sockid,(struct sockaddr*)&address,(socklen_t*)&addressLen);
    if(new_socket < 0)
      Error("error in accepting connection");

    while(true){
      char buffer[1024]={0};
      // recv(new_socket,buffer,1024,0);
      // printf("%s\n",buffer );
      int num;
      if ((num = recv(new_socket, buffer, 1024,0))== -1) {
         perror("recv");
         exit(1);
      }
      else if (num == 0) {
         printf("Connection closed\n");
         //So I can now wait for another client
         break;
      }
      buffer[num] = '\0';
      // printf("Server:Msg Received %s\n", buffer);


      if(strcmp(buffer,"QUIT")==0){
        char acknwldg[10]="-1";
        if ((send(new_socket,acknwldg, strlen(acknwldg),0))== -1)
          Error("Failure Sending Message");
  
        break;
      }
      else if(strcmp(buffer,"LSTU")==0){
        char* acknwldg = listUser();
        if((send(new_socket,acknwldg, strlen(acknwldg),0))== -1)
          Error("Failure Sending Message");

        // printf("acknowledged with %s\n",acknwldg);
      }
      else if(strcmp(buffer,"READM")==0){
         char* acknwldg = ReadMail();
         if(strcmp(acknwldg,"-1")==0){
            // printf("Error current user null\n");
         }

         if((send(new_socket,acknwldg, strlen(acknwldg),0))== -1)
           Error("Failure Sending Message");

         // printf("acknowledged with %s\n",acknwldg);

         acknwldg = NULL;
         free(acknwldg);

      }
      else if(strcmp(buffer,"DELM")==0){
        int rt = deleteMail();
        char acknwldg[100];
        if(rt==-1){
          strcpy(acknwldg,"server error\n");
          // printf("Error currentUser null\n");
        }
        else if(rt==0)
          strcpy(acknwldg,"NO mail to delete");
        else
          strcpy(acknwldg,"Mail deleted sucessfully");

        if((send(new_socket,acknwldg, strlen(acknwldg),0))== -1)
          Error("Failure Sending Message");

        // printf("acknowledged with %s\n",acknwldg);
      }
      else if(strcmp(buffer,"DONEU")==0){
        DoneUser();
        char acknwldg[10]="done";
        if((send(new_socket,acknwldg, strlen(acknwldg),0))== -1)
          Error("Failure Sending Message");

        // printf("acknowledged with %s\n",acknwldg);

      }
      else if(strchr(buffer,' ')!=NULL){
        int index =  (int)(strchr(buffer,' ') - buffer);
        int len = strlen(buffer)-(index+1);
        char first[100];
        char second[100];
        strncpy(first,buffer,index);
        first[index]='\0';
        strncpy(second,buffer+index+1,len);
        second[len]='\0';

        if(strcmp(first,"ADDU")==0){
          char acknwldg[1024];
          if(createUser(second))
              strcpy(acknwldg,"User added succesfully");
          else
              strcpy(acknwldg,"Userid already present");

          if((send(new_socket,acknwldg, strlen(acknwldg),0))== -1)
            Error("Failure Sending Message");

        }
        else if(strcmp(first,"USER")==0){
          char acknwldg[1024];
          int count = setUser(second);
          if(count ==-1)
            strcpy(acknwldg,"-1"); //user not exists
          else
            sprintf(acknwldg,"User exits and have %d mail",count);

          if((send(new_socket,acknwldg, strlen(acknwldg),0))== -1)
              Error("Failure Sending Message");
        }
        else if(strcmp(first,"SEND")==0){
            char acknwldg[1024];
          if(strchr(second,' ')!=NULL){
            int index1 =  (int)(strchr(second,' ') - second);
            int len1 = strlen(second)-(index1+1);
            char first1[100];
            char second1[MAIL_SIZE];
            strncpy(first1,second,index1);
            first1[index1]='\0';
            strncpy(second1,second+index1+1,len1);
            second1[len1]='\0';

            int rt = sendMail(first1,second1);
            if(rt==-1)
              strcpy(acknwldg,"User doesn't Exists!!");
            else if(rt==0)
              strcpy(acknwldg,"No terminating ### in msg , FAILED !!");
            else
              strcpy(acknwldg,"Message sent");

          }
          else{
            strcpy(acknwldg,"Server error");
          }

          if((send(new_socket,acknwldg, strlen(acknwldg),0))== -1)
              Error("Failure Sending Message");
        }
        else{

          char acknwldg[] = "Invalid Comand";
          if((send(new_socket,acknwldg, strlen(acknwldg),0))== -1)
            Error("Failure Sending Message");

          // printf("acknowledged with %s\n",acknwldg);

        }

      }
      else {

        char acknwldg[]="Invalid Comand";
        if((send(new_socket,acknwldg, strlen(acknwldg),0))== -1)
          Error("Failure Sending Message");

        // printf("acknowledged with %s\n",acknwldg);
      }

      //printf("Server:Msg being sent: %s\nNumber of bytes sent: %d\n",buffer, strlen(buffer));

    }


  }

  return 0;
}
