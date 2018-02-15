#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<stdbool.h>
#include <time.h>

#define USER_SIZE 1000

// FILE* array[USER_SIZE];

#define UserNameLen 1024
#define NumUsers 1024
#define MAIL_SIZE 10000

char userIDFileName[1024];

char* UserIdArray[NumUsers];
int Ulen=0;

// spool file pointer for current user
FILE* currentUser = NULL;
int currentUserMailNo = 0;
char* currentUserID = NULL;
int CurrentUserTotalMail =0;
char rootDir[100];

FILE* file;

void initializeSeverFile(){
  strcpy(userIDFileName,"MAILSERVER/UserIDs.txt");
  strcpy(rootDir,"MAILSERVER/");
  Ulen=0;
  currentUser = NULL;
  currentUserMailNo = 0;
  currentUserID = NULL;
  CurrentUserTotalMail =0;
}

//fiils the array from persitent userids stored aaray
void fillUserIdsInArray(){
  FILE* fp;
  fp = fopen(userIDFileName,"a+");
  UserIdArray[Ulen]= (char*)malloc(sizeof(char)*UserNameLen);

  while (fscanf(fp,"%s",UserIdArray[Ulen]) != EOF) {
      //printf("%s\n",UserIdArray[Ulen] );
      Ulen++;
      UserIdArray[Ulen]= (char*)malloc(sizeof(char)*UserNameLen);
  }
  fclose(fp);
}


char* listUser( ){
  char* str = (char*)malloc( sizeof(char)*NumUsers*UserNameLen);
  int i;
  for( i=0;i<Ulen;i++){
    strcat(str,UserIdArray[i]);
    strcat(str,"  ");
  }
  return str;
}


bool UserExists(char* userid){
  int i;
  for(i=0;i<Ulen;i++){
    if(strcmp(UserIdArray[i],userid)==0)
      return true;
  }

  return false;
}

bool createUser(char* userid){
  if( UserExists(userid)){
    //not sucesss already exists
    return false;
  }else{
    FILE* fp = fopen(userIDFileName,"a+");
    char* userIDstored = (char*)malloc(sizeof(char)*100);
    strcpy(userIDstored,userid);
    fprintf(fp,"%s\n",userid);
    UserIdArray[Ulen] = userIDstored;
    Ulen++;
    fclose(fp);
    return true;
  }
}


int setUser(char* userId){
  if(!UserExists(userId)){
    return -1;
    currentUserID = NULL;
  }else{
    char Location[1000];
    strcpy(Location,rootDir);
    strcat(Location,userId);
    currentUser = fopen(Location,"a+");
    char buffer[MAIL_SIZE];
    int count = 0;
    while(fscanf(currentUser,"%s",buffer)!=EOF){
      if(strcmp(buffer,"###")==0)
        count++;
    }
    rewind(currentUser);
    currentUserID = (char*)malloc(sizeof(char)*(strlen(userId)+5));
    strcpy(currentUserID,userId);
    CurrentUserTotalMail=count;
    return count;
  }

}

void sendReadMailResponse(char* mail){
  printf("%s\n",mail );
}

char* ReadMail(){
  char* str=(char*)malloc(sizeof(char)*MAIL_SIZE);
  strcpy(str," ");
  if(currentUser==NULL){
    strcpy(str,"-1");
    return str;
  }else{
      int k = 0;
      char buffer[MAIL_SIZE];
      if(CurrentUserTotalMail == 0){
        strcpy(str,"Invalid NO MAILS to read !!");
        return str;
      }
      else if(CurrentUserTotalMail==currentUserMailNo){
        rewind(currentUser);
        currentUserMailNo=0;
      }

      // while(fscanf(currentUser,"%s",buffer)!=EOF){

      //   if(strcmp(buffer,"###")==0)
      //     break;
      //   else{
      //     if(strcmp(buffer,"Subject:")==0){
      //         k=1;
      //         continue;
      //     }
      //     if(k==1){
      //         strcat(str,buffer);
      //         strcat(str," ");
      //     }
      //   }
      // }

      char ch;
      int hash =0;
      int len = strlen(str);
      while((ch=getc(currentUser))!=EOF){
        if(ch=='#')
          hash++;
        else
          hash=0;

        str[len]= ch;
        len++;

        if(hash==3)
          break;

      }
      str[len-3]='\0';

  }
  currentUserMailNo++;
  return str ;
}


