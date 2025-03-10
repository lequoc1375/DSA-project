package enemies;

import java.awt.Color;

public class Shotgun extends Enemy{

    public Shotgun(int x, int y, int health, float fireRate) {
        super(x, y, health, fireRate, new Color(204, 153, 0));
    }

    @Override
    public void attack() {

    }

}
