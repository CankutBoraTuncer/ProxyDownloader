import java.io.IOException;
import java.util.ArrayList;


public class ResponseMessage extends Message {

    ArrayList<String> httpMessage;
    String httpMessageAll;
    String httpStatus;
    String httpStatusLine;
    String httpLen;
    String httpContentType;
    String httpData;
    String httpEncode;
    String httpOthers;
    String httpEncoding;

    public ResponseMessage(ArrayList<String> responseMessage, String httpEncoding) throws IOException {
        super();
        this.httpMessage = responseMessage;
        this.httpEncoding = httpEncoding;
        this.httpMessageAll = "";
        this.httpStatus = "";
        this.httpStatusLine = "";
        this.httpLen = "";
        this.httpContentType = "";
        this.httpData = "";
        this.httpOthers = "";
        this.httpEncode = "";
        extractHttpMessage();
    }


    private void extractHttpMessage() {
        boolean isDataBody = false;
        for (String line : this.httpMessage) {
            this.httpMessageAll += line + "\n";
            if (this.httpMessage.indexOf(line) == 0) {
                this.httpStatusLine = line;
                this.httpStatus = httpStatusLine.split(" ")[1];
            } else if (line.contains("Content-Length")) {
                this.httpLen = line;
            } else if (line.contains("Content-Type")) {
                this.httpContentType = line;
            } else if (isDataBody) {
                this.httpData += line + "\n";
            } else if (line.length() == 0) {
                isDataBody = true;
            } else {
                this.httpOthers += line;
            }
        }
    }

    public String getHttpData() {
        return this.httpData;
    }

    public boolean checkResponseStatusCode() {
        if (this.httpStatus.equals("200")) {
            System.out.println("The response message returned 200 OK!");
            return true;
        } else {
            System.out.println("Error: status code " + this.httpStatus);
            return false;
        }
    }

    @Override
    public String toString() {
        return this.httpStatusLine;
    }
}
