package entity.MovedObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Player implements ObjectCanMove{
    private Point position;

    public Player(int x, int y) {
        this.position = new Point(x, y);
    }
    @Override
    public Point getPosition() {
        return position;
    }
    @Override
    public void setPosition(Point newPosition) {
        this.position = newPosition;
    }
    @Override
    public void draw(Graphics g, int size) {
        g.setColor(Color.BLUE); // Màu của Player hoặc Allies
        Point pos = getPosition();
        int offset = (16 - size) / 2; // Căn giữa trong ô 16px
        g.fillRect(pos.x * 16 + offset, pos.y * 16 + offset, size, size);
    }

    
}

