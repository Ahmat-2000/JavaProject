package server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * We implement Runnable because each client will run on a separate
 * Thread.
 * for a class to be able to run on a separate new Tread, it should
 * implements Runnable
 */
public class ClientHandler implements Runnable{
    /**
     * This list will containt all client handler
     */
    public static ArrayList<ClientHandler> CLIENTHANDLERLIST = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    /**
     * We use BufferedWriter and BufferedReader just for performence
     * The difference between OutputStreamWriter and OutputStreamString is that the
     * first one send data a byte and the second one sends it as character
     * So we use bufferedReader to read data from others and bufferedWriter to send data to
     * others
     * @param s
     */
    public ClientHandler(Socket s){
        socket = s;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientUsername = bufferedReader.readLine();
            // Add this handler to the list
            CLIENTHANDLERLIST.add(this);
            broadcastMessage("SERVER : "+ clientUsername + " has entered the chat!");
        } catch (IOException e) {
            e.printStackTrace();
            closeEveryThing(socket, bufferedReader , bufferedWriter);
        }
    }

    private void closeEveryThing(Socket s, BufferedReader bR, BufferedWriter bW) {
       remveClientHandler();
       try {
            if (bR != null) {
                bR.close();
            }
            if(bW != null){
                bW.close();
            }
            if (s !=null) {
                s.close();
            }
       } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void broadcastMessage(String messageToSend) {
       for (ClientHandler ch : CLIENTHANDLERLIST) {
        try {
            if (!ch.clientUsername.equals(this.clientUsername)) {
                ch.bufferedWriter.write(messageToSend); // we send the message without newLine character
                ch.bufferedWriter.newLine(); // we send the newLine character
                /**
                 * The buffer does not send the message unless the buffer is full, so we send it
                 * manuly with the flush method
                 */
                ch.bufferedWriter.flush();
            }
        } catch (Exception e) {
            closeEveryThing(socket, bufferedReader, bufferedWriter);
        }
       }
    }
    /**
     * We use this method to remove a clientHandler when the client left the chat
     */
    public void remveClientHandler(){
        CLIENTHANDLERLIST.remove(this);
        broadcastMessage("SERVER : " + clientUsername + " has left the chat");
    }

    /**
     * Every thing in this method will run in a separate Thread.
     * Read form server or write to server is a blocking code, so we will
     * run it on separate Thread
     */
    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (Exception e) {
                closeEveryThing(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }
    
}
