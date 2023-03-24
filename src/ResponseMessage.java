import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

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
    String httpEncoding;
    byte[] httpDataImage;


    public ResponseMessage(ArrayList<String> responseMessage, String httpEncoding) throws IOException {
        super();
        this.httpMessage = responseMessage;
        this.httpEncoding = httpEncoding;
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

    public ResponseMessage(Object[] imageData) throws IOException {
        super();
        byte[] buffer = (byte[]) imageData[0];
        int dataByteCount = (int) imageData[2];
        int offset = (int) imageData[1];
        int totalByteCount = (int) imageData[3];
        String httpHeader = new String(buffer, 0, totalByteCount - dataByteCount);
        this.httpMessage = new ArrayList<>(Arrays.asList(httpHeader.split("\n")));
        this.httpData = new String(buffer, offset, dataByteCount);
        this.httpMessageAll = httpHeader + "\r\n\r\n" + this.httpData;
        this.httpStatus = "";
        this.httpStatusLine = "";
        this.httpLen = "";
        this.httpContentType = "";
        this.httpOthers = "";
        this.httpEncode = "";
        extractHttpMessageImage();
    }

    private void extractHttpMessageImage() {
        for (String line : this.httpMessage) {
            if (this.httpMessage.indexOf(line) == 0) {
                this.httpStatusLine = line;
                this.httpStatus = httpStatusLine.split(" ")[1];
            } else if (line.contains("Content-Length")) {
                this.httpLen = line;
            } else if (line.contains("Content-Type")) {
                this.httpContentType = line;
            } else {
                this.httpOthers += line;
            }
        }
    }

    private void extractHttpMessage() {
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
            } else {
                this.httpOthers += line;
            }
        }
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
        return this.httpStatusLine;
    }
}
