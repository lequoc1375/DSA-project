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
        g.setColor(color);
        g.fillOval(x-4, y-4, 8, 8);
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
}
