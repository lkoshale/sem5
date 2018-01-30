
#define  __USE_GNU
#include <iostream>
#include <cstdlib>
#include <sys/types.h>
#include <sys/sysinfo.h>
#include <sys/mman.h>
#include <unistd.h>
#include <stdio.h>

#include <thread>
#include <sched.h>
#include <ctype.h>
#include <string.h>
#include <chrono>
#include <vector>
#include <algorithm>
#include <mutex>
#include <ctime>


#define NUM_THREADS 20

using namespace std;


int sched_getcpu(void);
mutex m;
clock_t begin_t;

//per thread data
// time -> (process id, cpu running on)
vector<pair<double,pair<pid_t,int> > > thread_data(NUM_THREADS);
bool isShuffle = false;
int NUM_PROCS = sysconf(_SC_NPROCESSORS_CONF);


bool overThreshhold(double d1, double d2){
    double threshhold_percentage=0.5;
    if(d1/d2>=threshhold_percentage){
        return true;
    }
    return false;
}


void foo(int ii)
{
  // do stuff...
  clock_t in_t, get_t;

  for(int i=0;i<1000;i++){
    in_t=clock();

    m.lock();
    //----------CS BEGIN--------------------------------------
      get_t=clock();
      //  cout<<"foo: "<<ii<<" "<<sched_getcpu()<<endl;
      for(int j=0;j<10000;j++);
    //----------CS END----------------------------------------
    m.unlock();
    //Computing lock time.
    double curTime=thread_data[ii].first;
    double newTime=double(get_t-in_t);
    double totTime=double(clock()-begin_t);
    if(thread_data[ii].first!=0){
      cout<<totTime;
      if(overThreshhold(curTime+newTime, totTime)){
        if(!isShuffle)  cout<<curTime+newTime<<" "<<totTime<<" "<<ii<<" waited. ";
        isShuffle=true;
      }
    }
    thread_data[ii].first=curTime+newTime;
    thread_data[ii].second.first=getpid();
    thread_data[ii].second.second=sched_getcpu();
 //   cout<<ii<<" waited for "<<double(get_t-in_t)<<endl;
  }

}



//background shuffling thread
void shuffle1(){

  while(true){

    if(isShuffle){
      sort(thread_data.begin(),thread_data.end());
      int x = NUM_THREADS/NUM_PROCS;
      int r =  NUM_THREADS%NUM_PROCS;
      int y = NUM_PROCS;
      int pre =0,z=1;
      int i = 0;
      while (y--) {

        for(i = pre;i<pre+x+z;i++){
          cpu_set_t mask;
          /* CPU_ZERO initializes all the bits in the mask to zero. */
           CPU_ZERO( &mask );
           /* CPU_SET sets only the bit corresponding to cpu. */
           CPU_SET(y, &mask );

           //NOTE problem in scheduling 
           if(sched_setaffinity(thread_data[i].second.first ,sizeof(mask),&mask)==-1){
             cout<<" Error !! cant schedule to the cpu \n";
           }

        }

        r--;
        pre = i;

        if(r>0) z=1;
        else z=0;


      }
      //int sched_setaffinity(pid_t pid, size_t cpusetsize,const cpu_set_t *mask);
      isShuffle = false;
    }


  }

      // sleep shuffling thread for 5 second
      this_thread::sleep_for (std::chrono::seconds(5));


}



int main(int argc, char const *argv[]) {

  vector<thread> vec(NUM_THREADS);
  
  
  thread t = thread(shuffle1);
  t.detach();

  for (size_t i = 0; i < NUM_THREADS; i++) {
      vec[i] = thread(foo,i);
  }



  for(int i=0;i<NUM_THREADS;i++){
    vec[i].join();
    cout<<thread_data[i].first<<endl;
  }
//
// if(isShuffle)
//       cout<<"Threshhold crossed!!"<<endl;



  /* code */
  return 0;
}
