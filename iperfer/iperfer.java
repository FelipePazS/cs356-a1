import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.*;

public class iperfer {
    static String error_message = "Error: invalid inputs";
    public static void main(String[] args) throws IOException {
        //parse command line arguments
        boolean is_client = false;
        boolean is_server = false;
        int i = 0;
        int port = 0;
        String host = "";
        int time = 0;

        while (i < args.length){
            String arg = args[i];
            if (arg.equals("-c")){
                is_client = true;
                i++;
            } 
            else if (arg.equals("-s")){
                is_server = true;
                i++;
            }
            else if (arg.equals("-p")){
                if (i + 1 < args.length){
                    port = Integer.parseInt(args[i + 1]);
                    i += 2;
                } else {
                    System.out.println(error_message);
                    System.exit(0);
                }
            }
            else if (arg.equals("-h")){
                if (i + 1 < args.length){
                    host = args[i + 1];
                    i += 2;
                } else {
                    System.out.println(error_message);
                    System.exit(0);
                }
            }
            else if (arg.equals("-t")){
                if (i + 1 < args.length){
                    host = args[i + 1];
                    i += 2;
                } else {
                    System.out.println(error_message);
                    System.exit(0);
                }
            }
            else {
                System.out.println(error_message);
                System.exit(0);
            }
        }

        if (is_client){
            System.out.println("Connecting to " + host + "@ port " + port);

            Socket clientSocket = new Socket(host, port);
            System.out.println("Connection established.\n");
    
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            PrintWriter writer = new PrintWriter(out, true);

            int total_bytes_sent = 0;
            int start_time = ((int) System.currentTimeMillis() / 1000);
            while (((int) System.currentTimeMillis() / 1000) < (start_time + time) ){
                for (int x = 0; x < 250; x ++){
                    out.writeInt(0);
                }
                writer.println();
            }
            total_bytes_sent = out.size();
            int end_time = ((int) System.currentTimeMillis() / 1000);
            int elapsed_time = end_time - start_time;
            int KB_sent = total_bytes_sent / 1000;
            float rate = (8 * ((float)KB_sent / 1000)) / elapsed_time;
            System.out.println("sent=" + KB_sent + " KB" + " rate=" + rate + " Mbps");
            clientSocket.close();
        } else if (is_server){
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("Watiting for client connections on port " + port);
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connection established.\n");

            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            int total_bytes_received = 0;
            int character_read = 0;
            int start_time = ((int) System.currentTimeMillis() / 1000);
            while (character_read != -1){
                for (int y = 0; y < 1000; y ++){
                    character_read = reader.read();
                    if (character_read == -1){
                        continue;
                    }
                    total_bytes_received += 1;
                }
            }
            int end_time = ((int) System.currentTimeMillis() / 1000);
            int elapsed_time = end_time - start_time;
            int KB_received = total_bytes_received / 1000;
            float rate = (8 * ((float) KB_received / 1000)) / elapsed_time;
            System.out.println("received=" + KB_received + " KB" + " rate=" + rate + " Mbps");
            
            clientSocket.close();
            serverSocket.close();
        }
    }
}
