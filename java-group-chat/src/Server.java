import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }


    /**
     * Start the server
     */
    public void startServer(){
        try {
            // while the server socket is open, wait for client 
            // to connect
            System.out.println("Server waiting for connection");
            while (! serverSocket.isClosed()) {
                // The accept method return a socket object and it's a blocking code
                Socket socket = serverSocket.accept(); // we wont passe this line untile a client connect
                System.out.println("A new client has connected !");

                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void closeServerSocket(){
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // We create a server listing for client on port 1234
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}