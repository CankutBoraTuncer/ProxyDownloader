import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ProxyDownloader {
    final static String ROOT_PATH = new File("").getAbsolutePath().concat("\\src\\");

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
                String request = readAllMessage(inFromServer);
                RequestMessage requestMessage = new RequestMessage(request);
                if (requestMessage.isValid()) {
                    // Create the client socket
                    Socket clientSocket = new Socket(requestMessage.getHttpHost(), 80);
                    // Output and input servers
                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    BufferedReader inFromServer2 = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    // Create the message and write to the server
                    outToServer.writeBytes(requestMessage.generateHttpRequestMessage());
                    // Read the server message
                    String response = readAllMessage(inFromServer2);
                    ResponseMessage responseMessage = new ResponseMessage(response);
                    saveToTxt(requestMessage.getFileName(), responseMessage.getHttpData());
                    //responseMessage.checkResponseStatusCode();
                    //System.out.println(inFromServer2.readLine());
                }
            }
        } catch (IOException e) {
            System.out.println("problemo:" + e);
        }
    }

    public static String readAllMessage(BufferedReader reader) {
        String lines = "";
        boolean contFlag = false;
        while (true) {
            String line;
            try {
                line = reader.readLine();
                if(line != null && line.contains("Content-Length")){
                    contFlag = true;
                }
            } catch (IOException e) {
                break;
            }
            if (line == null ||(line.length() == 0 && !contFlag)) {
                break;
            } else {
                lines += line + "\n";
            }
        }
        return lines;
    }

    public static void saveToTxt(String name, String data){
        try {
            File file = new File(name);
            boolean foo = file.createNewFile();
            FileWriter myWriter = new FileWriter(name);
            myWriter.write(data);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }
}