
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
#include <string>
#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>

using namespace std;

// #define PORT 8080
///////////////////////////////

bool DEBUG ;
string IPADDRS;
int PORT,SNF,MAX_PACKET_LEN,PACKET_GEN_RATE,MAX_PACKETS,WINDOW_SIZE,BUFFER_SIZE;

double RTT_SUM;
int PACK_SENT;
//////////////////////
int sockfd ;
struct sockaddr_in address;

///////////////////////

queue < unsigned char* >BUFFER;
bool endF  = false;
pthread_mutex_t lock;

//////////////////////
vector<int>SenderWindowACK;
pthread_mutex_t ack_lock;

vector<unsigned char* > SENDER_WINDOW;

vector < struct timespec > StartTime;
/////////////////////////

void Error(string cause ){
  string er = "ERROR : "+cause+" : ";
  perror(er.c_str());
  exit(EXIT_SUCCESS);
}

float get_RTT_AVE(){
	return RTT_SUM/PACK_SENT ;
}

//geneartes packets based on rate in different thread
void* genratePacket(void* args){
	 int count = 0;

	 if(PACKET_GEN_RATE > 1000000){
	 	cout<<"Too HIGh PACKAGE GEN RATE \n";
	 	exit(0);
	 }

	 float onepacktime = (1.0/PACKET_GEN_RATE)*1000000;
	 unsigned int microsec = (unsigned int)ceil(onepacktime);
	 while(!endF){
	 	unsigned char* packet = (unsigned char*) malloc(sizeof(unsigned)*MAX_PACKET_LEN);
	 	if(BUFFER.size() < BUFFER_SIZE ){
	 		packet[0] = count % WINDOW_SIZE;
	
	 		pthread_mutex_lock(&lock);
	 			BUFFER.push(packet);
	 		pthread_mutex_unlock(&lock);
	 		count++;
	 		//sleep
	 		usleep(microsec);
	 	}
	 	// cout<<"packet No "<<(int)count<<"\n";
	 }

}

void* recive_ACK_fn(void* args){

	socklen_t addressLen = sizeof(address);

	while(!endF){
		  char recvBuff[1024] = {0};
		  int num;
		  struct timespec stop;
      if ((num = recvfrom(sockfd,recvBuff,1024,0,(struct sockaddr*)&address,(socklen_t*)&addressLen))== -1) {
         perror("recvfrom");
         exit(1);
      }


      //packet sent properly
      int seqNo = atoi(recvBuff);
      if(seqNo >= WINDOW_SIZE)
      	Error("Sequence no out of range in ack");

      clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &stop);
     	
     	struct timespec start = StartTime[seqNo];
     	double result = (stop.tv_sec - start.tv_sec) * 1e6 + (stop.tv_nsec - start.tv_nsec) / 1e3;    // in microseconds

      pthread_mutex_lock(&ack_lock);
      SenderWindowACK[seqNo] = 1;	
      pthread_mutex_unlock(&ack_lock);

     	RTT_SUM+=result;
      PACK_SENT++;

      cout<<"ack "<<seqNo<<" -- "<<result<<" "<<PACK_SENT<<"\n";
	}

}

vector<int> check_miss(){

	vector<int> Pmiss;

	for(int i=0;i<WINDOW_SIZE;i++){
		// pthread_mutex_lock(&ack_lock);
		if(SenderWindowACK[i]==-1)
			  Pmiss.push_back(i);
		// pthread_mutex_unlock(&ack_lock);
	}
	

	return Pmiss;
}

void resend_packets(vector<int>misedPack){
	vector<int>misPack(misedPack);
	vector<int>attemptNo(WINDOW_SIZE,1);

	while(misPack.size()>0){

		//check time
		for(int i=0 ;i<misPack.size();i++){
			
			struct timespec stop,start;
			start = StartTime[misPack[i]];
			clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &stop);
			double msec = (stop.tv_sec - start.tv_sec) * 1e6 + (stop.tv_nsec - start.tv_nsec) / 1e3;    // in microseconds


			int time_limit ;
			if(PACK_SENT <= 10)
				time_limit = 300000; //300 milli sec
			else
				time_limit = 2*get_RTT_AVE();

			if(msec > time_limit ){

				clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &StartTime[misPack[i]]);
				if(sendto(sockfd,SENDER_WINDOW[misPack[i]],MAX_PACKET_LEN,0,(struct sockaddr *)& address,sizeof(address)) != MAX_PACKET_LEN)
       	 Error("send error");

       	attemptNo[misPack[i]]+=1;
      }

		}

		
		misPack = check_miss();

		if ( find(attemptNo.begin(), attemptNo.end(), 10) != attemptNo.end() ){
			Error("maximum attempt for re transmitting packet exceeded");
			exit(EXIT_SUCCESS);
			break;
		}

	}

}


