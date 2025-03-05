package enemies;

import java.awt.Color;

public class Boomer extends Enemy{

    public Boomer(int x, int y, int health, float fireRate) {
        super(x, y, health, fireRate, new Color(199, 21, 133));
    }

    @Override
    public void attack() {
      
    }

}
