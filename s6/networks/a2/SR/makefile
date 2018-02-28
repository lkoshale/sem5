all: recieverSR senderSR

senderSR: senderSR.cpp
	g++ -pthread -o senderSR senderSR.cpp 

recieverSR: recieverSR.cpp
	g++ -o recieverSR recieverSR.cpp

clean:
	rm recieverSR
	rm senderSR

