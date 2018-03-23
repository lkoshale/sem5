#include <stdio.h>
#include <omp.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/time.h>
#include <time.h>

#define N 100000000

int a[N],b[N],c[N];


double rtclock(void)
{
  struct timezone Tzp;
  struct timeval Tp;
  int stat;
  stat = gettimeofday (&Tp, &Tzp);
  if (stat != 0) printf("Error return from gettimeofday: %d",stat);
  return(Tp.tv_sec + Tp.tv_usec*1.0e-6);
}

int main(int argc,char* argv[]) {
    int nThreads;
    struct timespec start,stop;

    // if ( argc != 2 ) {
    //     printf("Number of threads not specified\n");
    //     exit(-1);
    // }
    // nThreads = atoi(argv[1]);
    // if ( nThreads <= 0 ) {
    // printf("Num threads <= 0\n");
    // exit(-1);
    // }
    
    for(int i=0; i< N; i++ ) {
        a[i] = 1;b[i]=2;c[i]=0;
    }
    // printf("nthreads chunk time\n");
    
    // int num = 10;
    nThreads = 1;
    while (nThreads <= 4){
        omp_set_num_threads(nThreads);
        double t1,t2;
        int chunk = N/nThreads;

        t1 = rtclock();

       #pragma omp parallel shared(a,b,c,chunk)
        {
            #pragma omp for schedule(guided,chunk)
            for(int i=0; i< N; i++ ) {
             if(i%2 == 0)
                a[i] = b[i] - c[i];
            }

       }

        t2 = rtclock();
        printf("%lf %d \n",t2-t1,nThreads);
       nThreads++;
    }
    return 0;
}


