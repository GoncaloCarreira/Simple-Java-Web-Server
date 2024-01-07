import java.io.*;
import java.net.ServerSocket;

import java.net.Socket;

public class WebServer {
    public static void main(String[] args) throws IOException {


        int portNumber = 9001;

        ServerSocket serverSocket = new ServerSocket(portNumber);

        Socket clientSocket = serverSocket.accept();


        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String input = in.readLine();


        File index = new File("www/index.html");
        File error = new File("www/404.html");
        File logo = new File("www/logo.png");


        //INDEX
        BufferedReader readIndexFile = new BufferedReader(new FileReader(index));

        String fileIndexLine = readIndexFile.readLine();
        StringBuilder fileIndexResult = new StringBuilder();

        while (fileIndexLine != null) {
            fileIndexResult.append(fileIndexLine);
            fileIndexLine = readIndexFile.readLine();
        }


        //404
        BufferedReader readErrorFile = new BufferedReader(new FileReader(error));

        String fileErrorLine = readErrorFile.readLine();
        StringBuilder fileErrorResult = new StringBuilder();

        while (fileErrorLine != null) {
            fileErrorResult.append(fileErrorLine);
            fileErrorLine = readErrorFile.readLine();
        }


        //LOGO
        FileInputStream inputStream = new FileInputStream(logo);

        if (index.exists()) {

            if (input.equals("GET /index.html HTTP/1.1")) {

                String headerOK = "HTTP/1.0 200 Document Follows\r\n" +
                        "Content-Type: text/html; charset=UTF-8\r\n" +
                        "Content-Length: " + fileIndexResult.length() + " \r\n" +
                        "\r\n";

                clientSocket.getOutputStream().write(headerOK.getBytes());

                clientSocket.getOutputStream().write(fileIndexResult.toString().getBytes());

                clientSocket.getOutputStream().flush();

            } else if (input.equals("GET /logo.png HTTP/1.1")) {

                String headerImgOK = "HTTP/1.0 200 Document Follows\r\n" +
                        "Content-Type: image/png \r\n" +
                        "Content-Length: " + logo.length() + " \r\n" +
                        "\r\n";

                clientSocket.getOutputStream().write(headerImgOK.getBytes());

                sendImg(inputStream, clientSocket.getOutputStream());

                clientSocket.getOutputStream().flush();

            } else {

                String headerError = "HTTP/1.0 404 Not Found\r\n" +
                        "Content-Type: text/html; charset=UTF-8\r\n" +
                        "Content-Length: " + fileErrorResult.length() + " \r\n" +
                        "\r\n";

                clientSocket.getOutputStream().write(headerError.getBytes());

                clientSocket.getOutputStream().write(fileErrorResult.toString().getBytes());

                clientSocket.getOutputStream().flush();

            }
        }
        clientSocket.close();
        in.close();
    }

    public static void sendImg(FileInputStream inputStream, OutputStream outputStream) {
        byte[] buffer = new byte[1024];
        try {
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        } catch (IOException e) {
            System.out.println("Error reading/writing file.");
        }
    }
}


