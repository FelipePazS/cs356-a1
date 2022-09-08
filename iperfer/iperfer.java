import org.apache.commons.cli.*;
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

        // process your command line args
        // if you want to use the apache commons library
        Options options = new Options();

        Option c = Option.builder("c")
        .argName("client_mode")
        .desc("runs iperfer in client mode").build();
        options.addOption(c);

        Option s = Option.builder("s").longOpt("server")
        .argName("server mode")
        .desc("runs iperfer in server mode").build();
        options.addOption(s);

        Option h = Option.builder("h")
        .argName("host")
        .hasArg()
        .desc("hostname or IP address of the iperf server which will consume data").build();
        options.addOption(h);

        Option p = Option.builder("p")
        .argName("port")
        .hasArg()
        .desc(" the port on which the host is waiting to consume data").build();
        options.addOption(p);

        Option t = Option.builder("t")
        .argName("time")
        .hasArg()
        .desc(" duration in seconds for which data should be generated").build();
        options.addOption(t);


        // Parse comand line arguments and validate values when needed
        CommandLine cmd = null;
        CommandLineParser parser = new DefaultParser();
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(error_message);
            System.exit(0);
        }

        String host = cmd.getOptionValue("h");

        String port_string = cmd.getOptionValue("p");
        int port = Integer.parseInt(port_string);
        if (port < 1024 || port > 65535){
            System.out.println("Error: port number must be in the range 1024 to 65535");
            System.exit(0);
        }

        String time_string = cmd.getOptionValue("t");
        int time = 0;
        if (time_string != null){
            time = Integer.parseInt(time_string);
        }
        if (time > 600){
            System.out.println("Error: time must not exceed 10 minutes");
            System.exit(0);
        }


        if (cmd.hasOption("c")){
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
        } else if (cmd.hasOption("s")){
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
}
