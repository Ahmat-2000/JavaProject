package model;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username) {
        this.socket = socket;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;

        } catch (IOException e) {
            e.printStackTrace();
            closeEveryThing(socket, bufferedReader , bufferedWriter);
        }
    }

    private void closeEveryThing(Socket s, BufferedReader bR, BufferedWriter bW) {
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

    /**
     * C'est une fonction bloquante
     * le client écoute le clavier et envoie le message quand on
     * tape sur ENTRE
     */
    public void sendMessage(){
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            @SuppressWarnings("resource")
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(username + " : "  + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

        } catch (Exception e) {
           closeEveryThing(socket, bufferedReader, bufferedWriter);
           System.exit(0);
        }
    }

    /**
     * Écoute le server
     */
    public void listenForMessage(){
        new Thread(new Runnable() {

            @Override
            public void run() {

                String messageFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        messageFromGroupChat = bufferedReader.readLine();
                        if(messageFromGroupChat == null){
                            closeEveryThing(socket, bufferedReader, bufferedWriter);
                            System.exit(0);
                        }
                        System.out.println(messageFromGroupChat);
                    } catch (Exception e) {
                        closeEveryThing(socket, bufferedReader, bufferedWriter);
                        System.exit(0);
                    }
                }
            }
            
        }).start();
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.err.print("Enter your username for the group chat : ");
            String username = scanner.nextLine();
            System.out.println();
            Socket socket = new Socket("localhost",1234);
            Client client = new Client(socket, username);
            /**
             * This two methods are blocking code but they are running on separate Threads
             * So they are no longer blocking
             */
            client.listenForMessage();
            client.sendMessage();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
