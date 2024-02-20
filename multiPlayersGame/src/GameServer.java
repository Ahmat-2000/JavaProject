import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
@SuppressWarnings("unused")
public class GameServer {
    private ServerSocket ss;
    private int numPlayers;
    private int maxPlayers;
    private Socket p1Socket;
    private Socket p2Socket;
    private ReadFromClient p1ReadRunnable;
    private ReadFromClient p2ReadRunnable;
    private WriteToClient p1WriteRunnnable;
    private WriteToClient p2WriteRunnnable;

    private double p1x,p1y,p2x,p2y;

    public GameServer() {
        System.out.println("===== GAME SERVER =====");
        this.numPlayers = 0;
        this.maxPlayers = 2;

        p1x = 100;
        p1y = 400;
        p2x = 490;
        p2y = 400;

        try {
            this.ss = new ServerSocket(45371);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException from GameServer Constructor");
        }
    }

    public void acceptConnections(){
        try {
            System.out.println("Waiting for connections...");
            while (numPlayers < maxPlayers) {
                Socket s = ss.accept();
                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                
                numPlayers += 1;
                out.writeInt(numPlayers);
                System.out.println("Player #"+numPlayers+" has connected.");

                ReadFromClient rfc = new ReadFromClient(numPlayers, in);
                WriteToClient wtc = new WriteToClient(numPlayers, out);

                if (numPlayers == 1) {
                    p1Socket = s;
                    p1ReadRunnable = rfc;
                    p1WriteRunnnable = wtc;
                }else{
                    p2Socket = s;
                    p2ReadRunnable = rfc;
                    p2WriteRunnnable = wtc;

                    p1WriteRunnnable.sendForStartMessage();
                    p2WriteRunnnable.sendForStartMessage();

                    Thread readThread1 = new Thread(p1ReadRunnable);
                    Thread readThread2 = new Thread(p2ReadRunnable);
                    readThread1.start();
                    readThread2.start();
                    Thread writeThread1 = new Thread(p1WriteRunnnable);
                    Thread writeThread2 = new Thread(p2WriteRunnnable);
                    writeThread1.start();
                    writeThread2.start();
                }
            }
            System.out.println("No longer accepting connections");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException from acceptConnections");
        }
    }

    private class ReadFromClient implements Runnable{
        private int playerID;
        private DataInputStream dataIn;
        public ReadFromClient(int playerID, DataInputStream dataIn) {
            this.playerID = playerID;
            this.dataIn = dataIn;
            System.out.println("Read from Player #"+playerID+" Runnable created");
        }
        @Override
        public void run() {
            try {
               while (true) {
                    if (playerID == 1) {
                        p1x = dataIn.readDouble();
                        p1y = dataIn.readDouble();
                    }else{
                        p2x = dataIn.readDouble();
                        p2y = dataIn.readDouble();
                    }
               }
            } catch (IOException e) {
                System.out.println("IOException from ReadFromClient run()");
            }
        }

    }

    private class WriteToClient implements Runnable{
        private int playerID;
        private DataOutputStream dataOut;
        public WriteToClient(int playerID, DataOutputStream dataOut) {
            this.playerID = playerID;
            this.dataOut = dataOut;
            System.out.println("Write to Player #"+playerID+" Runnable created");
        }
        @Override
        public void run() {
            try {
                while (true) {
                    if (playerID == 1) {
                        dataOut.writeDouble(p2x);
                        dataOut.writeDouble(p2y);
                        dataOut.flush();
                    }else{
                        dataOut.writeDouble(p1x);
                        dataOut.writeDouble(p1y);
                        dataOut.flush();
                    }
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        System.out.println("InterruptionException from WriteToClient run()");   
                    }
                }
             } catch (IOException e) {
                 System.out.println("IOException from ReadFromClient run()");
             }
        }
        public void sendForStartMessage(){
            try {
               dataOut.writeUTF("We now have 2 players");
            } catch (IOException e) {
                System.out.println("IOException from sendForStartMessage ");
            }
        }
    }

    public static void main(String[] args) {
        GameServer gs = new GameServer();
        gs.acceptConnections();
    }
}
