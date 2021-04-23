# Lab5 - Programming with SSL Sockets(JSSE)

The main goal of this lab is to reinforce the understanding of the fundamentals for the development of distributed applications using secure channels, namely SSL.

This specification assumes knowledge of the terminology related to SSL and JSSE .

## Server

The server program shall be invoked as follows:
```
java SSLServer <port> <cypher-suite>*         
```
where:

**\<port\>** is the port number where the server provides the service


**\<cypher-suite\>** is a sequence, possibly empty, of strings specifying the combination of cryptographic algorithms the server should use, in order of preference. If no cypher suite is specified, the server shall use any of the cypher-suites negotiated by default by the SSL provider of JSE.

The server program should have an infinite loop in which the server waits for a request from a client, processes the request and sends the respective response to the client.


## Client

The client program should be invoked as follows:
```
java SSLClient <host> <port> <oper> <opnd>* <cypher-suite>*
```

where:

**\<host\>** is the DNS name (or the IP address, in the dotted decimal format) where the server is running;

**\<port\>** is the port number where the server is providing service;

**\<oper\>** is the operation to request from the server;

**\<opnd\>** is the list of operands of that operation;

**\<cypher-suite\>** is a sequence, possibly empty, of strings specifying the combination of cryptographic algorithms the client should use, in order of preference. If no cypher suite is specified, the client shall use any of the cypher-suites negotiated by default by the SSL provider of JSE.
