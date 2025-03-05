package manager;

import enemies.Enemy;
import java.util.ArrayList;
import java.util.List;

public class EnemiesManager {

    private List<Enemy> enemies = new ArrayList<>();
    public EnemiesManager(Enemy enemy) {
        enemies.add(enemy);
    }

    public void update() {
        for (Enemy enemy : enemies) {
            enemy.attack();
        }
    }

}
