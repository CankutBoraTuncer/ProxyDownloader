import java.util.ArrayList;

public class Message {
    protected String topLine;
    protected ArrayList<String> headers;
    protected String body;
    public Message(){
        this.topLine = "";
        this.headers = new ArrayList<>();
        this.body = "";
    }

    protected String generateHttpMessage(){
        String httpMessage = "";
        // Adding the request line
        httpMessage += this.topLine + "\r\n";
        // Header lines
        for(String header : this.headers){
            httpMessage += header + "\r\n";
        }
        // End of header
        httpMessage += "\r\n";
        // Adding the body
        httpMessage += this.body;
        System.out.println(httpMessage);
        return httpMessage;
    }

    @Override
    public String toString() {
        return generateHttpMessage();
    }
}
