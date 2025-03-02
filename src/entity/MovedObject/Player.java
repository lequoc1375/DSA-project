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

    public void draw(Graphics g, int size) {
        g.setColor(Color.BLUE);
        g.fillRect(position.x *16,position.y *16, 8, 8);
    }

    
}

