all: recieverSR senderSR recieverGBN senderGBN

senderSR: senderSR.cpp
	g++ -g -pthread -o senderSR senderSR.cpp 

recieverSR: recieverSR.cpp
	g++ -g -pthread -o recieverSR recieverSR.cpp

senderGBN: senderGBN.cpp
	g++ -g -pthread -o senderGBN senderGBN.cpp 

recieverGBN: recieverGBN.cpp
	g++ -g -o recieverGBN recieverGBN.cpp

clean:
	rm recieverGBN
	rm senderGBN
	rm recieverSR
	rm senderSR
