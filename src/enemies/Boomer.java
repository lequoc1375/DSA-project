package enemies;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;
import entity.Barrier;
import entity.Boom;
import entity.MovedObject.Player;

import static main.GamePanel.*;

public class Boomer extends Enemy {
    private List<Boom> boomList;
    private Timer attackTimer = null;
    private Player player;
    public Boomer(int x, int y, int health, float fireRate,Player player) {
        super(x, y, health, fireRate, new Color(199, 21, 133));
        boomList = new ArrayList<>();
        this.player = player;
    }

    @Override
    public void attack() {
        attackTimer = new Timer(5000, (ActionEvent e) -> {
            System.out.println("Boomer attacking: Dropping bombs!");
            Random random = new Random();
            int totalBombsPerTurn = 3;

            for (int i = 0; i < totalBombsPerTurn; i++) {
                int bombX, bombY;
                Point point;

                do {
                    bombX = random.nextInt(COLS) * TILE_SIZE;
                    bombY = random.nextInt(ROWS) * TILE_SIZE;
                    point = new Point(bombX, bombY);
                } while (Barrier.isObstacle(bombX/TILE_SIZE, bombY/TILE_SIZE));

                Boom boom = new Boom(point);
                boomList.add(boom);
                System.out.println("Bomb dropped at (" + bombX + ", " + bombY + ")");
            }
        });
        attackTimer.setRepeats(false);
        attackTimer.start();


    }

    @Override
    public void update() {
        super.update();

        if (attackTimer == null || !attackTimer.isRunning()) {
            attack();
        }

        for (Boom boom : boomList) {
            Point boomTile = new Point(boom.getX() / TILE_SIZE, boom.getY() / TILE_SIZE);
            if (player.getPosition().equals(boomTile)) {
                boom.triggerExplosion();
            }
        }

    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        for (Boom boom : boomList) {
            boom.draw(g);
        }
    }
}
