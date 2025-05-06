package entity.MovedObject;

import enemies.EMPDisabler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

public  class Allies  implements ObjectCanMove{
    private Point posAllies;
    private Color color;
    public static final float HITBOX_SIZE = 1.2f;
    public boolean skillActive = false;
    public int x, y;
    public Allies(int x, int y, Color color) {
        this.posAllies = new Point(x, y); 
        this.color = color;
        this.x = x;
        this.y = y;
        
    }
    @Override
    public Point getPosition() {
        return posAllies;
    }

    @Override
    public void setPosition(Point newPosition) {

        this.posAllies = newPosition;
        this.x = newPosition.x;
        this.y = newPosition.y;
    }

    public void setWeaponDisabled(boolean disabled) {
        skillActive = disabled;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return 8;
    }

    public int getHeight() {
        return 8;
    }

    public void draw(Graphics g, int x, int y) {
        switch(this.getLevel()) {
            case 1:
                g.setColor(new Color(253,253,255));
                break;
            case 2:
                g.setColor(new Color(69,203,103));
                break;
            case 3:
                g.setColor(new Color(51,161,247));
                break;
            case 4:
                g.setColor(new Color(163,67,255));
                break;
            case 5:
                g.setColor(new Color(253,214,54));
                break;
            case 6:
                g.setColor(new Color(239,34,38));  
                break;  
        }
        g.fillOval(x-4, y-4, 8, 8);
        g.setColor(color);
        g.fillOval(x-3, y-3, 6, 6);
    }

    public Color getColor() {
        return color;
    }
    public void update() {

    }

    public boolean isEmpDisabled() {
        return skillActive;
    }

    private EMPDisabler empSource;

    public void setEmpSource(EMPDisabler source) {
        this.empSource = source;
    }

    public EMPDisabler getEmpSource() {
        return empSource;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }

    private int level = 1;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
