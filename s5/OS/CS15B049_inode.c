#include <sys/types.h>
#include <sys/stat.h>
#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/sysmacros.h>
#include <sys/dir.h>
#include <dirent.h>
#include <string.h>


void getDir(char* dir)
{
	if( strcmp(dir,".")==0 || strcmp(dir,"..") ==0 )
		return;

	char name[200];
       struct dirent *dp;
       DIR *dfd;

       if ((dfd = opendir(dir)) == NULL) {
           fprintf(stderr, "dirwalk: can't open %s\n", dir);
           return;
       }
       
       while ((dp = readdir(dfd)) != NULL) {
       
       	if (strcmp(dp->d_name, ".") == 0 || strcmp(dp->d_name, "..")==0)
               continue;
       		
       		
       		char* n = (char*)malloc(sizeof(char)*250);
       		strcpy(n ,dir);
       		strcat(n,"/");
       		strcat(n,dp->d_name);
       		//printf("%s",n);
       		
       		struct stat sb;
       		
       		if (stat(n, &sb) == -1) {
		       perror("stat");
		       exit(EXIT_FAILURE);
		}
		
       	//	printf("I-node number: %ld\n ", (long) sb.st_ino);
       		
       		if( (sb.st_mode & S_IFMT )== S_IFDIR ){
	       		printf("\ninside %s there is folder %s  " , dir, dp->d_name);
	       		printf(" with inode = %ld\n", (long)dp->d_ino );
			getDir(n);
		}
		else {
		
			printf("\ninside %s there is file %s  " , dir, dp->d_name);
	       		printf(" with inode = %ld\n", (long)dp->d_ino );
		
		}
       		
       }
}

int main(int argc, char *argv[])
{
   struct stat sb;

   if (argc != 2) {
       fprintf(stderr, "Usage: %s <pathname>\n", argv[0]);
       exit(EXIT_FAILURE);
   }

   if (stat(argv[1], &sb) == -1) {
       perror("stat");
       exit(EXIT_FAILURE);
   }

	
   printf( "\n ****************************************\n");
   printf( " printing data stored in inode of %s \n",argv[1]);
   printf( " ****************************************\n");
	
   printf("File type:                ");

   switch (sb.st_mode & S_IFMT) {
   case S_IFBLK:  printf("block device\n");            break;
   case S_IFCHR:  printf("character device\n");        break;
   case S_IFDIR:  printf("directory\n");               break;
   case S_IFIFO:  printf("FIFO/pipe\n");               break;
   case S_IFLNK:  printf("symlink\n");                 break;
   case S_IFREG:  printf("regular file\n");            break;
   case S_IFSOCK: printf("socket\n");                  break;
   default:       printf("unknown?\n");                break;
   }

   printf("I-node number:            %ld\n", (long) sb.st_ino);
   
   printf("Link count:               %ld\n", (long) sb.st_nlink);
  
   printf("Preferred I/O block size: %ld bytes\n",
           (long) sb.st_blksize);
   printf("File size:                %lld bytes\n",
           (long long) sb.st_size);
   printf("Blocks allocated:         %lld\n",
           (long long) sb.st_blocks);

   printf("Last status change:       %s", ctime(&sb.st_ctime));
   printf("Last file access:         %s", ctime(&sb.st_atime));
   printf("Last file modification:   %s", ctime(&sb.st_mtime));

   
	
   if( (sb.st_mode & S_IFMT )== S_IFDIR ){
   printf( "\n ****************************************\n");
   printf( " exploring the files inside %s \n",argv[1]);
   printf( " ****************************************\n");
	getDir(argv[1]);
   }

   exit(EXIT_SUCCESS);
}
