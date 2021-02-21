import java.io.*;
import java.net.*;
import java.util.Hashtable;

public class Server {
    
    private int port;
    private DatagramSocket socket;
    private Hashtable<String, String> table;

    public static void main(String[] args) throws IOException {

        if(args.length != 1) {
            System.out.println("Usage: java Server <port number>");
            return;
        }

        Server server = new Server();

        server.create(args);

        while(true){
            server.run();
        }

    }

    private void create(String[] args) throws IOException { 
        this.port = Integer.parseInt(args[0]);
        this.socket = new DatagramSocket(this.port);
        this.table = new Hashtable<String, String>();
    }

    private void run() throws IOException{
        byte[] buffer = new byte[512];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        this.process(packet);

    }


    private void process(DatagramPacket packet) throws IOException{
        String received = new String(packet.getData());
        String msg = this.extract(received);

        int res = -1;
        
        if (msg != null) {
            res = table.size();
            
            msg = res + "\n" + msg;
        } else {
            msg = Integer.toString(res);
        }

        byte[] buf = msg.getBytes();

        int clientPort = packet.getPort();

        InetAddress clientAddress = packet.getAddress();
        
        DatagramPacket newPacket = new DatagramPacket(buf, buf.length, clientAddress, clientPort);

        socket.send(newPacket);


    }

    private String extract(String received) {
        String[] msgArgs = received.split(" ");

        for(int i = 0; i < msgArgs.length; i++){
            msgArgs[i] = msgArgs[i].trim();
        }

        String res;

        if(msgArgs[0].equals("LOOKUP")){
            if(!this.table.containsKey(msgArgs[1])) return null;
            else{
                res = msgArgs[1] + " " + this.table.get(msgArgs[1]);
                return res;
            }  
        }
        else if(msgArgs[0].equals("REGISTER")){
            if(!this.table.contains(msgArgs[1])){
                this.table.put(msgArgs[1], msgArgs[2]);
                res = msgArgs[1] + " " + msgArgs[2];
                return res; 
            }
            else return null;
        }
        else{
            System.out.println("Invalid message received");
            return null;
        }
    }

}