int main(int argc, char const *argv[]) {

	if(argc == 18){
		DEBUG = true;
		IPADDRS = argv[3];
		PORT = atoi(argv[5]);
		SNF=atoi(argv[7]);
		MAX_PACKET_LEN = atoi(argv[9]);
		PACKET_GEN_RATE = atoi(argv[11]);
		MAX_PACKETS = atoi(argv[13]);
		WINDOW_SIZE = atoi(argv[15]);
		BUFFER_SIZE = atoi(argv[17]);
	}
	else if(argc==17){
		DEBUG = false;
		IPADDRS = argv[2];
		PORT = atoi(argv[4]);
		SNF=atoi(argv[6]);
		MAX_PACKET_LEN = atoi(argv[8]);
		PACKET_GEN_RATE = atoi(argv[10]);
		MAX_PACKETS = atoi(argv[12]);
		WINDOW_SIZE = atoi(argv[14]);
		BUFFER_SIZE = atoi(argv[16]);
	}
	else if(argc==1){
		DEBUG = false;
		IPADDRS = "127.0.0.1";
  	PORT = 8080;
  	SNF = 4;
  	MAX_PACKET_LEN = 256 ;
  	PACKET_GEN_RATE = 20;
  	MAX_PACKETS = 100;
  	WINDOW_SIZE = 10 ;			//Max WS = 2^(SNF-1)
  	BUFFER_SIZE = 100;

  }else{
    printf("please provide a PORT no in command line\n");
    exit(0);
  }

  //initailize global var
  RTT_SUM = 0 ; 
  PACK_SENT = 0;

  for(int i=0;i<WINDOW_SIZE;i++){
  	unsigned char* temp = NULL;

  	SENDER_WINDOW.push_back(temp);
  	SenderWindowACK.push_back(-1);

  	struct timespec tm;
  	StartTime.push_back(tm);
  } 



 	sockfd = socket(PF_INET,SOCK_DGRAM,IPPROTO_UDP);
  address.sin_family = AF_INET;
  address.sin_port = htons(PORT);
  address.sin_addr.s_addr = inet_addr(IPADDRS.c_str());
  memset(address.sin_zero, '\0', sizeof address.sin_zero); 


  if(sockfd<0)
    Error("socket creation error!! ");


  int UNACK_PACK_COUNT = 0;


  pthread_t tid,recvThread;
  void* arg = NULL;
  int err = pthread_create(&tid,NULL,genratePacket,arg);
  if(err!=0)
  	Error("cant create thred for packet gen");

  int rcvErr = pthread_create(&recvThread,NULL,recive_ACK_fn,arg);
  if(rcvErr!=0)
  	Error("cant create thred to recv ack");

  // sleep(1);
  // endF = true;
  // pthread_join(tid, NULL);

  while(PACK_SENT < MAX_PACKETS ){


  	while(UNACK_PACK_COUNT < WINDOW_SIZE  && PACK_SENT < MAX_PACKETS ){
  		//get next packet in queue
  		bool con = false;
  		unsigned char* packet = NULL;
  		pthread_mutex_lock(&lock);
  		if(!BUFFER.empty()){
	 			packet =	BUFFER.front();
	 			BUFFER.pop();
	 		}
	 		else{
	 			con = true;
	 		}
	 		pthread_mutex_unlock(&lock);
	 		
	 		if(con)
	 			continue;

	 		//store packet and its time
	 		int seqNo = (int)packet[0];
	 		SENDER_WINDOW[seqNo]= packet;
	 		clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &StartTime[seqNo]);

    	if(sendto(sockfd,packet,MAX_PACKET_LEN,0,(struct sockaddr *)& address,sizeof(address)) != MAX_PACKET_LEN)
        Error("send error");

      UNACK_PACK_COUNT++;

    }

  	if(UNACK_PACK_COUNT == WINDOW_SIZE){
  		//check all ack recived
  		vector<int>misPack = check_miss();
  		if(misPack.size()>0){
  			//missin packet
  			//resend
  			resend_packets(misPack);
  		}

    		//make all ack -1
    	pthread_mutex_lock(&ack_lock);
    	for(int i=0;i<WINDOW_SIZE;i++){
    		SenderWindowACK[i]=-1;
    	}
      pthread_mutex_unlock(&ack_lock);
  		

  	}

  	UNACK_PACK_COUNT = 0;
  }

  endF=true;
  usleep(1000);  //sleep for 1millisec
  cout<<get_RTT_AVE() << " "<< PACK_SENT << "\n";
  pthread_join(tid,NULL);
  exit(EXIT_SUCCESS);
  // pthread_join(recvThread,NULL);

  return 0;

}
 
