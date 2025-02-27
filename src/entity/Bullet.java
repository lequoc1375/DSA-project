package entity;
import java.awt.*;

public class Bullet {
    private double x, y;
    private double dx, dy;
    private static final int SIZE = 6;
    private static final int SPEED = 10;
    private static final int TILE_SIZE = 16;
    private static final int ROWS = 40;
    private static final int COLS = 40;

    public Bullet(Point startPos, double dx, double dy) {
        this.x = startPos.x * TILE_SIZE + TILE_SIZE / 2.0;
        this.y = startPos.y * TILE_SIZE + TILE_SIZE / 2.0;
        this.dx = dx * SPEED;
        this.dy = dy * SPEED;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public boolean isOutOfBounds() {
        return x < 0 || x >= COLS * TILE_SIZE || y < 0 || y >= ROWS * TILE_SIZE;
    }

    public boolean hitsPlayer(Point playerPos) {
        double px = playerPos.x * TILE_SIZE + TILE_SIZE / 2.0;
        double py = playerPos.y * TILE_SIZE + TILE_SIZE / 2.0;
        return Math.hypot(x - px, y - py) < SIZE / 2.0;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillOval((int) (x - SIZE / 2.0), (int) (y - SIZE / 2.0), SIZE, SIZE);
    }
}
