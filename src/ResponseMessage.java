import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

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
    byte[] httpDataImage;


    public ResponseMessage(ArrayList<String> responseMessage) throws IOException {
        super();
        this.httpMessage = responseMessage;
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

    private void extractHttpMessage() throws IOException {
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
                this.httpData += line;
            } else if (line.length() == 0) {
                isDataBody = true;

                break;
            } else {
                this.httpOthers += line;
            }
        }/**
         if (this.httpContentType.contains("jpeg")) {
         this.httpDataImage = this.httpData.getBytes(StandardCharsets.UTF_8);
         System.out.println(httpDataImage);
         ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.httpDataImage);
         BufferedImage image = ImageIO.read(byteArrayInputStream);
         // Write image to file in JPEG format
         File outputFile = new File("C:\\Users\\USER\\OneDrive\\Desktop\\BILKENTEEE\\BilkentEEE-Year3-Semestr2\\CS421\\lab\\lab2\\ProxyDownloader\\src\\output.jpg");
         ImageIO.write(image, "jpg", outputFile);
         }*/
    }

    public String getHttpData() {
        return this.httpData;
    }

    public boolean checkResponseStatusCode() {
        if (this.httpStatus.equals("200")) {
            System.out.println("The request is accepted!");
            return true;
        } else {
            System.out.println("Error: status code " + this.httpStatus);
            return false;
        }
    }

    @Override
    public String toString() {
        return this.httpMessageAll;
    }
}
