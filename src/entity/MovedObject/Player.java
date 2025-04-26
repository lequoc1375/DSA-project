package entity.MovedObject;

import enemies.EMPDisabler;

import java.awt.*;

public class Player implements ObjectCanMove {
    private Point position;
    private double health;
    public static float speed = 120; 
    private boolean isSlowed = false;
    private boolean  skillActive = true;
    private Rectangle bound;
    public Player(int x, int y) {
        this.position = new Point(x, y);
        bound = new Rectangle();
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point newPosition) {
        this.position = newPosition;
    }

    public int getX() { return position.x; }
    public int getY() { return position.y; }
    public float getSpeed() { return speed; }
    public void setSpeed(float newSpeed) { this.speed = newSpeed; }

    public void setEmpDisabled(boolean disabled) {
        this.skillActive = disabled;
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
