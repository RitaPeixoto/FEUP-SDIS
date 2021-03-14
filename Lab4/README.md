# Lab4 - TCP

This lab aims at consolidating the basic concepts required for programming distributed applications that use TCP communication and also gain familiarity with the sockets API in Java for TCP communication.



## Server 

The server program shall be invoked as follows:
```
java Server <srvc_port>             
```
where:

**\<srvc_port\>** is the port number where the server provides the service

The server program should have an infinite loop in which the server waits for a request from a client, processes the request and sends the respective response to the client.


## Client

The client program should be invoked as follows:
```
java Client <host_name> <port_number> <oper> <opnd> *
```

where:

**\<host_name\>** is the name of the host where the server runs;

**\<port_number\>** is the port number of the multicast group used by the server to advertise its service;

**\<oper\>** is ''register'' or ''lookup'', depending on the operation to invoke;

**\<opnd\>** * is the list of operands of the specified operation:

**\<DNS name>\ \<IP address\>**, for register;

**\<IP address\>**, for lookup.
