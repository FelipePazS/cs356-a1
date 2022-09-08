import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class Server {
    int port;
    // pass any variables to the constructor
    public Server(int port) {  
        this.port = port;
    }

    // run your server code
    public void run() throws IOException {
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
            for (int i = 0; i < 1000; i ++){
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
