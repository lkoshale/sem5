#include <stdio.h>
#include <omp.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/time.h>
#include <time.h>

#define N 10000000

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

    if ( argc != 2 ) {
        printf("Number of threads not specified\n");
        exit(-1);
    }
    nThreads = atoi(argv[1]);
    if ( nThreads <= 0 ) {
    printf("Num threads <= 0\n");
    exit(-1);
    }
    
    for(int i=0; i< N; i++ ) {
        a[i] = 1;b[i]=2;c[i]=0;
    }
        
    omp_set_num_threads(nThreads);
    double t1,t2;
    int chunk = N/nThreads;

    clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &start);

    #pragma omp parallel shared(a,b,c,chunk)
    {
        #pragma omp parallel for schedule(static)
        for(int i=0; i< N; i++ ) {
        //  if(i%2 == 0)
            a[i] = b[i] - c[i];
        }

    }

    clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &stop);
    double result = (stop.tv_sec - start.tv_sec) * 1e6 + (stop.tv_nsec - start.tv_nsec) / 1e3;    // in microseconds
    printf("Time : %lf %d \n",result,nThreads);

    return 0;
}
