package view;

public class Main {
    
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    public static void main(String[] args) {
        SideMenu menu = new SideMenu();
        ChatContainer chat = new ChatContainer();
        Screen window = new Screen("Chat App", WIDTH, HEIGHT,menu,chat);
        window.setVisible(true);
    }
}
