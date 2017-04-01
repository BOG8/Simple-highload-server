import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by zac on 28.03.17.
 */

public class Request {
    private String pathFile = null;
    private String method = null;

    public void setHeader(String line) {
        method = getMethod(line);
        definePath(line);
    }

    @Nullable
    public String getMethod(String line){
        final int spaceIndex = line.indexOf(' ');
        if (spaceIndex != -1) {
            return line.substring(0, spaceIndex);
        }
        return null;
    }

    public void definePath(String line) {
        final int patchBeginningIndex = line.indexOf(' ') + 1;
        final int afterPatchIndex = line.indexOf(' ', patchBeginningIndex);
        pathFile = line.substring(patchBeginningIndex, afterPatchIndex);
        try {
            pathFile = URLDecoder.decode(pathFile, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {}
        final int questionIndex = pathFile.indexOf('?');
        if (questionIndex != -1) {
            pathFile = pathFile.substring(0, questionIndex);
        }
        if (pathFile.contains("../")) {
            pathFile = null;
        }
    }

    public String getPathFile(){
        return pathFile;
    }

    public String getMethod(){
        return method;
    }

}
