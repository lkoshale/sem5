
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>
#include <limits.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/time.h>
#include <iostream>
#include <string>
#include <vector>
#include <map>
#include <sstream>
#include <algorithm>
#include <iterator>
#include <queue>
#include <set>

using namespace std;

int PID;
int N = 3;
double startTime;

// udp data
int PORT = 10000;
int sockid;
struct sockaddr_in address;

//LSA DATA
int LSA_SEQ_NO=0;

//input
int id=0;
char infile[1024] = "in.txt";
char outfile[1024] = "outfile";
int HELLO_INT = 1;  //sec
int LSA_INT = 5; //sec
int SPF_INT= 20; //sec

//network data
class nData{
public: int node2,minC,maxC;
nData(int a,int b,int c){node2=a,minC=b,maxC=c;}
};

class cData{
    public: int node,val,cost;string path;vector<pair<cData*,int> >adj;
    cData(int n){node=n; path=""; cost=INT_MAX;}
    void addEdge(cData* n,int val){adj.push_back(pair<cData*,int>(n,val));}
};

// class mycomp{
//     public:
//         int operator()(cData* c1,cData* c2 ){
//             return (c1->cost) > (c2->cost) ;
//         }
// };

vector< vector<nData> >network;
map<int,int>NCostMap;
map<int,string>NLSAMap;

//NCOSTMAP lock required 
pthread_mutex_t NC_lock;
pthread_mutex_t NLSA_lock;

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
void* LSA(void* args);
void* SPF(void* args);
void fillnetwork();
int Cost(int node1,int node2);
string createLSA();
void advertiseLSA(string str,int src);
void setInput(const char* arg,const char* val);
double rtclock(void);


int main(int argc,char* argv[]){
    startTime = rtclock();
    //TODO get cmndline
    if(argc>1){
        if(argc%2==0)
            Error("comandline argument missing");
        else{
            for(int i=1;i<argc;i+=2)
                setInput(argv[i],argv[i+1]);
        }
    }
    //take input
    fillnetwork();
    // for(int i=0;i<N;i++){
    //     for(int j=0;j<network[i].size();j++){
    //         nData t = network[i][j];
    //         printf("%d %d %d %d \n",i,t.node2,t.minC,t.maxC);
    //     }
    // }
  
    PID = 0;  //0th proc
    create_proc(N-1);

    printf("created process %d \n",PID);
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
        Error("error in binding "+to_string(PID));
    
    pthread_t send_h ,recv_h,lsa_h,spf_h;
    void* arg = NULL;
    int rcv = pthread_create(&recv_h,NULL,recv_hello,arg);
    if(rcv!=0)
        Error("cant create thred for recv");
    
    int snd = pthread_create(&send_h,NULL,send_hello,arg);
    if(snd!=0)
        Error("cant create thread to send");

    //////////////////////////////////////////
    sleep(5);
    int lsa_suc = pthread_create(&lsa_h,NULL,LSA,arg);
    if(lsa_suc!=0)
        Error("cant create thread for lsa");

    sleep(5);
    int spf_suc = pthread_create(&spf_h,NULL,SPF,arg);
    if(spf_suc!=0)
        Error("cant create thread for spf");

    ///////////////////////////////////////////
    pthread_join(lsa_h,NULL);
    pthread_join(recv_h,NULL);
    pthread_join(send_h,NULL);
    return 0;
}

int Cost(int node1,int node2){
    for(int i=0;i< network[node1].size();i++){
        nData temp = network[node1][i];
        if(temp.node2 == node2)
            return( ( rand() % (temp.maxC - temp.minC) )+ temp.minC );
    }
    return 0; 
}

