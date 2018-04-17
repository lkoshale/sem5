#!/bin/bash

## defining constants 
numThread=$2

# #### resolve permision error #######
# delte old file and rebuild the parsec program
source env.sh
# cd pkgs/apps/$1/
# sudo rm -rf run
# cd ..
# cd ..
# cd ..

# # rebuild the program
# source env.sh
# parsecmgmt -a uninstall -p $1
# parsecmgmt -a build -p $1
#####-------######
it=1

# run program in different terminal
gnome-terminal -x bash -c  "parsecmgmt -a run -p $1 -i native -n $numThread"
sleep $3

# get pid of the process
rm p.txt
ps aux | grep $1 | awk '{print $2}' > p.txt 
total=$(wc -l < p.txt)
((total--))
line=$( sed "${total}q;d" p.txt)
echo $line
sleep 5
# get the Threads related to the process
sudo rm th.txt
ps -T -p $line | awk '{print $2}' &> th.txt


# store thread id in an array
sed -n '3,$p' < th.txt >th1.txt
cnt=1
while read p; do
	TH[$cnt]=$p
	((cnt++))
done < th1.txt 

#print thread ids
for((i=1;i<$cnt;i++))
do
	#tid=$(python getTh.py $i)
	#tid=$(sed -n "$ip" th.txt)
	#echo $tid
	#TH[$i]=$tid
	echo ${TH[$i]}
	
done

rm th1.txt


#####################


TOTALOCKTIME=0

# if process is running 
while ps -p $line > /dev/null; 
do
		# start perf to analysis each thread
		for((i=1;i<$cnt;i++))
		do
			cd $i
			echo ${TH[$i]}
			sudo ./perf lock record -t ${TH[$i]} &
			cd ..
			AR[$i]=$!
		done

		# stop analysis after 
		sleep 1
		if ps -p $line > /dev/null; then
			
			
			# get the perf report parse the report and get the lock times
			for((i=1;i<$cnt;i++))
			do
				cd $i
				gnome-terminal -x bash -c  "sudo kill -INT ${AR[$i]}"
				cd ..	
			done	
			sleep 0.1
			for((i=1;i<$cnt;i++))
			do
				#sleep 0.5
				cd $i
				sudo ./perf lock report &> rep.txt 
				python a.py rep.txt > sum.txt
				Twait=$(head -n 1 sum.txt )
				TOTALOCKTIME=$((TOTALOCKTIME+Twait))
				cd ..
				lock=$(python per.py $Twait 1000000000)
				echo "${TH[$i]} $lock" >>LOCK.txt
				LOCK[$i]=$lock
				echo "back"
			done

		fi

		# sort in ascending order
		sort -k 2,2 LOCK.txt > sort.txt


		cat sort.txt | awk '{print $1}' > sortTh.txt
		cat sort.txt | awk '{print $2}' > sortL.txt

		echo "thread-ID  --  LockTime"

		for((i=1;i<$cnt;i++))
		do
			SortT[$i]=$( sed "${i}q;d" sortTh.txt)
			SortLK[$i]=$( sed "${i}q;d" sortL.txt)
			echo "${SortT[$i]}  -- ${SortLK[$i]}"
		done


	

		#start scheduling by set affinity

		 cpuCore=$(nproc)
		 group=$(( ($cnt-1)/$cpuCore))
		 rem=$(((cnt-1)%cpuCore ))
		# echo " num $cnt $group $cpuCore"
		echo "****-----scheduling------******"
		 index=1
		for((i=0;i<cpuCore;i++))
		do
		 	for((j=0;j<group;j++))
		 	do
		 		#if [[ $index < $cnt ]]; then
		 		#	taskset -cp $i ${SortT[$index]} 
		 			((index++))
		 		#fi
		 		
		 	done

		done

		# schedule reminder 
		for (( i = 0; i < rem; i++ )); do
		#	taskset -cp $i ${SortT[$index]}
			((index++))
		done


	    rm LOCK.txt
	    	
		echo " "
		echo $it
		echo "***-----------one iteration---------------****"
		echo "  "
		it=$it+1
done


echo $TOTALOCKTIME

 # j=1
 # for((i=0;i<C;i++))
 # do
 # 	taskset -cp $i ${SortT[$j]} 
 # 	((j++))
 # 	taskset -cp $i ${SortT[$j]} 
 # 	((j++))
 # 	taskset -cp $i ${SortT[$j]} 
 # 	((j++))
 # done



