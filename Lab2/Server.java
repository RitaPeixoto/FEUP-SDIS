import java.io.*;
import java.net.*;
import java.util.Hashtable;
import java.util.concurrent.*;

public class Server {

    private int multicastPort;
    private int serverPort;
    private InetAddress multicastAddress;
    private InetAddress serverAddress;

    private DatagramSocket serverSocket;
    private MulticastSocket multicastSocket;
    private Hashtable<String,String> table;
    public static void main(String[] args) throws IOException{

        if(args.length != 3){
            System.out.println("Usage: java Server <srvc_port> <mcast_addr> <mcast_port>");
            return;
        }

        Server server = new Server();
        
        try{
            server.create(args);
        }
        catch (IOException e){
            System.err.println(e.getMessage());
            return;
        }

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        

        scheduler.scheduleAtFixedRate(new Runnable(){
            @Override
            public void run(){
                    String msg = "localhost" + " " + server.serverPort;
                    byte[] buf = msg.getBytes();
                    DatagramPacket newPacket = new DatagramPacket(buf, buf.length, server.multicastAddress,server.multicastPort);
                    try{
                        server.multicastSocket.send(newPacket);
                        System.out.println("multicast: " + server.multicastAddress + " " + server.multicastPort + ":" + server.serverAddress + " " + server.serverPort);
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
            } 
        }, 0, 1, TimeUnit.SECONDS);

        while(true){
            server.run();
        }


    }

    private void create(String[] args) throws IOException{
        this.serverPort = Integer.parseInt(args[0]);
        this.multicastAddress = InetAddress.getByName(args[1]);
        this.multicastPort = Integer.parseInt(args[2]);
        this.serverAddress = InetAddress.getByName("localhost");
        
        
        this.multicastSocket = new MulticastSocket(this.multicastPort);
        this.serverSocket = new DatagramSocket(this.serverPort);

        this.table = new Hashtable<String, String>();

        
    }

    private void run() throws IOException{
        System.out.println("Adeus");
        byte[] buffer = new byte[512];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        serverSocket.receive(packet);
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

        this.serverSocket.send(newPacket);


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
