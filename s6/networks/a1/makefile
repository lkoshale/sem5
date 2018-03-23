all: emailclient emailserver

emailclient: client.c
	gcc -o emailclient client.c 

emailserver: server.c cprocessor.c
	gcc -o emailserver server.c cprocessor.c

clean:
	rm emailserver
	rm emailclient