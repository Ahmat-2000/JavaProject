import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

@SuppressWarnings("unused")

public class PlayerFrame extends JFrame{
    private int width, height;
    private Container contentPane;
    private PlayerSprite me;
    private PlayerSprite enemy;
    private DrawingComponent dc;
    private Timer animationTimer;
    private boolean up,down,left,right;
    private Socket socket; 
    private int playerID;
    private ReadFromServer rfsRunnable;
    private WriteToServer wtsRunnable;

    public PlayerFrame(int w, int h) {
        width = w;
        height = h;
        up = false;
        down = false;
        left = false;
        right = false;
    }
    public void setUpGUI(){
        contentPane = this.getContentPane();
        this.setTitle("Player #"+playerID);
        this.setSize(width,height);
        contentPane.setPreferredSize(new Dimension(width,height));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        createSprite();
        dc = new DrawingComponent();
        contentPane.add(dc);
        setUpAnimationTimer();
        setUpKeyListener();

        this.setVisible(true);
    }
    
    private void createSprite(){
        if (playerID == 1) {
            me = new PlayerSprite(100,400,50,Color.BLUE);   
            enemy = new PlayerSprite(490,400,50,Color.RED);
        }else {
            enemy = new PlayerSprite(100,400,50,Color.BLUE);   
            me = new PlayerSprite(490,400,50,Color.RED);
        }
    }

    private class DrawingComponent extends JComponent {
        protected void paintComponent(Graphics g){
            Graphics2D g2d = (Graphics2D) g;
            enemy.drawSprite(g2d);
            me.drawSprite(g2d);
        }
    }
    public void setUpAnimationTimer() {
       int interval = 10;
       ActionListener al = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            double speed = 5;
            if (up) {
                me.moveV(-speed);
            }
            else if (down) {
                me.moveV(speed);
            }
            else if (left) {
                me.moveH(-speed);
            }
            else if (right) {
                me.moveH(speed);
            }
            dc.repaint();
        }        
       };
       animationTimer = new Timer(interval,al);
       animationTimer.start();
    }
    private void connectToServer(){
        try {
            socket = new Socket("localhost", 45371);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            playerID = in.readInt();
            System.out.println("You are Player #"+playerID);
            if (playerID == 1) {
                System.out.println("Waiting for Player #1 to connect..");  
            }
            rfsRunnable = new ReadFromServer(in);
            wtsRunnable = new WriteToServer(out);

            rfsRunnable.waitForStartMessage();
        } catch (IOException e) {
            System.out.println("IOException from connectToServer");
        }
    }
    private void handleKey(int keyCode, boolean value){
        switch (keyCode) {
            case KeyEvent.VK_UP:
                up = value;
                break;
            case KeyEvent.VK_DOWN:
                down = value;
                break;
            case KeyEvent.VK_LEFT:
                left = value;
                break;
            case KeyEvent.VK_RIGHT:
                right = value;
                break;
            default:
                break;
        }
    }

    private void setUpKeyListener(){
        KeyListener kl = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent ke){ handleKey(ke.getKeyCode(), true); }
            @Override
            public void keyReleased(KeyEvent ke){ handleKey(ke.getKeyCode(), false);}
            @Override
            public void keyTyped(KeyEvent ke) {
                // TODO Auto-generated method stub
            }
        };
        contentPane.addKeyListener(kl);
        contentPane.setFocusable(true);
    }

    private class ReadFromServer implements Runnable{
        private DataInputStream datain;

        ReadFromServer(DataInputStream in){
            datain = in;
            System.out.println("Read from server Runnable created");
        }
        @Override
        public void run() {
            try {
                while (true) {
                    if (enemy != null) {
                        enemy.setX(datain.readDouble());
                        enemy.setY(datain.readDouble());
                    }
                
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        System.out.println("InterruptionException from ReadFromServer run()");   
                    }
                }
            } catch (IOException e) {
                System.out.println("IOException from ReadFromServer run()");
            }
        }
        public void waitForStartMessage(){
            try {
               String startMsg = datain.readUTF();
               System.out.println("Message from server : "+startMsg);
               Thread readThread = new Thread(rfsRunnable);
               Thread writeThread = new Thread(wtsRunnable);
               readThread.start();
               writeThread.start();
            } catch (IOException e) {
                System.out.println("IOException from waitForStartMessage ");
            }
        }

    }
    private class WriteToServer implements Runnable{
        private DataOutputStream dataout;

        WriteToServer(DataOutputStream out){
            dataout = out;
            System.out.println("Write to server Runnable created");
        }
        @Override
        public void run() {
            try {
                while (true) {
                    if (me != null) {
                        dataout.writeDouble(me.getX());
                        dataout.writeDouble(me.getY());
                        dataout.flush();
                    }
                  
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        System.out.println("InterruptionException from WriteToServer run()");   
                    }
                }
            } catch (IOException e) {
                System.out.println("IOException from WriteToServer run()");
            }
        }
    }


    public static void main(String[] args) {
        PlayerFrame pf = new PlayerFrame(600, 500);
        pf.connectToServer();
        pf.setUpGUI();
    }
}
