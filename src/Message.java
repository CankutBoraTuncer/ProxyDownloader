import java.io.*;
import java.util.ArrayList;

public class Message {

    private static final String LOG_FILE = new File("").getAbsolutePath().concat("\\http_log.txt");
    protected String topLine;
    protected ArrayList<String> headers;
    protected String body;
    protected String httpMessage;

    public Message() {
        this.topLine = "";
        this.headers = new ArrayList<>();
        this.body = "";
    }

    protected String generateHttpMessage() {
        String httpMessage = "";
        // Adding the request line
        httpMessage += this.topLine + "\r\n";
        // Header lines
        for (String header : this.headers) {
            httpMessage += header + "\r\n";
        }
        // End of header
        httpMessage += "\r\n";
        // Adding the body
        httpMessage += this.body;
        this.httpMessage = httpMessage;
        log(LOG_FILE, this.httpMessage);
        return httpMessage;
    }

    public static ArrayList<String> readAllMessage(BufferedReader reader) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        boolean contFlag = false;
        while (true) {
            String line;
            try {
                line = reader.readLine();
                if (line != null && line.contains("Content-Length")) {
                    contFlag = true;
                    continue;
                }
                if (line == null || (line.length() == 0 && !contFlag)) {
                    break;
                } else {
                    lines.add(line);
                }
            } catch (IOException e) {
                break;
            }

        }
        return lines;
    }

    @Override
    public String toString() {
        return generateHttpMessage();
    }

    public static void log(String fileName, String data) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
            out.println(data);
            out.close();
        } catch (IOException e) {
            System.out.println("An error occurred. " + fileName);
        }
    }
}
