import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {


    public Server(int port){

        try(ServerSocket ss = new ServerSocket(port)){

            System.out.println("Server startou na porta: " + port);

            while (true){
                System.out.println("Esperando conexão com cliente");

                try (Socket s = ss.accept()) {


                    System.out.println("Conexão aceita: " + s.getInetAddress());

                    OutputStream outputStream = s.getOutputStream();

                    String message = "Hello World";

                    byte[] bodyBytes = message.getBytes(StandardCharsets.UTF_8);

                    String httpResponse = String.format(
                            "HTTP/1.1 200\r\n" +
                                    "Content-Type: text/plan\r\n" +
                            "Content-Length: %d\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n" +
                                    "%s",
                            bodyBytes.length,
                            message
                    );

                    outputStream.write(httpResponse.getBytes(StandardCharsets.UTF_8));

                    outputStream.flush();

                    System.out.println("Fechando conexão");
                }
            }
        } catch (IOException e) {
            System.out.println("Erro:" + e);
        }

    }

    private int getLengthStringToBytes(String msg){
        byte[] msgBytes = msg.getBytes(StandardCharsets.UTF_8);
        return msgBytes.length;
    }

}
