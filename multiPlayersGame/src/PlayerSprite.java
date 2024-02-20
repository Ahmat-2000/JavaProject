import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class PlayerSprite {
    private double x,y,size;
    private Color color;
    public PlayerSprite(double x, double y, double size, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }
    public void drawSprite(Graphics2D g2d){
        Rectangle2D.Double square = new Rectangle2D.Double(x,y,size,size);
        g2d.setColor(color);
        g2d.fill(square);
    }
    public void  moveH(double n){
        x += n;
    }
    public void  moveV(double n){
        y += n;
    }
    public double getY() {
        return y;
    }
    public double getX() {
        return x;
    }
    public void setX(double n) {
        x = n;
    }
    public void setY(double n) {
        y = n;
    }

}
