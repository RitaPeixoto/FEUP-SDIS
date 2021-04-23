import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Hashtable;

public class SSLServer {
    private static Hashtable<String,String> table;

    public static void main(String[] args) throws IOException {

        if(args.length < 1){
            System.out.println("java SSLServer <port> <cypher-suite>*");
            return;
        }

        int port = Integer.parseInt(args[0]);

        SSLServerSocket serverSocket;
        SSLServerSocketFactory socketFactory;

        System.setProperty("javax.net.ssl.keyStore", "serverKeyStore");
        System.setProperty("javax.net.ssl.keyStorePassword","123456");
        System.setProperty("javax.net.ssl.trustStore","truststore");
        System.setProperty("javax.net.ssl.trustStoreType","JKS");
        System.setProperty("javax.net.ssl.trustStorePassword","123456");

        socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        serverSocket = (SSLServerSocket) socketFactory.createServerSocket(port);
        serverSocket.setNeedClientAuth(true);

        if(args.length > 1){
            String[] cyphers = Arrays.copyOfRange(args,1, args.length);
            serverSocket.setEnabledCipherSuites(cyphers);
        }


        while(true){
            Socket clientSocket = serverSocket.accept();

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String req = "";
            req = in.readLine();
            String res = request(req);

            out.println(res);
            out.flush();


            System.out.println("\nRequest: " + req);
            System.out.println("Sent: " + res);

            out.close();
            in.close();
            clientSocket.close();

        }
    }


    public static String request(String req){
        String oper = (req.split(" "))[0];
        String res = "";
        String dns_name = "";
        String ip_adress = "";

        if(oper.equals("LOOKUP")){
            dns_name = (req.split(" "))[1];
            res = lookup(dns_name);
        }
        else if(oper.equals("REGISTER")){
            dns_name = (req.split(" "))[1];
            ip_adress = (req.split(" "))[2];
            res = "" + register(dns_name, ip_adress);
        }
        else
            System.out.println("Invalid operation!");

        return res;
    }


    private static int register(String name, String ip){
        String value = table.put(name, ip);

        if(value == null) return table.size();
        return -1;
    }


    private static String lookup(String name){
        return table.get(name);
    }
}
