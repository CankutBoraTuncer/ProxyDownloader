import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestMessage extends Message {

    String httpMessage;
    String[] httpMessageArr;
    String httpMethod;
    String httpHost;
    String httpURL;
    String httpPath;
    String httpVersion;
    String httpUserAgent;
    String httpAccept;
    String httpAcceptLanguage;
    String httpAcceptEncoding;
    String httpConnection;
    String httpData;
    String fileName;
    boolean isValid = false;

    public RequestMessage(String requestMessage) {
        super();
        this.httpMessage = requestMessage;
        this.httpMessageArr = requestMessage.split("\n");
        this.httpMethod = extractHttpMethod();
        if (isGETMessage()) {
            this.httpURL = extractHttpURL();
            this.httpHost = extractHttpHost();
            this.httpPath = extractHttpPath();
            this.httpVersion = extractHttpVersion();
            this.httpUserAgent = extractUserAgent();
            this.httpAccept = extractHttpAccept();
            this.httpAcceptLanguage = extractHttpAcceptLanguage();
            this.httpAcceptEncoding = extractHttpAcceptEncoding();
            this.httpConnection = extractHttpConnection();
            this.httpData = extractHttpData();
            this.fileName = extractFileName();
            this.isValid = true;
            System.out.println(this);
        }
    }

    public boolean isGETMessage() {
        return this.httpMethod.equals("GET");
    }

    private String extractFileName() {
        File file = new File(this.httpURL);
        String fileName = file.getName();
        return fileName;
    }

    private String extractHttpData() {
        return this.httpMessageArr[7];
    }

    private String extractHttpConnection() {
        return this.httpMessageArr[6];
    }

    private String extractHttpAcceptEncoding() {
        return this.httpMessageArr[5];
    }

    private String extractHttpAcceptLanguage() {
        return this.httpMessageArr[4];
    }

    private String extractHttpAccept() {
        return this.httpMessageArr[3];
    }

    private String extractUserAgent() {
        return this.httpMessageArr[2];
    }

    private String extractHttpVersion() {
        return this.httpMessageArr[0].split(" ")[2];
    }

    private String extractHttpURL() {
        return this.httpMessageArr[0].split(" ")[1];
    }

    private String extractHttpMethod() {
        return this.httpMessageArr[0].split(" ")[0];
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
        super.topLine = this.httpMethod + " " + this.httpPath + " " + this.httpVersion;
        super.headers.add("Host: " + this.httpHost);
        super.headers.add(this.httpUserAgent);
        super.headers.add(this.httpAccept);
        super.headers.add(this.httpAcceptLanguage);
        super.headers.add(this.httpAcceptEncoding);
        super.headers.add(this.httpConnection);
        return super.generateHttpMessage();
    }

    public String getHttpMessage() {
        return httpMessage;
    }

    public void setHttpMessage(String httpMessage) {
        this.httpMessage = httpMessage;
    }

    public String[] getHttpMessageArr() {
        return httpMessageArr;
    }

    public void setHttpMessageArr(String[] httpMessageArr) {
        this.httpMessageArr = httpMessageArr;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getHttpHost() {
        return httpHost;
    }

    public void setHttpHost(String httpHost) {
        this.httpHost = httpHost;
    }

    public String getHttpURL() {
        return httpURL;
    }

    public void setHttpURL(String httpURL) {
        this.httpURL = httpURL;
    }

    public String getHttpPath() {
        return httpPath;
    }

    public void setHttpPath(String httpPath) {
        this.httpPath = httpPath;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getHttpUserAgent() {
        return httpUserAgent;
    }

    public void setHttpUserAgent(String httpUserAgent) {
        this.httpUserAgent = httpUserAgent;
    }

    public String getHttpAccept() {
        return httpAccept;
    }

    public void setHttpAccept(String httpAccept) {
        this.httpAccept = httpAccept;
    }

    public String getHttpAcceptLanguage() {
        return httpAcceptLanguage;
    }

    public void setHttpAcceptLanguage(String httpAcceptLanguage) {
        this.httpAcceptLanguage = httpAcceptLanguage;
    }

    public String getHttpAcceptEncoding() {
        return httpAcceptEncoding;
    }

    public void setHttpAcceptEncoding(String httpAcceptEncoding) {
        this.httpAcceptEncoding = httpAcceptEncoding;
    }

    public String getHttpConnection() {
        return httpConnection;
    }

    public void setHttpConnection(String httpConnection) {
        this.httpConnection = httpConnection;
    }

    public String getHttpData() {
        return httpData;
    }

    public void setHttpData(String httpData) {
        this.httpData = httpData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    @Override
    public String toString() {
        return "Message{" + "HttpType='" + httpMethod + '\'' + ", HttpURL='" + httpHost + '\'' + ", HttpVersion='" + httpVersion + '\'' + '}';
    }
}
