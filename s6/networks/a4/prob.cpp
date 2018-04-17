
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>
#include <limits.h>
#include <time.h>
#include <iostream>
#include <string>
#include <vector>
#include <random>


using namespace std;



int main(int argc,char* argv[]){
 
    double p;
    cin>>p;
// main process is sender and a new thread is created for reciever
    std::default_random_engine generator;
    std::uniform_int_distribution<int> distribution(0,100000);
    int heads=0, tails=0;
    srand(time(NULL));

    int l = 0,r=0;
    for(int i=0;i<10000;i++){
        int number = rand() % 100000 + 1;
        int num = distribution(generator);
        // cout<<num<<" - "<<number<<"\n";
        if(num>p*100000)
            l++;
        else
            r++;

        if(number>=p*100000)
            heads++;
        else
            tails++;
    }
    cout<<l<<" "<<r<<"\n";
    cout<<heads<<" "<<tails<<"\n";


}
