import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by zac on 28.03.17.
 */

public class Response {
    private static final String SERVER = "BOG_Server";
    private static final String CONNECTION = "connection";

    private FileSystem fileSystem = null;
    private Long contentLength = null;
    private String contentType = null;

    private static final String HTTP_VERSION = "HTTP/1.1";
    private String status = "200 OK";
    private Request request;

    public Response(Request request) {
        this.request = request;
        if (request.getPathFile() != null) {
            fileSystem = new FileSystem(request.getPathFile());
        }
        if (fileSystem != null) {
            contentLength = fileSystem.fileSize();
            contentType = fileSystem.getContentType();
        }
        setStatus();
    }

    public void writeResponse(OutputStream out) throws IOException {
        writeHeaders(out);
        if (request.getMethod().equals("GET") && fileSystem.isFileExists()) {
            writeFile(out);
        }
    }

    private void writeHeaders(OutputStream out) throws IOException {
        final StringBuilder buf = new StringBuilder();
        buf.append(HTTP_VERSION).append(' ').append(status).append("\r\n");
        out.write(buf.toString().getBytes());
        if (!isSupports()) {
            return;
        }
        buf.setLength(0);
        if (contentLength != null) {
            buf.append("Content-Length:").append(' ').append(contentLength).append("\r\n");
        }
        if (contentType != null) {
            buf.append("Content-Type:").append(' ').append(contentType).append("\r\n");
        }
        final String date = getServerTime();
        buf.append("Date:").append(' ').append(date).append('\n');
        buf.append("Server:").append(' ').append(SERVER).append('\n');
        buf.append("Connection:").append(' ').append(CONNECTION).append("\r\n\r\n");
        out.write(buf.toString().getBytes());
    }

    private boolean isSupports(){
        final String method = request.getMethod();
        return method.equals("GET") || method.equals("HEAD");
    }

    private static String getServerTime() {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEEEEEEEEE, dd MMMMMMMMMM yyyy HH:mm:ss z",
                Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }

    private void writeFile(OutputStream out) throws IOException{
        try {
            final FileInputStream reader = fileSystem.getFile();
            final byte[] chunk = new byte[16 * 1024];
            int lenght;
            while ((lenght = reader.read(chunk)) > 0) {
                out.write(chunk, 0, lenght);
            }
            out.flush();
        } catch (FileNotFoundException ignored) {}
    }

    private void setStatus() {
        if (request.getMethod().equals("POST")) {
            status = "405 Method Not Allowed";
        } else if (fileSystem == null || (!fileSystem.isDir() && !fileSystem.isFileExists())) {
            status = "404 Not Found";
        } else if (fileSystem != null && (!fileSystem.canRead() || !fileSystem.isFileExists())) {
            status = "403 Forbidden";
        }
    }
}


