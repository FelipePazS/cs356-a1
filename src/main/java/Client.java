import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

public class Client {
    String host;
    int port;
    int time;
    // pass constructor values as required
    public Client(String host, int port, int time) {
        this.host = host;
        this.port = port;
        this.time = time;
    }

    // run your code
    public void run()  throws IOException {
        System.out.println("Connecting to " + host + "@ port " + port);

        Socket clientSocket = new Socket(host, port);
        System.out.println("Connection established.\n");
 
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        PrintWriter writer = new PrintWriter(out, true);

        int total_bytes_sent = 0;
        int start_time = ((int) System.currentTimeMillis() / 1000);
        while (((int) System.currentTimeMillis() / 1000) < (start_time + time) ){
            for (int i = 0; i < 250; i ++){
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
    }
}