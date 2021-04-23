import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

public class SSLClient {
    public static void main(String[] args) throws IOException {

        if(args.length < 4) {
            System.out.println("java SSLClient <host> <port> <oper> <opnd>* <cypher-suite>*");
            return;
        }

        String res = create(args);

        int port = Integer.parseInt(args[1]);
        String host =  args[0];

        SSLSocket clientSocket;
        SSLSocketFactory socketFactory;
        System.setProperty("javax.net.ssl.keyStore", "clientKeyStore");
        System.setProperty("javax.net.ssl.keyStorePassword","123456");
        System.setProperty("javax.net.ssl.trustStore","truststore");
        System.setProperty("javax.net.ssl.trustStoreType","JKS");
        System.setProperty("javax.net.ssl.trustStorePassword","123456");


        socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        clientSocket = (SSLSocket) socketFactory.createSocket(host, port);
        clientSocket.setNeedClientAuth(true);

        if(args.length <= 4){
            String[] cyphers = Arrays.copyOfRange(args, res.length()+1, args.length);
            clientSocket.setEnabledCipherSuites(cyphers);
        }

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out.println(res);

        System.out.println("\nSent: "+ res);


        String response = in.readLine();
        System.out.println("Received: "+ response + "\n");

        out.close();
        in.close();
        clientSocket.close();

    }

    private static String create(String[] args){
        String request ="";
        if(args[2].equals("register")) {
            String dns_name = args[3];
            String ip_address = args[4];
            request = "REGISTER " + dns_name + " " + ip_address;
        }
        else if(args[2].equals("lookup")) {
            String dns_name = args[3];
            request = "LOOKUP " + dns_name;
        }
        else System.out.println("Invalid Operation");

        return request;
    }
}
