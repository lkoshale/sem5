#include <omp.h>
#include <unistd.h>
#include <sys/time.h>
#include <stdio.h>
#include <stdlib.h>
#define N (114748360)
#define threshold (0.0000001)

// int comparevec(int n, float wref[], float w[]);
// int compare(int n, float wref[][n], float w[][n]);

float c[N],b[N],a[N];//,cc[N][N];
// float vec[N], vecc[N];



int main(int argc, char *argv[]){
 int nThreads;
 double rtclock(void);
 double clkbegin, clkend;
 double t1,t2;
 double t1o,t2o;
 
 int i,j,k;
 
 if ( argc != 2 ) {
   printf("Number of threads not specified\n");
   exit(-1);
 }
 nThreads = atoi(argv[1]);
 if ( nThreads <= 0 ) {
   printf("Num threads <= 0\n");
   exit(-1);
 }

 

 /* Initialize a & b */
 
   for(int i=0; i< N; i++ ) {
        a[i] = 1;b[i]=2;c[i]=0;
    }
        
  /*serial code */
  t1 = rtclock();

    for(int i=0; i< N; i++ ) {
            if(i%2 == 0)
                a[i] = b[i] - c[i];
            else
                a[i] = b[i] + c[i];
        }

  
  t2 = rtclock();
  printf("Base version: %.2f GFLOPs; Time = %.2f\n",2.0e-9*N*N*N/(t2-t1),t2-t1);



  /* RESET */
//   for(i=0;i<N;i++)
//     for(j=0;j<N;j++)
//       cc[j][i] = c[j][i];
  
//   for(i=0;i<N;i++) for(j=0;j<N;j++) c[j][i] = 0;



  printf("Num threads = %d\n", nThreads );
  omp_set_num_threads(nThreads);
  printf("Matrix Size = %d\n",N);
  t1o = rtclock();
 
  /*   EDIT BELOW  THIS LINE   */

int chunk = N/nThreads;
omp_set_nested(1);
#pragma omp parallel shared(a,b,c,chunk)
    {
        #pragma omp parallel for schedule(dynamic,chunk)
        for(int i=0; i< N; i++ ) {
            if(i%2 == 0)
                a[i] = b[i] - c[i];
            else
                a[i] = b[i] + c[i];
        }
    }



  /*   EDIT ABOVE  THIS LINE   */
  
  t2o = rtclock();
  printf("Optimized/parallelized version: %.2f GFLOPs; Time = %.2f\n",2.0e-9*N*N*N/(t2o-t1o),t2o-t1o);
  int retVal = 0;//compare(N,c,cc) || comparevec(N,vec,vecc);
  if(retVal == 0) {
    printf("Optimization Speedup = %.2f \n", (((t2-t1)-(t2o-t1o))/(t2-t1))*100);
  }
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


// int comparevec(int n, float wref[], float w[]) {
//   float maxdiff,this_diff;
//   int numdiffs;
//   int i;
//   numdiffs = 0;
//   maxdiff = 0;
//   for (i=0;i<n;i++) {
//     this_diff = wref[i] - w[i];
//     if (this_diff < 0) this_diff = -1.0*this_diff;
//     if (this_diff>threshold) {
//       numdiffs++;
//       if (this_diff > maxdiff) maxdiff=this_diff;
//     }
//   }
//   if (numdiffs > 0) {
//     printf("%d Diffs found over threshold %f; Max Diff = %f\n",
// 	   numdiffs,threshold,maxdiff);
//     return 1;
//   } else {
//     printf("Passed Correctness Check\n");
//     return 0;
//   }
// } 

// int compare(int n, float wref[][n], float w[][n])
// {
//   float maxdiff,this_diff;
//   int numdiffs;
//   int i,j;
//   numdiffs = 0;
//   maxdiff = 0;
//   for (i=0;i<n;i++)
//    for (j=0;j<n;j++) {
//      this_diff = wref[i][j]-w[i][j];
//      if (this_diff < 0) this_diff = -1.0*this_diff;
//      if (this_diff>threshold) { numdiffs++;
//        if (this_diff > maxdiff) maxdiff=this_diff;
//      }
//    }
//   if (numdiffs > 0) {
//     printf("%d Diffs found over threshold %f; Max Diff = %f\n",
// 	   numdiffs,threshold,maxdiff);
//     return 1;
//   } else {
//     printf("Passed Correctness Check\n");
//     return 0;
//   }
// }
