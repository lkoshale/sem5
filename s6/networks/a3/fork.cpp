
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

int pid = 6;
int n = 5;
//create n-1 to 1 process
void create_proc(int n){
    if(n>=1){
        if(fork()==0)
            pid = n;
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

void readinput(){
FILE* fp = fopen("in.txt","r");
    int l=0;
    char buf[1024],link[1024];
    if(fscanf(fp,"%s %s\n",buf,link)!=EOF){
        n = atoi(buf);
        l = atoi(link);
    }else
        Error("input file empty!!");
    
    printf("%d %d\n",n,l);
    char n1[100],n2[100],nMax[100],nMin[100];
    while(fscanf(fp,"%s %s %s %s\n",n1,n2,nMax,nMin)!=EOF){
        printf("%d %d -- %d %d\n",atoi(n1),atoi(n2),atoi(nMax),atoi(nMin));
    }
}

int main(){
  int n = 5;
    pid = 0;
    // create_proc(n-1);
    // readinput();
    string str = "hello 122";
    int spc = str.find('#');
    cout<<spc;
    // string sub = str.substr(spc+1,str.size()-spc);

    // cout<<stoi(sub)-1<<" "<<str.size()-spc-1<<"\n";
    cout<<"This is process "<<pid<<" "<<getpid()<<"\n";
    return 0;
}




