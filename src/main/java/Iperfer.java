import org.apache.commons.cli.*;
import java.io.IOException;

public class Iperfer {
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
            Client client = new Client(host, port, time);
            client.run();
        } else if (cmd.hasOption("s")){
            Server server = new Server(port);
            server.run();
        }
    }
}