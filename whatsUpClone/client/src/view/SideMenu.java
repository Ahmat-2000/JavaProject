package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class SideMenu extends JPanel{
    private JButton groupe , user, admin;
    public static final Color color = new Color(245, 245, 245);
    public SideMenu(){
        this.setBorder(BorderFactory.createLineBorder(color, 1,true));
        // this.setPreferredSize(new Dimension(150,40));
        this.setBackground(color);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        user = createStyledButton("User");
        admin = createStyledButton("Admin");
        groupe = createStyledButton("Groupe");

        this.add(groupe);
        this.add(user);
        this.add(admin);
        for (int i = 0; i < 40; i++) {
        this.add(createStyledButton("Btn "+i));
        this.revalidate();
        }
    }
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150,40));
        button.setBackground(new Color(59, 89, 182));
        button.setFocusPainted(false); // Remove the focus border
        button.setFont(new Font("Arial", Font.BOLD, 14));
        
        return button;
    }
}
