package view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ChatContainer extends JPanel{
    
    public ChatContainer(){
        this.setBackground(SideMenu.color);
        this.setLayout(new BorderLayout(10,0));

        JTextArea input = new JTextArea();
        input.setRows(10); // Set the number of rows

        JPanel message = new JPanel();

        JScrollPane ms = new JScrollPane(message);
        JScrollPane in = new JScrollPane(input);

        
        this.add(ms);
        this.add(in,BorderLayout.SOUTH);
    }
}
