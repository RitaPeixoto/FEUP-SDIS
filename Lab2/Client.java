import java.io.*;
import java.net.*;

public class Client {

    private int multicastPort;
    private MulticastSocket socket;
    private String oper;
    private InetAddress multicastAddress;
    private InetAddress datagramAddress;
    private int datagramPort;
    private String dns_name;
    private String ip_address;   
    private String request = "";


    public static void main(String[] args) throws IOException{

        if(args.length < 3 || args.length > 4) {
            System.out.println("Usage: java Client <mcast_addr> <mcast_port> <oper> <opnd> ");
            return;
        }

        Client client = new Client();

        Integer res = client.create(args);

        if(res == -1) return;

        try{
            client.run();
        }
        catch (SocketException e){
            System.out.println(e.getMessage());
        }

    }
    
    private Integer create(String[] args) throws IOException{
        this.multicastPort = Integer.parseInt(args[1]);
        this.socket = new MulticastSocket(this.multicastPort);
        this.multicastAddress = InetAddress.getByName(args[0]); 
      
        this.socket.joinGroup(this.multicastAddress);

        byte[] buffer = new byte[512];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        this.socket.receive(packet);

        String[] res = new String(packet.getData()).split(" "); 
        System.out.println(res[1]);
        System.out.println(res[0].trim());
        this.datagramAddress = InetAddress.getByName(res[0].trim());
        this.datagramPort = Integer.parseInt(res[1].trim());
        System.out.println("multicast: " + this.multicastAddress + " " + this.multicastPort + " : " + this.datagramAddress + " " + this.datagramPort);


        this.oper = args[2];

        if(this.oper.equals("register")) {
            this.dns_name = args[3];
            this.ip_address = args[4];
            this.request = "REGISTER " + this.dns_name + " " + this.ip_address;
        }
        else if(this.oper.equals("lookup")) {
            this.dns_name = args[3];
            this.request = "LOOKUP " + this.dns_name;
        }
        else {
            System.out.println("Invalid Operation");
            return -1;
        }

        return 0;
    }

    private void run() throws IOException {
        byte[] buffer = new byte[512];
        buffer = this.request.getBytes();
        DatagramPacket requestDatagram = new DatagramPacket(buffer, buffer.length, this.datagramAddress, this.datagramPort);
        this.socket.send(requestDatagram);

        DatagramPacket responseDatagram = new DatagramPacket(buffer, buffer.length);
        this.socket.receive(responseDatagram);
        String response = new String(responseDatagram.getData(), 0, responseDatagram.getLength());

        System.out.println(this.request + "*::" + response);
        this.socket.close();
    }

    
}
