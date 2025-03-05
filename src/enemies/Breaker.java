package enemies;

import java.awt.Color;

public class Breaker extends Enemy{

    public Breaker(int x, int y, int health, float fireRate) {
        super(x, y, health, fireRate, new Color(0, 100, 0));
    }

    @Override
    public void attack() {
       
    }

}
