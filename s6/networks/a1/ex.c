#include<stdio.h>
#include<stdlib.h>
#include<string.h>


int main(){

  char msg[10000] = "its time###";
  char* result = strstr(msg,"###");
  int position = result - msg ;
   char msgBuff[10000];
   strncpy(msgBuff,msg,position);
   msgBuff[position]='\0';
   strcpy(msg,msgBuff);
   strcat(msg," ###\n");
   printf("%s\n",msg );


}
