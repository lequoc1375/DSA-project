package enemies;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import static main.GamePanel.TILE_SIZE;

public abstract class Enemy {
    protected int x, y;
    protected int health;
    protected float fireRate;
    protected float spawnDelay;
    protected Color color;
    private double angle = 0; 

    public Enemy(int x, int y, int health, float fireRate, Color color) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.fireRate = fireRate;
        this.spawnDelay = spawnDelay;
        this.color = color;
    }

    public abstract void attack();

    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            die();
        }
    }

    public void die() {
        System.out.println("Enemy bị tiêu diệt!");
    }

    public void render(Graphics g) {
        int centerX = x * TILE_SIZE + TILE_SIZE / 2;
        int centerY = y * TILE_SIZE + TILE_SIZE / 2;
        int radius = TILE_SIZE / 2;

        int[] xPoints = new int[5];
        int[] yPoints = new int[5];

        for (int i = 0; i < 5; i++) {
            double currentAngle = Math.toRadians(72 * i + angle - 90);
            xPoints[i] = centerX + (int) (radius * Math.cos(currentAngle));
            yPoints[i] = centerY + (int) (radius * Math.sin(currentAngle));
        }

        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, 5);
    }

    public void update() {
        angle += 2; 
        if (angle >= 360) {
            angle -= 360;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getBound() {
        return new Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
}
