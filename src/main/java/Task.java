import java.io.*;
import java.net.Socket;
import java.util.concurrent.RecursiveAction;

/**
 * Created by zac on 28.03.17.
 */

public class Task extends RecursiveAction {
    Socket socket;
    InputStream in;
    OutputStream out;
    BufferedReader reader;
    BufferedWriter writer;

    public Task(Socket socket) throws IOException {
        this.socket = socket;
        in = socket.getInputStream();
        out = socket.getOutputStream();
        reader = new BufferedReader(new InputStreamReader(in));
        writer = new BufferedWriter(new OutputStreamWriter(out));
    }

    @Override
    protected void compute() {
        try {
            final Request request = new Request();
            final String buf = reader.readLine();
            request.setHeader(buf);
            final Response response = new Response(request);
            response.writeResponse(out);
        } catch (IOException ignored) {} finally {
            closeAll();
        }
    }

    public void closeAll() {
        try {
            in.close();
            out.close();
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException ignored) {}
    }

}
