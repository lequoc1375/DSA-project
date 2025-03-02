package entity.MovedObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

public class Allies  implements ObjectCanMove{
    private int x, y;
    private Point posAllies;
    private Color color;
    public static final float HITBOX_SIZE = 1.2f;
    
    public Allies(int row, int col) {
        Random random = new Random();
        this.posAllies = new Point(random.nextInt(row-1)+0, random.nextInt(col-1)+0); 
        this.color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
    @Override
    public Point getPosition() {
        return posAllies;
    }

    @Override
    public void setPosition(Point newPosition) {
        this.posAllies = newPosition;
    }



   
}
