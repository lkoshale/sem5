#include <stdio.h>
#include <omp.h>
#include <stdlib.h>

#define size 10

int a[size][size];
int b[size][size];

int c[size][size];

int main(){
    omp_set_num_threads(4);
    for(int i=0;i<size;i++){
        for(int j=0;j<size;j++){
            a[i][j] = 2;
            b[i][j] = 1;
            // c[i][j] =-1;
        }
    }

    int chunk = 3;
    omp_set_nested(1);
    // #pragma omp parallel shared(a,b,c,chunk)
    {

        // #pragma omp for schedule(static,chunk) nowait
        for(int i=0;i<size;i++){
            //  #pragma omp parallel shared(a,b,c,chunk)
             {
            // #pragma omp for schedule(static,chunk) nowait
            for(int j=0;j<size;j++){
                if(j>=1 && i>=1)
                    c[i][j] = a[i][j]+c[i-1][j-1];  
                else
                    c[i][j] = a[i][j];  
            }
             }
        }

    }

    for(int i=0;i<size;i++){
        for(int j=0;j<size;j++){
            printf("%d ",c[i][j]);
        }
        printf("\n");
    }


    return 0;
}