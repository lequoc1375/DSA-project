package entity.MovedObject;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

public class Allies  implements ObjectCanMove{
    private int x, y;
    private Point posAllies;
    private Color color;
    public static final float HITBOX_SIZE = 1.2f;
    
    public Allies(int x, int y) {
        Random random = new Random();
        this.posAllies = new Point(x, y); 
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
