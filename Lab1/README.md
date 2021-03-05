# Lab1 - Programming with Unicast Datagram Sockets

The goal of this lab was to consolidate the main concepts required for programming distributed applications that use UDP multicast communication.


## Server 

The server program shall be invoked as follows:
```
java Server \<srvc_port\> \<mcast_addr\> \<mcast_port\>
where:
```

\<srvc_port\> is the port number where the server provides the service

\<mcast_addr\> is the IP address of the multicast group used by the server to advertise its service

\<mcast_port\> is the multicast group port number used by the server to advertise its service.

## Client

The client program should be invoked as follows:
```
java client \<mcast_addr\> \<mcast_port\> \<oper\> \<opnd\> *
```

where:

\<mcast_addr\> is the IP address of the multicast group used by the server to advertise its service;

\<mcast_port\> is the port number of the multicast group used by the server to advertise its service;

\<oper\> is ''register'' or ''lookup'', depending on the operation to invoke;

\<opnd\> * is the list of operands of the specified operation:

\<DNS name>\ \<IP address\>, for register;

\<IP address\>, for lookup.