void* recv_hello(void* args){

    socklen_t addressLen = sizeof(address);
    
    while(true){
        char* buffer = (char*)malloc(sizeof(char)*1024);
        int num;
        if ((num = recvfrom(sockid,buffer,1024,0,(struct sockaddr*)&address,(socklen_t*)&addressLen))== -1) {
            Error("recv_hello");
        }
        string str(buffer);

        if(str.find("LSA")!=-1){    //LSA packet
            // printf("%s got in %d\n",buffer,PID);
            istringstream iss(str);
            vector<string> tokens{istream_iterator<string>{iss},istream_iterator<string>{}};
            // for(auto itr = tokens.begin();itr!= tokens.end();itr++){
            //     cout<<*itr<<" * ";
            // }
            int srcID = stoi(tokens[1]);
            map<int,string>::iterator itr;
            bool advertise = false;
            //critical section
            pthread_mutex_lock(&NLSA_lock);
            itr = NLSAMap.find(srcID);
            if(itr==NLSAMap.end()){
                NLSAMap.insert(pair<int,string>(srcID,str));
                advertise = true;
            }else{
                istringstream iss_new(itr->second);
                vector<string> tokens_new{istream_iterator<string>{iss_new},istream_iterator<string>{}};
                if(stoi(tokens[2]) > stoi(tokens_new[2])){
                    itr->second = str;   //update
                    advertise = true;
                }
            }
            pthread_mutex_unlock(&NLSA_lock);

            if(advertise)
                advertiseLSA(str,srcID);


        }else if(str.find('#')!=-1){    //reply packet
            //extract cost and check packet
            string response(buffer);
            int j_sp = response.find(' ');
            int i_hash = response.find('#');
            int c_at = response.find('@');
            string j_str = response.substr(j_sp+1,i_hash-j_sp-1);
            string i_str = response.substr(i_hash+1,c_at-i_hash-1);
            string cost_str = response.substr(c_at+1,response.size()-c_at);

            int i_node = stoi(i_str);
            int j_node = stoi(j_str);
            int cost_val = stoi(cost_str);

            if(i_node != PID){
                buffer = NULL;
                continue;   //check integrity of packet
            }

            map<int,int>::iterator itr;
            //Critical section
            pthread_mutex_lock(&NC_lock);
            itr = NCostMap.find(j_node);
            if(itr==NCostMap.end()){
                NCostMap.insert(pair<int,int>(j_node,cost_val));
            }else{
                itr->second = cost_val ;//update the cost TODO
            }
            pthread_mutex_unlock(&NC_lock);

            // printf("%s in -- %d // %d. %d. %d\n",buffer,PID,j_node,i_node,cost_val);
        }
        else {//hello msg
            int spc = str.find(' ');
            string sub = str.substr(spc+1,str.size()-spc);
            int from = stoi(sub); //c++11

            char sendmsg[1024];
            sprintf(sendmsg,"HelloRelpy %d#%d@%d",PID,from,Cost(PID,from));
            // if(sendto(sockid,sendmsg,strlen(sendmsg),0,(struct sockaddr *)& address,sizeof(address)) != strlen(sendmsg))
            //     Error("send error");
            sendmsg[strlen(sendmsg)]='\0';
            int sockfd = socket(PF_INET,SOCK_DGRAM,IPPROTO_UDP);
            struct sockaddr_in address;
            address.sin_family = AF_INET;
            address.sin_port = htons(PORT+from);
            address.sin_addr.s_addr = inet_addr("127.0.0.1");
            memset(address.sin_zero, '\0', sizeof address.sin_zero); 

            if(sockfd<0)
                Error("socket creation error!! ");

            char msg[1024];
            strcpy(msg,sendmsg);
            if(sendto(sockfd,msg,strlen(msg),0,(struct sockaddr *)& address,sizeof(address)) != strlen(msg))
                Error("send error different number of bbytes sent");
            //
            close(sockfd);
            // printf("%s in %d , %s\n",buffer,PID,sendmsg);
            
        }
        
        buffer = NULL;
    }
}

//run in different thread sends message
void* send_hello(void* args){
   while(true){
    
    for(int i=0;i<network[PID].size();i++){
        nData nbr = network[PID][i];

        int sockfd = socket(PF_INET,SOCK_DGRAM,IPPROTO_UDP);
        struct sockaddr_in address;
        address.sin_family = AF_INET;
        address.sin_port = htons(PORT+nbr.node2);
        address.sin_addr.s_addr = inet_addr("127.0.0.1");
        memset(address.sin_zero, '\0', sizeof address.sin_zero); 

        if(sockfd<0)
            Error("socket creation error!! ");

        char msg[1024];
        sprintf(msg,"hello %d",PID); 
        msg[strlen(msg)]='\0';
        if(sendto(sockfd,msg,strlen(msg),0,(struct sockaddr *)& address,sizeof(address)) != strlen(msg))
            Error("send error different number of bbytes sent");

        close(sockfd);
    }
   
    sleep(HELLO_INT);
   
   }
}

