import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private String request = "";
    private String hostname; 
    private String remote_name;
    private String oper; 
    private List<String> opnd = new ArrayList<String>();


    public static void main(String[] args) throws IOException{

        if(args.length < 3 || args.length > 5) {
            System.out.println("Usage: java Client <host_name> <remote_object_name> <oper> <opnd>*");
            return;
        }

        Client client = new Client();

        Integer res = client.create(args);

        if(res == -1) return;

        client.run();

    }
    
    private Integer create(String[] args) throws IOException{
        this.hostname = args[0];
        this.remote_name = args[1];
        
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

    private void run() throws IOException {
        try {
            Registry registry = LocateRegistry.getRegistry(this.hostname);
            RemoteInterface stub = (RemoteInterface) registry.lookup(this.remote_name);
            String msg = stub.answer(this.request);
            String verify = this.oper;
            for (String opnd : this.opnd) {
                verify = verify + " " + opnd;
            }
            verify += " :: " + msg;
            System.out.println(verify);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }   
}
