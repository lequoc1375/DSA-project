package entity.MovedObject;

import enemies.EMPDisabler;
import manager.AlliesManager;
import scenes.Playing;

import java.awt.*;

public class Player implements ObjectCanMove {
    private Point position;
    private double health;
    public static float speed = 120;
    private boolean isSlowed = false;
    private boolean  skillActive = true;
    private Rectangle bound;
    private BrownAllies brownAllies;

    private AlliesManager alliesManager;
    private boolean shieldActive = false;
    private boolean isDead = false;
    private Playing playing;
    
    private int playerHealth = 1;

    public Player(int x, int y, Playing playing) {
        this.position = new Point(x, y);
        bound = new Rectangle();
        this.playing = playing;
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
    public void playerIsHit() {
        if (shieldActive) {
            breakShield();

        } else if (playerHealth > 1) {
            playerHealth--;
        } else {
            setPlayerHealth(0);
            die();
        }
    }

    private void breakShield() {
        shieldActive = false;
        System.out.println("Shield is broken!");
    }

    private void die() {
        isDead = true;
        System.out.println("Player die");
    }


    public void setBrownAllies(BrownAllies brownAllies) {
        this.brownAllies = brownAllies;
    }

    public void setAlliesManager(AlliesManager manager) {
        this.alliesManager = manager;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }
}
