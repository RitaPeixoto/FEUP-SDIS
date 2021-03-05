import java.io.*;
import java.net.*;
import java.util.Hashtable;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
public class Server implements RemoteInterface{

    private String remoteName;
    private Hashtable<String,String> table;
    public static void main(String[] args) {

        if(args.length != 1){
            System.out.println("Usage: java Server <remote_object_name>");
            return;
        }

        Server server = new Server();

        try{
            server.create(args);
            RemoteInterface stub = (RemoteInterface) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind(server.remoteName, stub);
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            return;
        }


    }

    private void create(String[] args) throws IOException{
        this.remoteName = args[0];
        this.table = new Hashtable<String, String>();
    }

    public String answer(String request){
        String msg = this.process(request);
        System.out.println(msg);
        return msg;
    }

    private String process(String received){
        String msg = this.extract(received);

        int res = -1;
        
        if (msg != null) {
            res = table.size();
            
            msg = res + "\n" + msg;
        } else {
            msg = Integer.toString(res);
        }

        System.out.println(received + " :: " + msg);

        return msg;
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
