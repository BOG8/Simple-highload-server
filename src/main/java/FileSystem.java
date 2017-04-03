import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by zac on 28.03.17.
 */

public class FileSystem {
    public static final String INDEX_DIR = "/index.html";
    public static String ROOT_DIR = "DOCUMENT_ROOT";

    private File file;
    private boolean isDir = false;

    public FileSystem(String path) {
        file = new File(ROOT_DIR + path);
        if (file.isDirectory()) {
            final StringBuilder builder = new StringBuilder();
            builder.append(ROOT_DIR).append(path).append(INDEX_DIR);
            file = new File(builder.toString());
            isDir = true;
        }
    }

    public FileInputStream getFile() throws FileNotFoundException{
        return new FileInputStream(file.getAbsoluteFile());
    }

    public boolean isFileExists(){
        return file.exists();
    }

    public boolean isDir(){
        return isDir;
    }

    public boolean canRead(){
        return file.canRead();
    }

    public long fileSize(){
        return file.length();
    }

    @Nullable
    public String getContentType() {
        final String path = file.getAbsolutePath();
        final int index = path.lastIndexOf('.');
        if (index == -1) {
            return null;
        }
        final String contentType = path.substring(index + 1);
        String result = null;
        switch (contentType) {
            case "html":
                result = "text/html";
                break;
            case "css":
                result = "text/css";
                break;
            case "js":
                result = "text/javascript";
                break;
            case "jpg":
                result = "image/jpeg";
                break;
            case "jpeg":
                result = "image/jpeg";
                break;
            case "png":
                result = "image/png";
                break;
            case "gif":
                result = "image/gif";
                break;
            case "swf":
                result = "application/x-shockwave-flash";
                break;
        }
        return result;
    }

}
