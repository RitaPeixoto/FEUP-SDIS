import java.io.*;
import java.net.*;

public class Client {

    private String host;
    private int port;
    private String oper;
    private String dns_name;
    private String ip_address;
    private String request = "";
    private DatagramSocket client_socket;
    
    public static void main(String[] args) throws IOException{

        if(args.length < 4 || args.length > 5) {
            System.out.println("Usage: java Client <host> <port> <oper> <opnd>");
            return;
        }

        Client client = new Client();

        Integer res = client.create(args);

        if(res == -1) return;

        client.run();
    }
    
    private Integer create(String[] args) throws IOException{
        this.host = args[0];
        this.port = Integer.parseInt(args[1]);
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
        
        this.client_socket = new DatagramSocket();
        return 0;
    }

    private void run() throws IOException {
        byte[] buffer = new byte[512];
        InetAddress address = InetAddress.getByName(this.host);
        buffer = this.request.getBytes();
        DatagramPacket requestDatagram = new DatagramPacket(buffer, buffer.length, address, this.port);
        this.client_socket.send(requestDatagram);

        DatagramPacket responseDatagram = new DatagramPacket(buffer, buffer.length);
        this.client_socket.receive(responseDatagram);
        String response = new String(responseDatagram.getData(), 0, responseDatagram.getLength());

        System.out.println("Client: " + response);
        this.client_socket.close();
    }

}