void* LSA(void* args){
    while(true){
        string lsa_str = createLSA();
        for(int i=0;i<network[PID].size();i++){
            nData nbr = network[PID][i];

            int sockfd = socket(PF_INET,SOCK_DGRAM,IPPROTO_UDP);
            struct sockaddr_in address;
            address.sin_family = AF_INET;
            address.sin_port = htons(PORT+nbr.node2);
            address.sin_addr.s_addr = inet_addr("127.0.0.1");
            memset(address.sin_zero, '\0', sizeof address.sin_zero); 

            if(sockfd<0)
                Error("socket creation error!! ");
            
            if(sendto(sockfd,lsa_str.c_str(),lsa_str.size(),0,(struct sockaddr *)& address,sizeof(address)) != lsa_str.size())
                Error("send error different number of bbytes sent");

            close(sockfd);
        }
    
        sleep(LSA_INT);
    
   }
}

vector<cData*> makeGraph(){
    vector<cData*>g;
    for(int i=0;i<N;i++){
        cData* temp = new cData(i);
        g.push_back(temp);
    }

    string myLSA = createLSA();
    //critical section
    pthread_mutex_lock(&NLSA_lock);
    map<int,string>tNLSAMap(NLSAMap);
    pthread_mutex_unlock(&NLSA_lock);

    for(auto itr = tNLSAMap.begin();itr!=tNLSAMap.end();itr++){
        cData* ptr = g[itr->first];
        istringstream iss(itr->second);
        vector<string> tokens{istream_iterator<string>{iss},istream_iterator<string>{}};
        int size = stoi(tokens[3]);
        for(int i=4;i<(2*size)+4;i+=2){
            int neghb = stoi(tokens[i]);
            int val = stoi(tokens[i+1]);
            ptr->addEdge(g[neghb],val);
        }
    }

    //add this node
    cData* ptr = g[PID];
    istringstream iss(myLSA);
    vector<string> tokens{istream_iterator<string>{iss},istream_iterator<string>{}};
    int size = stoi(tokens[3]);
    for(int i=4;i<(2*size)+4;i+=2){
        int neghb = stoi(tokens[i]);
        int val = stoi(tokens[i+1]);
        ptr->addEdge(g[neghb],val);
    }

    // return graph
    return g;
}

// pair<cData*,set<cData*>::iterator> getMinNode(set<cData*> setN){
//     set<cData*>::iterator pos = setN.begin();
//     cData* min = *pos;
//     for(auto itr= setN.begin(); itr!= setN.end();itr++){
//         if((*itr)->cost < min->cost){
//             min = *itr;
//             pos = itr;
//         }
//     }

//     return pair<cData*,set<cData*>::iterator>(min,pos);
// }

void printGraph(vector<cData*> graph){
     for(int i=0;i<graph.size();i++){
         cout<<graph[i]->node<<"-";
         for(int j=0;j<graph[i]->adj.size();j++){
             cout<<graph[i]->adj[j].first->node<<" ";
         }
         cout<<"\n";
     }
}

void* SPF (void* args){
    while(true){
        vector<cData*> graph = makeGraph();
        // if(PID==0){
        //     printGraph(graph);
        // }
       // run djkstra algo
        set<cData*> nodes;
        for(int i=0;i<graph.size();i++)
            nodes.insert(graph[i]);
        
        graph[PID]->cost = 0;
        graph[PID]->path =to_string(PID);

        while(!nodes.empty()){
            //pair<cData*,set<cData*>::iterator> minNode = getMinNode(nodes);
            set<cData*>::iterator pos = nodes.begin();
            cData* min = *pos;
            for(auto itr= nodes.begin(); itr!= nodes.end();itr++){
                if((*itr)->cost < min->cost){
                    min = *itr;
                    pos = itr;
                }
            }
           
            for(int i=0;i<min->adj.size();i++){
                if(min->adj[i].first->cost > min->cost+min->adj[i].second){
                    min->adj[i].first->cost = min->cost+min->adj[i].second;
                    min->adj[i].first->path = min->path+"-"+to_string(min->adj[i].first->node);
                }
            }

            nodes.erase(pos);
        }
        
    //djsktra over
        double endTime = rtclock();
        char of[1024];
        strcpy(of,outfile);
        strcat(of,to_string(PID).c_str());
        FILE* fptr = fopen(of,"w");
        fprintf(fptr,"Routing Table For Node %d at Time %lf\ndest\tpath\tcost\n",PID,endTime-startTime);
        string dataVal = "";
        for(int i=0;i<graph.size();i++){
            if(i==PID)continue;
            dataVal+=to_string(graph[i]->node)+" \t "+graph[i]->path+" \t "+to_string(graph[i]->cost)+"\n";
        }

        fprintf(fptr,"%s",dataVal.c_str());
        printf("Routing Table For Node %d at Time %lf\ndest \t path \t cost\n%s",PID,endTime-startTime,dataVal.c_str());
        fclose(fptr);

        sleep(SPF_INT);
    }

}