int deleteMail(){

  if(currentUser==NULL){
    return -1;
  }
  else if(CurrentUserTotalMail==0){
    return 0;
  }
  else{

    if(CurrentUserTotalMail==currentUserMailNo){
        rewind(currentUser);
        currentUserMailNo=0;
      }

    FILE* tempFile = fopen("MAILSERVER/temp","w");

    rewind(currentUser);
    char ch ;
    int mNo = 0;
    int mtrack = 0;




    if(currentUserMailNo==0){
      char buff[MAIL_SIZE];
      bool b = false;
      if(fscanf(currentUser,"%s",buff)==EOF && CurrentUserTotalMail>0)
        rewind(currentUser);

      while(fscanf(currentUser,"%s",buff)!=EOF){
        if(strcmp(buff,"###")==0){
          mNo++;
          b = true;
          break;
        }

      }

      char ch;
      while((ch = getc(currentUser))!=EOF){
        putc(ch,tempFile);
      }

    }
    else{
      while((ch = getc(currentUser))!=EOF){
        if(ch =='#')
          mtrack++;
        else
          mtrack=0;

        if(mtrack==3)
          mNo++;

        putc(ch,tempFile);

        if(mNo == currentUserMailNo){
          char buff[MAIL_SIZE];
          while(fscanf(currentUser,"%s",buff)!=EOF){
            if(strcmp(buff,"###")==0){
              mNo++;
              break;
            }
          }

        }
      }

    }

    fclose(tempFile);
    fclose(currentUser);

    char Location[100];
    strcpy(Location,rootDir);
    strcat(Location,currentUserID);
    remove(Location);
    rename("MAILSERVER/temp",Location);

    currentUser = fopen(currentUserID,"a+");

    rewind(currentUser);
    int lineNo = 0;
    char buff[MAIL_SIZE];
    if(currentUserMailNo!=0){
      while(fscanf(currentUser,"%s",buff)!=EOF){

        if(strcmp(buff,"###")==0)
          lineNo++;

        if(currentUserMailNo==lineNo)
          break;
      }
    }

  }
  CurrentUserTotalMail--;
  return 1;
}


int sendMail(char* toUser,char* msg){

  char dateAndTime[1024];
  time_t t = time(NULL);
  struct tm tm = *localtime(&t);

  sprintf(dateAndTime," %d-%d-%d %d:%d:%d", tm.tm_year + 1900, tm.tm_mon + 1, tm.tm_mday, tm.tm_hour, tm.tm_min, tm.tm_sec);

  if(!UserExists(toUser)){
      printf(" Error the user trying to send doesnt eist !!" );
      return -1;
  }

  if(strstr(msg,"###")==NULL){
      return 0;
  }
  else{
    char* result = strstr(msg,"###");
    int position = result - msg ;
    char msgBuff[MAIL_SIZE];
    strncpy(msgBuff,msg,position);
    msgBuff[position]='\0';
    strcpy(msg,msgBuff);
    strcat(msg," ###\n ");
  }

  if(currentUserID!=NULL&&currentUser!=NULL){
    char Location[500];
    strcpy(Location,rootDir);
    strcat(Location,toUser);
    FILE* toSend = fopen(Location,"a+");
    fprintf(toSend, "From: %s\nTo: %s\nDate: %s\nMail: %s\n",currentUserID,toUser,dateAndTime,msg );
    fclose(toSend);
  }

  return 1;
}

void DoneUser(){
  fclose(currentUser);
  currentUser = NULL;
  currentUserID = NULL;
  currentUserMailNo = 0;
  CurrentUserTotalMail = 0;
}

//
// int main(int argc, char const *argv[]) {
//
//   file = fopen("u1.txt","a+");
//   // file = fopen("user1","a+");
//   // fprintf(file, "writing to file\n" );
//   char buff[10000];
//
//   fillUserIdsInArray();
//   createUser("user2");
//   printf("%s\n", listUser() );
//   printf("%d\n", setUser("user1") );
//   // printf("%d\n", ReadMail() );
//   // printf("%d\n", deleteMail());
//   // printf("%d\n", ReadMail());
//   //
//   sendMail("user2","first msg\n.....\ncontents\n..... \n###\n");
//   DoneUser();
//   printf("%d\n",setUser("user2"));
//   printf("%d\n", ReadMail());
//
//
//
//   // while(fscanf(file,"%s",buff)!=EOF){
//   //   if(strcmp(buff,"###")==0)
//   //     printf("End of a msg \n" );
//   //
//   //   printf("%s\n",buff );
//   //
//   // }
//
//   return 0;
//
// }
