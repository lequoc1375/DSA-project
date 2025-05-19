package entity.MovedObject;

import enemies.EMPDisabler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
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
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int outerRadius = 8;
        int innerRadius = 6;
    
        switch (this.getLevel()) {
            case 1:
                g.setColor(new Color(255, 255, 250));
                break;
            case 2:
                g.setColor(new Color(50, 205, 50));
                break;
            case 3:
                g.setColor(new Color(30, 144, 255));
                break;
            case 4:
                g.setColor(new Color(147, 112, 219));
                break;
            case 5:
                g.setColor(new Color(255, 215, 0));
                break;
            case 6:
                g.setColor(new Color(220, 20, 60));
                break;
        }
    
        g.fillOval(x - outerRadius / 2, y - outerRadius / 2, outerRadius, outerRadius);
    
        g.setColor(color);
        g.fillOval(x - innerRadius / 2, y - innerRadius / 2, innerRadius, innerRadius);
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
