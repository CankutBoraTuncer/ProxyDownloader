import java.io.*;
import java.net.SocketTimeoutException;
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
        log(LOG_FILE, this.httpMessage, true);
        return httpMessage;
    }

    public static ArrayList<String> readResponseMessage(BufferedReader reader) {
        ArrayList<String> lines = new ArrayList<>();
        boolean contFlag = false;
        while (true) {
            String line;
            try {
                line = reader.readLine();
                if (line == null || (line.length() == 0 && !contFlag)) {
                    break;
                } else if (line.contains("Content-Length")) {
                    contFlag = true;
                }
                lines.add(line);
            } catch (IOException e) {
                break;
            }
        }
        return lines;
    }

    public static void readResponseImageAndSave(InputStream in, String fileName) {
        try {
            boolean isHeaderOver = false;
            boolean isLegitReturn = false;
            byte[] buffer = new byte[2048];
            int dataByteCount, offset;
            int endOfHeaderIndex;
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileName));
            while ((dataByteCount = in.read(buffer)) != -1) {
                offset = 0;
                if (!isHeaderOver) {
                    String string = new String(buffer, 0, dataByteCount);
                    if (string.contains("200 OK")) {
                        System.out.println("The response message returned 200 OK!");
                        isLegitReturn = true;
                    }
                    if (isLegitReturn && (endOfHeaderIndex = string.indexOf("\r\n\r\n")) != -1) {
                        dataByteCount = dataByteCount - endOfHeaderIndex - 4;
                        offset = endOfHeaderIndex + 4;
                        isHeaderOver = true;
                    } else {
                        dataByteCount = 0;
                    }
                }
                outputStream.write(buffer, offset, dataByteCount);
                outputStream.flush();
            }
            outputStream.close();
            return;
        } catch (SocketTimeoutException e) {
            System.out.println("The image file " + fileName + " is logged.");
            return;
        } catch (IOException e) {
            System.out.println("IOException when reading image data.");
            return;
        }
    }

    @Override
    public String toString() {
        return generateHttpMessage();
    }

    public static void log(String fileName, String data, boolean append) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, append)));
            out.println(data);
            out.close();
        } catch (IOException e) {
            System.out.println("An error occurred. " + fileName);
        }
    }
}
;