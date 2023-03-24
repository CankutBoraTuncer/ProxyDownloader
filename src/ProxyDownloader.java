import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ProxyDownloader {
    final static String ROOT_PATH = new File("").getAbsolutePath();
    final static String HISTORY_LOG_PATH = ROOT_PATH.concat("\\history.txt");
    static int histCount = 1;

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        try {
            ServerSocket welcomeSocket = new ServerSocket(port);
            while (true) {
                Socket connectionSocket = welcomeSocket.accept();
                ArrayList<String> request = readResponse(connectionSocket);
                RequestMessage requestMessage = new RequestMessage(request);
                if (requestMessage.isGETMessage()) {
                    System.out.println(requestMessage);
                    logSearchHistory(HISTORY_LOG_PATH, requestMessage);
                    Socket clientSocket = initSocket(requestMessage.getHttpHost(), 80, 5000);
                    sendRequest(clientSocket, requestMessage);
                    if (requestMessage.getFileType().equals("jpeg")) {
                        readImageResponse(clientSocket, requestMessage);
                    } else {
                        ResponseMessage responseMessage = readResponse(clientSocket, requestMessage);
                        String fileName = requestMessage.getFileName();
                        saveTxt(responseMessage, fileName);
                    }
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Error occurred: " + e);
        }
    }

    public static void readImageResponse(Socket clientSocket, RequestMessage requestMessage) throws IOException {
        InputStream inFromServerImage = clientSocket.getInputStream();
        String fileName = ROOT_PATH.concat("\\" + requestMessage.getFileName());
        Message.readResponseImageAndSave(inFromServerImage, fileName);
    }

    public static ResponseMessage readResponse(Socket clientSocket, RequestMessage requestMessage) {
        try {
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ArrayList<String> response = Message.readResponseMessage(inFromServer);
            return new ResponseMessage(response, requestMessage.getHttpEncoding());
        } catch (IOException ioe) {
            System.out.println("IOException when reading response message.");
            return null;
        }
    }

    public static ArrayList<String> readResponse(Socket clientSocket) {
        try {
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            return Message.readResponseMessage(inFromServer);
        } catch (IOException ioe) {
            System.out.println("IOException when reading response message.");
            return null;
        }
    }

    public static void sendRequest(Socket clientSocket, RequestMessage requestMessage) {
        try {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes(requestMessage.generateHttpRequestMessage());
        } catch (IOException ioe) {
            System.out.println("IOException when sending request message.");
        }
    }

    public static void logSearchHistory(String fileName, RequestMessage message) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(Calendar.getInstance().getTime());
        String data = ProxyDownloader.histCount + " - " + timeStamp + ": " + message.getHttpURL();
        Message.log(fileName, data, true);
        histCount++;
    }

    public static void saveTxt(ResponseMessage responseMessage, String fileName) {
        if (responseMessage.checkResponseStatusCode()) {
            Message.log(fileName, responseMessage.getHttpData(), false);
        }
    }

    public static Socket initSocket(String host, int port, int timeout) throws IOException {
        Socket socket = new Socket(host, 80);
        socket.setSoTimeout(timeout);
        return socket;
    }
}


