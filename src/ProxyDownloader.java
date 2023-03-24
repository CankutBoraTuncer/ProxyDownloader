import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ProxyDownloader {
    final static String HISTORY_PATH = new File("").getAbsolutePath().concat("\\history.txt");
    static int histCount = 1;

    public static void main(String[] args) {
        // Read the port
        int port = Integer.parseInt(args[0]);
        try {
            // Create the welcome socket
            ServerSocket welcomeSocket = new ServerSocket(port);
            while (true) {
                // Contact with the client
                Socket connectionSocket = welcomeSocket.accept();
                // Create the stream
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                // The HTTP message
                ArrayList<String> request = Message.readResponseMessage(inFromServer);
                RequestMessage requestMessage = new RequestMessage(request);
                if (requestMessage.isGETMessage()) {
                    System.out.println(requestMessage);
                    logSearchHistory(HISTORY_PATH, requestMessage);
                    // Create the client socket
                    Socket clientSocket = new Socket(requestMessage.getHttpHost(), 80);
                    clientSocket.setSoTimeout(3000);
                    // Output and input servers
                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    outToServer.writeBytes(requestMessage.generateHttpRequestMessage());
                    ResponseMessage responseMessage;
                    if (requestMessage.getFileType().equals("jpeg")) {
                        InputStream inFromServerImage = clientSocket.getInputStream();
                        Object[] imageData = Message.readResponseMessage(inFromServerImage);
                        responseMessage = new ResponseMessage(imageData);
                    } else {
                        BufferedReader inFromServer2 = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        ArrayList<String> response = Message.readResponseMessage(inFromServer2);
                        responseMessage = new ResponseMessage(response, requestMessage.getHttpEncoding());
                    }
                    if (responseMessage.checkResponseStatusCode()) {
                        System.out.println(responseMessage);
                        Message.log(requestMessage.getFileName(), responseMessage.getHttpData());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error occured: " + e);
        }
    }

    public static void logSearchHistory(String fileName, RequestMessage message) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(Calendar.getInstance().getTime());
        String data = ProxyDownloader.histCount + " - " + timeStamp + ": " + message.getHttpURL();
        Message.log(fileName, data);
    }
}