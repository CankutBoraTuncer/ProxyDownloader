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

    public static Object[] readResponseMessage(InputStream in) {
        try {
            Object[] imageData;
            int dataByteCount, offset, totalByteCount = 0;
            byte[] buffer = new byte[4096];
            boolean isMessageOver = false;
            int endOfHeaderIndex;
            while ((dataByteCount = in.read(buffer)) != -1) {
                offset = 0;
                if (!isMessageOver) {
                    totalByteCount+=dataByteCount;
                    String string = new String(buffer, 0, dataByteCount);
                    if ((endOfHeaderIndex = string.indexOf("\r\n\r\n")) != -1) {
                        dataByteCount = dataByteCount - endOfHeaderIndex - 4;
                        offset = endOfHeaderIndex + 4;
                        isMessageOver = true;
                    } else {
                        dataByteCount = 0;
                    }
                }
                imageData = new Object[]{buffer, offset, dataByteCount, totalByteCount};
                return imageData;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
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
/**
 * while ((count = s_in.read(buffer)) > 0) {
 * out.write(buffer, 0, count);
 * }
 * out.close();
 */

//public static ArrayList<String> readAllMessage(BufferedReader reader) throws IOException {
//    ArrayList<String> lines = new ArrayList<>();
//    boolean contFlag = false;
//    boolean isImage = false;
//    boolean readImage = false;
//    int len = 0;
//    while (true) {
//        String line;
//        try {
//            line = reader.readLine();
//            if (line == null || (line.length() == 0 && !contFlag)) {
//                break;
//            } else if (line.contains("Content-Length")) {
//                len = Integer.parseInt(line.split(" ")[1]);
//                contFlag = true;
//                lines.add(line);
//            } else if (line.contains("jpeg")) {
//                isImage = true;
//            } else if (line.length() == 0 && contFlag && isImage) {
//                readImage = true;
//            } else if (readImage) {
//
//
//                byte[] buffer = new byte[4096];
//                int count;
//                OutputStream out = new BufferedOutputStream(new FileOutputStream("C:\\Users\\USER\\OneDrive\\Desktop\\BILKENTEEE\\BilkentEEE-Year3-Semestr2\\CS421\\lab\\lab2\\ProxyDownloader\\src\\asd.jpg"));
//                while ((count = reader.read()) > 0) {
//                    out.write(buffer, 0, count);
//                }
//                out.close();
//                break;
//            } else {
//                lines.add(line);
//            }
//        } catch (IOException e) {
//            break;
//        }
//    }
//    return lines;
//}


//OutputStream dos = new FileOutputStream("test.jpg");
//    int count, offset;
//    byte[] buffer = new byte[2048];
//    boolean eohFound = false;
//while ((count = in.read(buffer)) != -1)
//        {
//        offset = 0;
//        if(!eohFound){
//        String string = new String(buffer, 0, count);
//        int indexOfEOH = string.indexOf("\r\n\r\n");
//        if(indexOfEOH != -1) {
//        count = count-indexOfEOH-4;
//        offset = indexOfEOH+4;
//        eohFound = true;
//        } else {
//        count = 0;
//        }
//        }
//        dos.write(buffer, offset, count);
//        dos.flush();
//        }
//        in.close();
//        dos.close();