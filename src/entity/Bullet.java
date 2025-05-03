package entity;
import java.awt.*;
import main.GamePanel;

public class Bullet {
    private double x, y;
    private double dx, dy;
    private static final int SIZE = 6;
    private static final int SPEED = 10;
    private static final int TILE_SIZE = 16;
    private static final int ROWS = GamePanel.ROWS;
    private static final int COLS = GamePanel.COLS;

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

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillOval((int) (x - SIZE / 2.0), (int) (y - SIZE / 2.0), SIZE, SIZE);
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, SIZE, SIZE);
    }

}
