all: recieverGBN senderGBN

senderGBN: senderGBN.cpp
	g++ -pthread -o senderGBN senderGBN.cpp 

recieverGBN: recieverGBN.cpp
	g++ -o recieverGBN recieverGBN.cpp

clean:
	rm recieverGBN
	rm senderGBN

