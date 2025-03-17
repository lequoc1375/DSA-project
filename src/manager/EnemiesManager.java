package manager;

import enemies.Enemy;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EnemiesManager {

    private static List<Enemy> enemies = new ArrayList<>();
    public EnemiesManager() {

    }

    public void update() {
        for (Enemy enemy : enemies) {
            enemy.update();
        }
    }

    public void render(Graphics g) {
        for (Enemy enemy : enemies) {
            enemy.render(g);
        }
    }

    public void clear() {
        enemies.clear();
    }

    public void add(Enemy enemy) {
        enemies.add(enemy);
    }

    public static List<Enemy> getEnemies() {
        return enemies;
    }

}
