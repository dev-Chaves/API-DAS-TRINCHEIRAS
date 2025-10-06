import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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

            BufferedReader reader = null;

            InputStreamReader input = new InputStreamReader(s.getInputStream());

            reader = new BufferedReader(input);

            String linha;
            // readLine permite a leitura apenas uma vez
            String metodo = reader.readLine().toLowerCase().split(" ")[0].trim().toLowerCase();

            switch (metodo){

                case "post":
                    int contentLength = 0;

                    while ((linha = reader.readLine()) != null && !linha.isBlank()){
                        if(linha.toLowerCase().startsWith("content-length:") ){

                            contentLength = Integer.parseInt(linha.split(":")[1].trim());

//                            System.out.println("Content-length: " + contentLength);

                        }
//                        System.out.println(linha);
                    }

                    char[] contet = new char[contentLength];

                    reader.read(contet, 0, contentLength);

                    String reqBody = new String(contet);

                    ProdutoRequest produto = SerializerRequest.desserialize(reqBody);

                    String httpResponse = String.format(
                            "HTTP/1.1 200 OK\r\n"+
                                    "Content-Type: application/json\r\n"+
                                    "Content-Length: %d\r\n"+
                                    "Connection: close\r\n"+
                                    "\r\n"+
                                    "%s",
                            SerializerRequest.serialize(produto).length(), SerializerRequest.serialize(produto));


                    outputStream.write(httpResponse.getBytes(StandardCharsets.UTF_8));

                    outputStream.flush();
                break;

                case "get":
                    System.out.println("feito o get");

                break;
            }

            System.out.println("------------------------------ \r\n");

            System.out.println("Thread: " + Thread.currentThread());

            System.out.println("Fechando conexão");

        } catch (IOException e) {
            System.err.println("Erro ao processar cliente: " + e.getMessage());
        }

    }

}
