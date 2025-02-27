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
    public void draw(Graphics g, int tileSize) {
        g.setColor(Color.RED);
        g.fillOval(position.x * tileSize, position.y * tileSize, tileSize, tileSize);
    }

    
}

