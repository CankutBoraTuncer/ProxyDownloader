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
                ArrayList<String> request = Message.readAllMessage(inFromServer);
                RequestMessage requestMessage = new RequestMessage(request);
                if (requestMessage.isGETMessage()) {
                    System.out.println(requestMessage);
                    logSearchHistory(HISTORY_PATH, requestMessage);
                    // Create the client socket
                    Socket clientSocket = new Socket(requestMessage.getHttpHost(), 80);
                    clientSocket.setSoTimeout(3000);
                    // Output and input servers
                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    BufferedReader inFromServer2 = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    // Create the message and write to the server
                    outToServer.writeBytes(requestMessage.generateHttpRequestMessage());
                    // Read the server message
                    ArrayList<String> response = Message.readAllMessage(inFromServer2);
                    ResponseMessage responseMessage = new ResponseMessage(response);
                    System.out.println(responseMessage);
                    if (responseMessage.checkResponseStatusCode()) {
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