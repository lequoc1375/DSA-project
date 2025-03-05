package enemies;

import java.awt.Color;

public class Sniper extends Enemy{

    public Sniper(int x, int y, int health, float fireRate) {
        super(x, y, health, fireRate, new Color(139,0,0));
    }

    @Override
    public void attack() {
      
    }

}
