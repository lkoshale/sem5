/* This file was automatically generated.  Do not edit! */
#define USER_SIZE 1000
#define UserNameLen 1024
#define NumUsers 1024
#define MAIL_SIZE 10000

void initializeSeverFile();
void DoneUser();
int sendMail(char *toUser,char *msg);
int deleteMail();
char* ReadMail();
void sendReadMailResponse(char *mail);
int setUser(char *userId);
char *listUser();
bool createUser(char *userid);
bool UserExists(char *userid);
void fillUserIdsInArray();
extern FILE *file;
extern char *currentUserID;
extern int currentUserMailNo;
extern FILE *currentUser;
extern int Ulen;
extern char *UserIdArray[NumUsers];
extern char userIDFileName[1024];
