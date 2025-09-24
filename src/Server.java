import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class Server {


    public Server(int port) throws InterruptedException {

        ExecutorService pool = Executors.newFixedThreadPool(4);

        try(ServerSocket ss = new ServerSocket(port)){

                System.out.println("Server startou na porta: " + port);

                while (true){

                    Socket s = ss.accept();

                    System.out.println("Conexão aceita: " + s.getInetAddress());

                    System.out.println("Esperando conexão com cliente");

                    pool.submit(tentarConexaoSocket(s));

            }
        } catch (IOException e) {
            System.out.println("Erro:" + e);
        }finally {
            pool.shutdown();
            pool.awaitTermination(5, TimeUnit.SECONDS);
        }

    }

    private Runnable tentarConexaoSocket(Socket s){

            return () -> {
                try(Socket socket = s) {

                    OutputStream outputStream = socket.getOutputStream();

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

                    socket.shutdownOutput();

                    System.out.println("Thread: " + Thread.currentThread());

                    System.out.println("Fechando conexão");
                } catch (IOException e) {
                    System.out.println("Erro:" + e.getMessage());
                }
            };
    }

}
