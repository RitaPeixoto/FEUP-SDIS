import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private String hostname;
    private int port_number;
    private String oper; 
    private String request = "";
    private List<String> opnd = new ArrayList<String>();
    public static void main(String[] args) throws IOException{

        if(args.length < 4 || args.length > 5) {
            System.out.println("Usage: java Client <host_name> <port_numbere> <oper> <opnd>*");
            return;
        }

        Client client = new Client();

        Integer res = client.create(args);

        if(res == -1) return;

        Socket clientSocket = new Socket(client.hostname, client.port_number);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out.println(client.request);
        System.out.println("\nSent: "+ client.request);
        

        String response = in.readLine();
        System.out.println("Received: "+ response + "\n");

        out.close();
        in.close();
        clientSocket.close();

    }

    private Integer create(String[] args) throws IOException{
        this.hostname = args[0];
        this.port_number = Integer.parseInt(args[1]);
        
        this.oper = args[2];

        if(this.oper.equals("register")) {
            String dns_name = args[3];
            String ip_address = args[4];
            this.opnd.add(dns_name);
            this.opnd.add(ip_address);
            this.request = "REGISTER " + dns_name + " " + ip_address;
        }
        else if(this.oper.equals("lookup")) {
            String dns_name = args[3];
            this.opnd.add(dns_name);
            this.request = "LOOKUP " + dns_name;
        }
        else {
            System.out.println("Invalid Operation");
            return -1;
        }

        return 0;
    }
}
