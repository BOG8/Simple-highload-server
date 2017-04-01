import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by zac on 28.03.17.
 */

public class Main {
    private static final int PORT = 8080;
    private static final int QUEUE = 50;
    private static final int POOL_SIZE = 4;

    public static void main(String[] args) throws IOException{
        try (ServerSocket serverSocket = new ServerSocket(PORT, QUEUE)) {
            final ForkJoinPool pool = new ForkJoinPool(POOL_SIZE);
            while (true) {
                final Socket socket = serverSocket.accept();
                pool.execute(new Task(socket));
            }
        }
    }
}
