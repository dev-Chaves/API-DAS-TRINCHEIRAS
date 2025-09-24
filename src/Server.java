import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public Server(int port){

        ExecutorService pool = Executors.newFixedThreadPool(4);

        try(ServerSocket ss = new ServerSocket(port)){

            System.out.println("Server startou na porta: " + port);

            while (true){

                Socket clientSocket = ss.accept();

                pool.submit(new ConnectionHandler(clientSocket));

            }
        } catch (IOException e) {
            System.out.println("Erro:" + e);
        }

    }
}
