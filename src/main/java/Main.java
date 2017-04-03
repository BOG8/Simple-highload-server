import org.apache.commons.cli.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by zac on 28.03.17.
 */

public class Main {
    private static final String SERVER_START = "Server start";
    private static final String SERVER_STOP = "Server stop";
    private static final String DIRECTORY = "Directory";
    private static final String CPU = "CPU";
    private static final int PORT = 8080;
    private static final int QUEUE = 50;
    private static int cpu = 4;

    public static void main(String[] args) {
        final Options option = new Options();
        option.addOption("r", true, DIRECTORY);
        option.addOption("c", true, CPU);
        final CommandLineParser params = new PosixParser();
        try {
            final CommandLine cmd = params.parse(option, args);
            final String parseDir = cmd.getOptionValue("r");
            if (parseDir != null) {
                FileSystem.ROOT_DIR = parseDir;
            }
            final String parseNumber = cmd.getOptionValue("c");
            if (parseNumber != null) {
                cpu = Integer.parseInt(parseNumber);
            }
        } catch (ParseException ignored) {}

        System.out.println(SERVER_START);
        try {
            final ServerSocket serverSocket = new ServerSocket(PORT, QUEUE);
            final ForkJoinPool pool = new ForkJoinPool(cpu);
            while (true) {
                final Socket socket = serverSocket.accept();
                pool.execute(new Task(socket));
            }
        } catch (IOException e) {
            System.out.print(SERVER_STOP);
        }
    }
}