string createLSA(){
    //critical section
    pthread_mutex_lock(&NC_lock);
    string str="LSA "+to_string(PID)+" "+to_string(LSA_SEQ_NO)+" "+to_string(NCostMap.size())+" ";
    for(auto itr = NCostMap.begin();itr!=NCostMap.end();itr++){
        str+= to_string(itr->first)+" "+to_string(itr->second)+" ";
    }
    pthread_mutex_unlock(&NC_lock);
    LSA_SEQ_NO++;
    return str;
}

void advertiseLSA(string msg , int src){
    for(int i=0;i<network[PID].size();i++){
        nData nbr = network[PID][i];
        if(nbr.node2==src)
            continue;

        int sockfd = socket(PF_INET,SOCK_DGRAM,IPPROTO_UDP);
        struct sockaddr_in address;
        address.sin_family = AF_INET;
        address.sin_port = htons(PORT+nbr.node2);
        address.sin_addr.s_addr = inet_addr("127.0.0.1");
        memset(address.sin_zero, '\0', sizeof address.sin_zero); 

        if(sockfd<0)
            Error("socket creation error!! ");
        
        if(sendto(sockfd,msg.c_str(),msg.size(),0,(struct sockaddr *)& address,sizeof(address)) != msg.size())
            Error("send error different number of bbytes sent");

        close(sockfd);
        
    }
}


void fillnetwork(){
    FILE* fp = fopen(infile,"r");
    int l=0;
    char buf[1024],link[1024];
    if(fscanf(fp,"%s %s\n",buf,link)!=EOF){
        N = atoi(buf);
        l = atoi(link);
    }else
        Error("input file empty!!");
    
    // printf("%d %d\n",N,l);
    for(int i=0;i<N;i++){
        vector<nData>temp;
        network.push_back(temp);
    }

    char n1[100],n2[100],nMax[100],nMin[100];
    while(fscanf(fp,"%s %s %s %s\n",n1,n2,nMin,nMax)!=EOF){
        int node1 = atoi(n1); int node2 = atoi(n2);
        int min = atoi(nMin); int max = atoi(nMax);
        nData data1(node2,min,max);
        nData data2(node1,min,max);
        if(node1 < N && node2 < N){
            network[node1].push_back(data1);
            network[node2].push_back(data2);
        }else
            Error("invalid input file node out of range!!");
    }
}

void setInput(const char* arg,const char* val){
    if(strcmp(arg,"-i")==0)
        id = atoi(val);
    else if(strcmp(arg,"-f")==0)
        strcpy(infile,val);
    else if(strcmp(arg,"-o")==0)
        strcpy(outfile,val);
    else if(strcmp(arg,"-h")==0)
        HELLO_INT = atoi(val);
    else if(strcmp(arg,"-a")==0)
        LSA_INT = atoi(val);
    else if(strcmp(arg,"-s")==0)
        SPF_INT = atoi(val);
}

double rtclock(void)
{
  struct timezone Tzp;
  struct timeval Tp;
  int stat;
  stat = gettimeofday (&Tp, &Tzp);
  if (stat != 0) printf("Error return from gettimeofday: %d",stat);
  return(Tp.tv_sec + Tp.tv_usec*1.0e-6);
}

//  priority_queue<cData*> pq;
//     graph[PID]->cost=0;
//     pq.push(graph[PID]);
//     while(!pq.empty()){
//         cData* temp = pq.top();
//         pq.pop();
//         set<cData*>set_adj;
//         for(int i=0;i<temp->adj.size();i++){
//             if(temp->adj[i].first->cost > temp->cost+temp->adj[i].second){
//                 temp->adj[i].first->cost = temp->cost+temp->adj[i].second;
//             }
//             set_adj.insert(temp->adj[i].first);
//         }
//         for(auto itr = set_adj.begin();itr!=set_adj.end();itr++){
//             pq.push()
//         }
//     }