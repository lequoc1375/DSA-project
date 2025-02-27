package entity.MovedObject;

import java.awt.Graphics;
import java.awt.Point;

public interface ObjectCanMove {
    void draw(Graphics g, int tileSize);
    Point getPosition();
    void setPosition(Point newPosition);
}
