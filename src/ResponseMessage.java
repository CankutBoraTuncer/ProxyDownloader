public class ResponseMessage extends Message {

    String httpMessage;
    String[] httpMessageArr;
    String httpStatus;
    String httpDate;
    String httpServer;
    String httpLastModifiedDate;
    String httpLen;
    String httpContentType;
    String httpData;

    public ResponseMessage(String responseMessage) {
        super();
        this.httpMessage = responseMessage;
        this.httpMessageArr = responseMessage.split("\n");
        this.httpStatus = extractStatusCode();
        if(checkResponseStatusCode()){
            this.httpDate = extractDate();
            this.httpServer = extractServer();
            this.httpLastModifiedDate = extractLastModifiedDate();
            this.httpLen = extractLen();
            this.httpContentType = extractContentType();
            this.httpData = extractData();
            System.out.println(this);
        }
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

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getHttpDate() {
        return httpDate;
    }

    public void setHttpDate(String httpDate) {
        this.httpDate = httpDate;
    }

    public String getHttpServer() {
        return httpServer;
    }

    public void setHttpServer(String httpServer) {
        this.httpServer = httpServer;
    }

    public String getHttpLastModifiedDate() {
        return httpLastModifiedDate;
    }

    public void setHttpLastModifiedDate(String httpLastModifiedDate) {
        this.httpLastModifiedDate = httpLastModifiedDate;
    }

    public String getHttpLen() {
        return httpLen;
    }

    public void setHttpLen(String httpLen) {
        this.httpLen = httpLen;
    }

    public String getHttpContentType() {
        return httpContentType;
    }

    public void setHttpContentType(String httpContentType) {
        this.httpContentType = httpContentType;
    }

    public String getHttpData() {
        return httpData;
    }

    public void setHttpData(String httpData) {
        this.httpData = httpData;
    }

    private String extractData() {
        String data = "";
        for (int i = 11; i < httpMessageArr.length; i++){
            data += httpMessageArr[i] + "\n";
        }
        return data;
    }

    private String extractContentType() {
        return this.httpMessageArr[9];
    }

    private String extractLen() {
        return this.httpMessageArr[6];
    }

    private String extractLastModifiedDate() {
        return this.httpMessageArr[3];
    }

    private String extractServer() {
        return this.httpMessageArr[2];
    }

    private String extractDate() {
        return this.httpMessageArr[1];
    }

    private String extractStatusCode() {
        return this.httpMessageArr[0];
    }

    public boolean checkResponseStatusCode() {
        if (this.httpStatus.contains("200")) {
            System.out.println("The request is accepted!");
            return true;
        } else {
            System.out.println("Error: status code " + this.httpStatus);
            return false;
        }
    }

    @Override
    public String toString() {
        return this.httpStatus + "\n" + this.httpDate + "\n" + this.httpServer + "\n" + this.httpLastModifiedDate + "\n" + this.httpLen + "\n" + this.httpContentType +"\n" + this.httpData;
    }
}
