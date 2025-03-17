package entity.MovedObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

public  class Allies  implements ObjectCanMove{
    private Point posAllies;
    private Color color;
    public static final float HITBOX_SIZE = 1.2f;
    public boolean skillActive = true;
    public Allies(int x, int y, Color color) {
        Random random = new Random();
        this.posAllies = new Point(x, y); 
        this.color = color;
        
    }
    @Override
    public Point getPosition() {
        return posAllies;
    }

    @Override
    public void setPosition(Point newPosition) {
        this.posAllies = newPosition;
    }

    public void setWeaponDisabled(boolean disabled) {
        skillActive = disabled;
    }

    public int getX() {
        return posAllies.x;
    }

    public int getY() {
        return posAllies.y;
    }

    public int getWidth() {
        return 8;
    }

    public int getHeight() {
        return 8;
    }

    public void draw(Graphics g, int x, int y) {
        g.setColor(color);
        g.fillOval(x-4, y-4, 8, 8);
    }

    public Color getColor() {
        return color;
    }

    
    
   
}
