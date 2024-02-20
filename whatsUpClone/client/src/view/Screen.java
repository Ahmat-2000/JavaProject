package view;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class Screen extends JFrame{
    public Container screenContaineur ;
    public SideMenu sideMenu;
    public ChatContainer chatContainer;
    public Screen(String title, int largeur, int hauteur,SideMenu sideMenu,ChatContainer chatContainer){
        this.setTitle(title);
        this.setSize(largeur, hauteur);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setIconImage(new ImageIcon("client/src/icon.png").getImage());

        this.sideMenu = sideMenu;
        this.chatContainer = chatContainer;
        this.screenContaineur = this.getContentPane();
        this.screenContaineur.setLayout(new BorderLayout());
        this.screenContaineur.add(new JScrollPane(this.sideMenu),BorderLayout.WEST);
        this.screenContaineur.add(this.chatContainer,BorderLayout.CENTER);
        
    }
}
