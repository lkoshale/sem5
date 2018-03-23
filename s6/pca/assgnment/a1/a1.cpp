
#include<iostream>
#include<vector>
#include<string>
#include<cstdlib>
#include<algorithm>
#include<stdio.h>

using namespace std;

// define known constants as macros
#define LRU 0
#define FIFO 1
#define SRRIP 2
#define PSUEDO_LRU 3
#define WRITE_THROUGH 0
#define WRITE_BACK 1
#define INCLUSIVE 0
#define EXCLUSIVE 1


////////////////////////////

vector<int> HextoBinary( string hex){
  string ans="";
  
}


////////////////////////////////////
class BLOCK{
public:
  int tagSize;
  bool valid;
  vector<int> tag;
  string data;
  //constructor
  BLOCK(){
    this->valid = false;

  }

};

class SET {
  public:
    //set size
    int asocitivity ;
    vector<BLOCK*>sets;
    SET(int as){
      this->asocitivity = as;
    }
};

class Cache {

public:
  long blockSize;
  long size;
  string label;
  int level;

  int asocitivity;
  int replacementPolicy;
  int writePolicy;

  // indexing
  int indexSize;
  vector<SET*> cache ;

  //inclusive cache
  vector< Cache* > included;

  Cache(int blockSize,string label,int level,int size,int asc,int rpl,int w){
    this->blockSize = blockSize;
    this->label = label;
    this->size = size;
    this->level = level;
    this->asocitivity = asc;
    this->replacementPolicy = rpl;
    this->writePolicy = w;

    //set indexSize
    this->indexSize = this->size/(this->asocitivity * this->blockSize);
    createCache();
  }

  //initailize all vectors
  void createCache();
  //lookup method
  int cacheLookup( string address );

};

//initailizse all the blocks with valid bit 0
void Cache:: createCache(){
  for(int i=0;i<this->indexSize;i++){
    SET* s = new SET(this->asocitivity);
    for(int j=0;j<this->asocitivity;j++){
      BLOCK* b = new BLOCK();
      s->sets.push_back(b);
    }
    this->cache.push_back(s);
  }

}


int Cache:: cacheLookup(string addrs){
    vector<int> bitAdrs = toBinary( aadrs);
}


/* Object oriented way */
/* modularizing for one or more cores */
class Core{

public:
  int coreId;
  vector<Cache>CacheHierachy;
  Core(int id){this->coreId = id;}
  void addNextLevelCache(Cache ch){ this->CacheHierachy.push_back(ch);}
};



int main(int argc, char const *argv[]) {

  FILE* config ;
  FILE* trace;
  FILE* output;

  if(argc >2){
    cout<<argv[0]<<" "<<argv[1]<<" "<<argv[2]<<" "<<argc<<"\n";
    config = fopen(argv[1],"r");
    trace = fopen(argv[2],"r");
  }

  // config = fopen("config.txt","r");
  // trace = fopen("memtrace.dat","r");
  //raed input from files

  //for test
  Core c1(1);
  Cache L1(64,"L1",0,16384,4,2,1);



  return 0;

}
