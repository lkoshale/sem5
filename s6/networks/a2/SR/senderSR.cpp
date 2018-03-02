
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
#include <iomanip>

using namespace std;

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

queue < unsigned char* >BUFFER;   //for packets
queue < struct timespec > genTime;   //for storing time when they generated
bool endF  = false;
pthread_mutex_t lock;

//////////////////////
vector<int>SenderWindowACK;
pthread_mutex_t ack_lock;

vector<unsigned char* > SENDER_WINDOW;

vector < struct timespec > StartTime;

vector<int>attemptNo;
pthread_mutex_t attempt_lock;
/////////////////////////
int trasmtNo;
///////////////////

void Error(string cause ){
  string er = "ERROR : "+cause+" : ";
  perror(er.c_str());
  exit(EXIT_SUCCESS);
}

float get_RTT_AVE(){
	return RTT_SUM/PACK_SENT ;
}

void setInput( const char* arg, const char* val ){
	if(strcmp(arg,"-d")==0)
		DEBUG = true;
	else if(strcmp(arg,"-s")==0)
		IPADDRS = val;
	else if(strcmp(arg,"-p")==0)
		PORT = atoi(val);
	else if(strcmp(arg,"-n")==0)
		SNF = atoi(val);
	else if(strcmp(arg,"-L")==0)
		MAX_PACKET_LEN = atoi(val);
	else if(strcmp(arg,"-R")==0)
		PACKET_GEN_RATE = atoi(val);
	else if(strcmp(arg,"-N")==0)
		MAX_PACKETS = atoi(val);
	else if(strcmp(arg,"-W")==0)
		WINDOW_SIZE = atoi(val);
	else if(strcmp(arg,"-B")==0)
		BUFFER_SIZE = atoi(val);
	else
		Error("Invalid command line argument ");

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
			struct timespec start;

			clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &start);
	 		pthread_mutex_lock(&lock);
	 			BUFFER.push(packet);
	 			genTime.push(start);
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

      double sTime = start.tv_sec*1e6 + start.tv_nsec/1e3 ;  //in micro sec
      char tstr[10];
      sprintf(tstr,"%d",(int)sTime % 1000);
      tstr[2]='\0';

     if(DEBUG)
        cout<<fixed<<setprecision(2)<<"Seq# "<<seqNo<<" Time Generated : "<< (int)(sTime/1000) << ":"<< tstr <<"  RTT : "<<result/1000<<"  Num of attempts: "<<attemptNo[seqNo]<<"\n";

      pthread_mutex_lock(&attempt_lock);
      	attemptNo[seqNo]=0;
      pthread_mutex_unlock(&attempt_lock);

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

       	trasmtNo++;
       	pthread_mutex_lock(&attempt_lock);
       	attemptNo[misPack[i]]+=1;
       	pthread_mutex_unlock(&attempt_lock);

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

	
	DEBUG = false;
	IPADDRS = "127.0.0.1";
	PORT = 8080; 							//default port
	SNF = 4;
	MAX_PACKET_LEN = 256 ;
	PACKET_GEN_RATE = 20;
	MAX_PACKETS = 100;
	WINDOW_SIZE = 10 ;			//Max WS = 2^(SNF-1)
	BUFFER_SIZE = 100;


	//debug always the first arg -d
	if(argc%2==0 && argc > 1){
		setInput(argv[1],NULL);
		for(int i=2;i<argc;i+=2){
			setInput(argv[i],argv[i+1]);
		}
	}
	else if(argc>1){
		for(int i=1;i<argc;i+=2){
			setInput(argv[i],argv[i+1]);
		}
	}

  //initailize global var
  RTT_SUM = 0 ; 
  PACK_SENT = 0;
  trasmtNo = 0;

  for(int i=0;i<WINDOW_SIZE;i++){
  	unsigned char* temp = NULL;

  	SENDER_WINDOW.push_back(temp);
  	SenderWindowACK.push_back(-1);
  	attemptNo.push_back(0);

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
  		struct timespec start;
  		pthread_mutex_lock(&lock);
  		if(!BUFFER.empty()){
	 			packet =	BUFFER.front();
	 			BUFFER.pop();
	 			start = genTime.front();
	 			genTime.pop();
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
	 		StartTime[seqNo] = start;
	 		clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &StartTime[seqNo]);

    	if(sendto(sockfd,packet,MAX_PACKET_LEN,0,(struct sockaddr *)& address,sizeof(address)) != MAX_PACKET_LEN)
        Error("send error");

      trasmtNo++;
      UNACK_PACK_COUNT++;
      pthread_mutex_lock(&attempt_lock);
      attemptNo[seqNo]+=1;
     	pthread_mutex_unlock(&attempt_lock);

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
  		
  		if(PACK_SENT == (MAX_PACKETS - (MAX_PACKETS%WINDOW_SIZE)) ){
  			if(MAX_PACKETS%WINDOW_SIZE != 0)
  				WINDOW_SIZE = MAX_PACKETS%WINDOW_SIZE;

  		}

  	}

  	UNACK_PACK_COUNT = 0;
  }

  endF=true;
  usleep(1000);  //sleep for 1millisec
  cout<<"\n";
  cout<<"Output :  PktRate = " <<PACKET_GEN_RATE << " Length = "<< MAX_PACKET_LEN  << "  ";
  cout<< fixed<<setprecision(2)<<"Retran Ratio = "  << (float)trasmtNo/PACK_SENT << "  Avg RTT = "<<get_RTT_AVE()/1000 << "\n";
  pthread_join(tid,NULL);
  exit(EXIT_SUCCESS);
  // pthread_join(recvThread,NULL);

  return 0;

}
 
