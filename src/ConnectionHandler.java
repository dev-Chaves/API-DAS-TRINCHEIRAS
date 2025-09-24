import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ConnectionHandler implements Runnable{

    private final Socket clientSocket;

    public ConnectionHandler(Socket s){
        this.clientSocket = s;
    }

    @Override
    public void run() {

        System.out.println("Conexão aceita: " + clientSocket.getInetAddress());

        System.out.println("Esperando conexão com cliente");

        try(Socket s = this.clientSocket){
            OutputStream outputStream = s.getOutputStream();

            String msg = "Hello World";

            byte[] bytesMsg = msg.getBytes(StandardCharsets.UTF_8);

            String httpResponse = String.format(
                    "HTTP/1.1 200 OK\r\n"+
                            "Content-Type: text/plain\r\n"+
                            "Content-Length: %d\r\n"+
                            "Connection: close\r\n"+
                            "\r\n"+
                            "%s",
                    bytesMsg.length, msg
            );

            outputStream.write(httpResponse.getBytes(StandardCharsets.UTF_8));

            outputStream.flush();

            System.out.println("Thread: " + Thread.currentThread());

            System.out.println("Fechando conexão");

        } catch (IOException e) {
            System.err.println("Erro ao processar cliente: " + e.getMessage());
        }

    }

}
