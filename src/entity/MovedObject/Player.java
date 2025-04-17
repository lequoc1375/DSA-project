package entity.MovedObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Player implements ObjectCanMove {
    private Point position;
    private double health;
    public static float speed = 120; 
    private boolean isSlowed = false;
    private boolean  skillActive = true;

    public Player(int x, int y) {
        this.position = new Point(x, y);
        this.health = 10;
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
    public double getHealth() { return health; }
    public void setHealth(double newHealth) { this.health = newHealth; }
    public void takeDamage(int damage) { health -= damage; }
    public float getSpeed() { return speed; }
    public void setSpeed(float newSpeed) { this.speed = newSpeed; }

    public void setEmpDisabled(boolean disabled) {
        skillActive = false;
    }

    public int getWidth() {
        return 8;
    }

    public int getHeight() {
        return 8;
    }

    public boolean isEmpDisabled() {
        return skillActive;
    }


}
