 #include <stdio.h>
 #include <omp.h>
 #include <unistd.h>
 #include <sys/time.h>
 #define N 10000
 #define CHUNKSIZE 100

double rtclock(void)
{
  struct timezone Tzp;
  struct timeval Tp;
  int stat;
  stat = gettimeofday (&Tp, &Tzp);
  if (stat != 0) printf("Error return from gettimeofday: %d",stat);
  return(Tp.tv_sec + Tp.tv_usec*1.0e-6);
}

 int main(int argc, char *argv[]) {

 int i, chunk;
 float a[N], b[N], c[N];
double t1,t2;
 /* Some initializations */
 for (i=0; i < N; i++)
   a[i] = b[i] = i * 1.0;
 chunk = CHUNKSIZE;
  
 omp_set_num_threads(4);
  t1 = rtclock();
 #pragma omp parallel shared(a,b,c,chunk) private(i)
   {

   #pragma omp for schedule(dynamic,chunk) nowait
   for (i=0; i < N; i++)
     c[i] = a[i] + b[i];

   }   /* end of parallel region */

    t2= rtclock();

    // for (i=0; i < N; i++)
    //     printf("%d ",(int)c[i]);

  printf("%lf\n",t2-t1);
    return 0;
 }