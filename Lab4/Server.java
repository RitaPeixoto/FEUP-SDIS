import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private int srvc_port;
    private Hashtable<String,String> table;
    public static void main(String[] args) {

        if(args.length != 1){
            System.out.println("Usage: java Server <srvc_port>");
            return;
        }
        
        Server server = new Server();

        try{
            server.create(args);
            
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            return;
        }
        
        ServerSocket serverSocket = null;
        
        try{
            serverSocket = new ServerSocket(server.srvc_port);
        }catch(IOException e){
            System.out.println("Error opening port: " + server.srvc_port);
            System.exit(-1);
        }


        while(true){
            Socket socket = null;
            try{
                socket = serverSocket.accept();
            }
            catch(IOException e){
                System.err.println("Accept failed: " + server.srvc_port);
				System.exit(1);
            }
            
            PrintWriter out = null;
            BufferedReader in = null;   
            
            try{
                out = new PrintWriter(socket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            }catch(IOException e){
                System.out.println("Error in PrintWriter or BufferedReader");
                return;
            }

            System.out.println("Srvr: waiting for client request");
            String str = "";

            try{
                 str = in.readLine();
            }
            catch(IOException e){
                System.out.println("Error reading lines");
                return;
            }
            
            System.out.println("\nRequest: " + str);
    
            String oper = (str.split(" "))[0];
            String res = "";
            String dns_name = "";
            String ip_adress = "";
            String print = " ";
    
            if(oper.equals("LOOKUP")){
                dns_name = (str.split(" "))[1];
                if(server.table.containsKey(dns_name)){
                    res = server.table.get(dns_name);
                }
                else{
                    res ="Not Found!";
                }
                print = oper + " " + dns_name + "::" + res;       
            }
            else if(oper.equals("REGISTER")){
                dns_name = (str.split(" "))[1];
                ip_adress = (str.split(" "))[2];
                if(server.table.containsKey(dns_name)){
                    res = "Already exists!";
                }
                else{
                    server.table.put(dns_name, ip_adress);
                    res = Integer.toString(server.table.size());
                }
                print = oper + " " + dns_name + " " + ip_adress + "::" + res;
            }
            else{
                res = "Invalid operation!";
                print = oper + " :: " + res;
            }
            
            out.println(res);
            out.flush();
    
            System.out.println("Sent: " + print);
            try{
                out.close();
			    in.close();
                socket.close();
            }
            catch(IOException e){
                System.out.println("Error closing");
                return;
            }
			
        }
			
        
       /* try{
            serverSocket.close();
        }catch(IOException e){
            System.out.println("Error closing socket");
            return;
        }
        */
    }

    private void create(String[] args) throws IOException{
        this.srvc_port = Integer.parseInt(args[0]);
        this.table = new Hashtable<String, String>();
    }
}
