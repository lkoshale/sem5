#include <omp.h>
#include <unistd.h>
#include <sys/time.h>
#include <stdio.h>
#include <stdlib.h>
#define N (1024)
#define threshold (0.0000001)

int comparevec(int n, float wref[], float w[]);
int compare(int n, float wref[][n], float w[][n]);

float c[N][N],b[N][N],a[N][N],cc[N][N];
float vec[N], vecc[N];



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
 
 for(i=0;i<N;i++)
   for(j=0;j<N;j++)
     {  a[i][j] = 1.1*(2*i+j);
       b[i][j] = 1.2*(i+2*j);
     }
 
 for(i=0;i<N;i++) {
   vecc[i] = 0;
   vec[i] = 0;
   for(j=0;j<N;j++) c[j][i] = 0;
 }
  /*serial code */
  t1 = rtclock();


  for(i=0;i<N;i++)
    for(j=0;j<N;j++)
      for(k=0;k<N;k++) 
	      c[j][i] =  c[j][i] + a[k][i]*b[k][j];

  for(i=0;i<N;i++)
    for(j=0;j<N;j++)
      vecc[j] += c[i][j] - a[i][j] - b[i][j];

  
  t2 = rtclock();
  printf("Base version: %.2f GFLOPs; Time = %.2f\n",2.0e-9*N*N*N/(t2-t1),t2-t1);



  /* RESET */
  for(i=0;i<N;i++)
    for(j=0;j<N;j++)
      cc[j][i] = c[j][i];
  
  for(i=0;i<N;i++) for(j=0;j<N;j++) c[j][i] = 0;



  printf("Num threads = %d\n", nThreads );
  omp_set_num_threads(nThreads);
  printf("Matrix Size = %d\n",N);
  t1o = rtclock();
 
  /*   EDIT BELOW  THIS LINE   */

int chunk = N/nThreads;
int trd = nThreads/2;
omp_set_nested(1);
#pragma omp parallel shared(a,b,c,chunk) 
{
	int j;
    #pragma omp for schedule(dynamic,chunk) nowait 
   for( j=0;j<N;j++)
    {
        #pragma omp parallel shared(a,b,c,chunk) 
        {
        int i,k;
        #pragma omp for schedule(static,chunk) nowait
         for( i=0;i<N;i++)
          for( k=0;k<N;k++)
            c[j][i] = c[j][i] + a[k][i]*b[k][j];
        }
    }
}


    #pragma omp parallel shared(a,b,c,chunk ) 
    {
   	int i,j;
     #pragma omp for schedule(static,chunk) nowait
      for( j=0;j<N;j++)
     {
      for( i=0;i<N;i++)
        vec[j] += c[i][j] - a[i][j] - b[i][j];
     }
    }



  /*   EDIT ABOVE  THIS LINE   */
  
  t2o = rtclock();
  printf("Optimized/parallelized version: %.2f GFLOPs; Time = %.2f\n",2.0e-9*N*N*N/(t2o-t1o),t2o-t1o);
  int retVal = compare(N,c,cc) || comparevec(N,vec,vecc);
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


int comparevec(int n, float wref[], float w[]) {
  float maxdiff,this_diff;
  int numdiffs;
  int i;
  numdiffs = 0;
  maxdiff = 0;
  for (i=0;i<n;i++) {
    this_diff = wref[i] - w[i];
    if (this_diff < 0) this_diff = -1.0*this_diff;
    if (this_diff>threshold) {
      numdiffs++;
      if (this_diff > maxdiff) maxdiff=this_diff;
    }
  }
  if (numdiffs > 0) {
    printf("%d Diffs found over threshold %f; Max Diff = %f\n",
	   numdiffs,threshold,maxdiff);
    return 1;
  } else {
    printf("Passed Correctness Check\n");
    return 0;
  }
} 

int compare(int n, float wref[][n], float w[][n])
{
  float maxdiff,this_diff;
  int numdiffs;
  int i,j;
  numdiffs = 0;
  maxdiff = 0;
  for (i=0;i<n;i++)
   for (j=0;j<n;j++) {
     this_diff = wref[i][j]-w[i][j];
     if (this_diff < 0) this_diff = -1.0*this_diff;
     if (this_diff>threshold) { numdiffs++;
       if (this_diff > maxdiff) maxdiff=this_diff;
     }
   }
  if (numdiffs > 0) {
    printf("%d Diffs found over threshold %f; Max Diff = %f\n",
	   numdiffs,threshold,maxdiff);
    return 1;
  } else {
    printf("Passed Correctness Check\n");
    return 0;
  }
}
