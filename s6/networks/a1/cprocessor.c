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

char userIDFileName[] = "UserIDs.txt";

char* UserIdArray[NumUsers];
int Ulen=0;

// spool file pointer for current user
FILE* currentUser = NULL;
int currentUserMailNo = 0;
char* currentUserID = NULL;

FILE* file;


//fiils the array from persitent userids stored aaray
void fillUserIdsInArray(){
  FILE* fp;
  fp = fopen(userIDFileName,"a+");
  UserIdArray[Ulen]= (char*)malloc(sizeof(char)*UserNameLen);

  while (fscanf(fp,"%s",UserIdArray[Ulen]) != EOF) {
      printf("%s\n",UserIdArray[Ulen] );
      Ulen++;
      UserIdArray[Ulen]= (char*)malloc(sizeof(char)*UserNameLen);
  }
  fclose(fp);
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
    fprintf(fp,"%s\n",userid);
    UserIdArray[Ulen] = userid;
    Ulen++;
    return true;
  }
}

char* listUser( ){
  char* str = (char*)malloc( sizeof(char)*NumUsers*UserNameLen);
  int i;
  for( i=0;i<Ulen;i++){
    strcat(str,UserIdArray[i]);
    strcat(str," , ");
  }
  return str;
}


int setUser(char* userId){
  if(!UserExists(userId)){
    return -1;
    currentUserID = NULL;
  }else{
    currentUser = fopen(userId,"a+");
    char buffer[MAIL_SIZE];
    int count = 0;
    while(fscanf(currentUser,"%s",buffer)!=EOF){
      if(strcmp(buffer,"###")==0)
        count++;
    }
    rewind(currentUser);
    currentUserID = (char*)malloc(sizeof(char)*(strlen(userId)+5));
    strcpy(currentUserID,userId);
    return count;
  }

}

void sendReadMailResponse(char* mail){
  printf("%s\n",mail );
}

int ReadMail(){
  char* str= (char*)malloc(sizeof(char)*MAIL_SIZE);
  if(currentUser==NULL)
    return -1;
  else{
      int k = 0;
      char buffer[MAIL_SIZE];
      while(fscanf(currentUser,"%s",buffer)!=EOF){

        if(strcmp(buffer,"###")==0)
          break;
        else{
          if(strcmp(buffer,"Subject:")==0){
              k=1;
              continue;
          }
          if(k==1){
              strcat(str,buffer);
          }
        }
      }
  }

  sendReadMailResponse(str);
  str = NULL;
  currentUserMailNo++;
  return 0;
}


int deleteMail(){

  if(currentUser==NULL)
    return -1;
  else{
    FILE* tempFile = fopen("temp","w");

    rewind(currentUser);
    char ch = getc(currentUser);
    int mNo = 0;
    int mtrack = 0;
    while(ch!=EOF){
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

      ch = getc(currentUser);

    }

    fclose(tempFile);
    fclose(currentUser);

    remove(currentUserID);
    rename("temp",currentUserID);

    currentUser = fopen(currentUserID,"a+");

    rewind(currentUser);
    int lineNo = 0;
    char buff[MAIL_SIZE];
    while(fscanf(currentUser,"%s",buff)!=EOF){

      if(strcmp(buff,"###")==0)
        lineNo++;

      if(currentUserMailNo==lineNo)
        break;
    }

  }
  return 1;
}


void writeToFile(FILE* fp,char* filename,char* from,char* to ,char* date,char* sub,char* msg){
    fp = fopen(filename,"a+");
    fprintf(fp, "From: %s\nTo: %s\nDate: %s\nSubject: %s\n%s\n###\n",from,to,date,sub,msg);
}


void sendMail(char* toUser,char* msg){

  char dateAndTime[1024];
  time_t t = time(NULL);
  struct tm tm = *localtime(&t);

  sprintf(dateAndTime,"now: %d-%d-%d %d:%d:%d", tm.tm_year + 1900, tm.tm_mon + 1, tm.tm_mday, tm.tm_hour, tm.tm_min, tm.tm_sec);

  if(!UserExists(toUser)){
      printf(" Error the user trying to send doesnt eist !!" );
      return;
  }

  if(currentUserID!=NULL&&currentUser!=NULL){
    FILE* toSend = fopen(toUser,"a+");
    fprintf(toSend, "From: %s\nTo: %s\nDate: %s\nSubject: %s",currentUserID,toUser,dateAndTime,msg );
    fclose(toSend);
  }

}

void DoneUser(){
  fclose(currentUser);
  currentUser = NULL;
  currentUserID = NULL;
  currentUserMailNo = 0;
}


int main(int argc, char const *argv[]) {

  file = fopen("u1.txt","a+");
  // file = fopen("user1","a+");
  // fprintf(file, "writing to file\n" );
  char buff[10000];

  fillUserIdsInArray();
  createUser("user2");
  printf("%s\n", listUser() );
  printf("%d\n", setUser("user1") );
  // printf("%d\n", ReadMail() );
  // printf("%d\n", deleteMail());
  // printf("%d\n", ReadMail());
  //
  sendMail("user2","first msg\n.....\ncontents\n..... \n###\n");
  DoneUser();
  printf("%d\n",setUser("user2"));
  printf("%d\n", ReadMail());



  // while(fscanf(file,"%s",buff)!=EOF){
  //   if(strcmp(buff,"###")==0)
  //     printf("End of a msg \n" );
  //
  //   printf("%s\n",buff );
  //
  // }

  return 0;

}
