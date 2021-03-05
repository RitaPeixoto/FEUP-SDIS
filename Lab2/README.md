# Lab2 - UDP Multicast with Java

this assignment is a client-server application to resolve DNS-like names, i.e. to retrieve the IP address corresponding to a DNS name.

Thus, the server must support the following operations:

* register
to bind a DNS name to an IP address. It returns -1, if the name has already been registered, and the number of bindings in the service, otherwise.


* lookup
to retrieve the IP address previously bound to a DNS name. It returns the IP address in the dotted decimal format or the NOT_FOUND string, if the name was not previously registered.


## Server 

The server program shall be invoked as follows:
```
java Server \<port number\>
```
where

\<port number\>
is the port number the server shall use to provide the service


## Client

The client program shall be invoked as follows:
```
java Client \<host\> \<port\> \<oper\> \<opnd\>*
```
where

\<host\>
is the DNS name (or the IP address, in the dotted decimal format) where the server is running

\<port\>
is the port number where the server is providing service

\<oper\>
is the operation to request from the server, either "register" or "lookup"

\<opnd\>*
is the list of operands of that operation


\<DNS name\> \<IP address\> for register

\<DNS name\> for lookup

After submitting a request, the client waits to receive a reply to the request, prints the reply, and then terminates.
