
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>
#include <stdbool.h>
#include <limits.h>
#include <time.h>
#include <iostream>
#include <string>
#include <vector>
#include <random>


using namespace std;

////////////////////
double Ki=1;
double Km=1;
double Kn=1;
double Kf=0.2;
double Ps=0.99;
int T = 100;
char outfile[1024]="outfile.txt";

int RWS = 1024;//in KB
int MSS = 1;//in KB
double CW = Ki*MSS;
double CTH = CW/2; 

///////////////////
const int PHASE_EXP = 1;
const int PHASE_LIN = 2;

/////////////



void Error(string cause ){
  string er = "ERROR : "+cause+" : ";
  perror(er.c_str());
  exit(EXIT_SUCCESS);
}



int main(int argc,char* argv[]){

    //TODO take input
    
    // slow start
    int PHASE = PHASE_EXP;

    srand(time(NULL));
    // srand(20);
    int pLimit= 1000000;
    int numPack = 0;
    int updateNo = 0;

    FILE* fptr = fopen(outfile,"w");
    int r=0,l=0;
    while(numPack<T){

        //send ceil number of packets
        for(int i=0;i<ceil(CW/MSS);i++){
        
            //prob of recv ack
            int num = rand()%pLimit+1;

            if(num < Ps*pLimit){
                // ack recieved
                //slow start
                if(PHASE == PHASE_EXP){
                    if( CW <= CTH ){

                        if(RWS > (CW+Km*MSS))
                            CW = CW + (Km*MSS);
                        else
                            CW = RWS;
                        
                       
                        updateNo++;
                    }
                    else{
                        PHASE = PHASE_LIN;
                    }
                }
               
                // congestion avoidance
                if(PHASE == PHASE_LIN){
                    
                    if(RWS < CW+(Kn*MSS*(MSS/CW)))
                        CW = RWS;
                    else
                        CW = CW+(Kn*MSS*(MSS/CW));
                    
                    
                    updateNo++;
                }

                r++;
            }else{  //1-Ps   timeout or error
                //no ack
                //new CW

                CTH = CW/2;

                if( CW*Kf > 1)
                    CW = CW*Kf;
                else
                    CW = 1;
                
                updateNo++;

                PHASE = PHASE_EXP;

                l++;
            }

            fprintf(fptr,"%d %lf \n",updateNo,CW);

            numPack++;
            if(numPack>=T)
                break;
        }
    }

    fclose(fptr);
    cout<<(float)r/T<<" "<<(float)l/T<<"\n";

}
