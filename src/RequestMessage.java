import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestMessage extends Message {

    ArrayList<String> httpMessage;
    String httpMessageAll;
    String httpMethod;
    String httpHost;
    String httpURL;
    String httpPath;
    String httpVersion;
    String fileName;
    String httpOthers;
    String httpRequestLine;
    String httpEncoding;
    String fileType;
    boolean isValid = false;

    public RequestMessage(ArrayList<String> requestMessage) {
        super();
        this.fileType = "";
        this.httpMessage = requestMessage;
        extractHttpMessage();
    }

    public boolean isGETMessage() {
        return this.httpMethod != null && this.httpMethod.equals("GET");
    }

    private String extractFileName() {
        File file = new File(this.httpURL);
        return file.getName();

    }

    private void extractHttpMessage() {
        for (String line : this.httpMessage) {
            this.httpMessageAll += line + "\n";
            if (this.httpMessage.indexOf(line) == 0) {
                this.httpRequestLine = line;
                this.httpMethod = this.httpRequestLine.split(" ")[0];
                this.httpURL = this.httpRequestLine.split(" ")[1];
                this.httpVersion = this.httpRequestLine.split(" ")[2];
                if(this.httpURL.contains(".jpg")){
                    this.fileType = "jpeg";
                }
                this.httpHost = extractHttpHost();
                this.httpPath = extractHttpPath();
                this.fileName = extractFileName();
            } else if (line.contains("Accept-Encoding")) {
                this.httpEncoding = line;
            } else if (line.length() != 0) {
                this.httpOthers += line;
            }
        }
    }


    private String extractHttpHost() {
        String httpHost = null;
        String regex = "^http?://([^/]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(this.httpURL);
        if (matcher.find()) {
            httpHost = matcher.group(1);
        }
        return httpHost;
    }

    private String extractHttpPath() {
        Pattern pattern = Pattern.compile("^http?://[^/]+(/.*)$");
        Matcher matcher = pattern.matcher(this.httpURL);
        String path = "";
        if (matcher.find()) {
            path = matcher.group(1);
        }
        return path;
    }

    public String generateHttpRequestMessage() {
        super.topLine = this.httpRequestLine;
        super.headers.add("Host: " + this.httpHost);
        super.headers.add(this.httpOthers);
        return super.generateHttpMessage();
    }

    public String getHttpHost() {
        return httpHost;
    }

    public String getHttpURL() {
        return httpURL;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getHttpEncoding() {
        return httpEncoding;
    }

    public void setHttpEncoding(String httpEncoding) {
        this.httpEncoding = httpEncoding;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return this.httpMessageAll;
    }
}
