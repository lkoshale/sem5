all: recieverSR senderSR

senderSR: senderSR.cpp
	g++ -g -pthread -o senderSR senderSR.cpp 

recieverSR: recieverSR.cpp
	g++ -g -pthread -o recieverSR recieverSR.cpp

clean:
	rm recieverSR
	rm senderSR

