package entity;

import java.awt.*;
import java.util.List;

public class Turret {
    private int x, y;
    private List<Bullet> bullets;

    public Turret(int x, int y, List<Bullet> bullets) {
        this.x = x;
        this.y = y;
        this.bullets = bullets;
    }

    public void fireBullet(Point playerPos) {
        if (playerPos == null) return;

        double dx = playerPos.x - x;
        double dy = playerPos.y - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance == 0) return;

        dx /= distance;
        dy /= distance;

        bullets.add(new Bullet(new Point(x, y), dx, dy));
    }

    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(x * 16, y * 16, 16, 16);
    }

    public int getX() { return x; }
    public int getY() { return y; }
}

