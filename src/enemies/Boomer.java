package enemies;

import entity.Barrier;
import entity.Boom;
import entity.MovedObject.Allies;
import entity.MovedObject.Player;
import manager.AlliesManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
import javax.swing.Timer;
import static main.GamePanel.*;

public class Boomer extends Enemy {
    private List<Boom> boomList;
    private List<Boom> pendingBooms;
    private AlliesManager alliesManager;
    private Timer attackTimer = null;
    private Player player;

    public Boomer(int x, int y, int health, float fireRate, Player player, AlliesManager alliesManager) {
        super(x, y, health, fireRate, new Color(199, 21, 133));
        boomList = new ArrayList<>();
        pendingBooms = new ArrayList<>();
        this.player = player;
        this.alliesManager = alliesManager;
    }

    @Override
    public void attack() {
        attackTimer = new Timer(5000, (ActionEvent e) -> {
            Random random = new Random();
            int totalBombsPerTurn = 3;

            for (int i = 0; i < totalBombsPerTurn; i++) {
                int bombX, bombY;
                Point point;

                do {
                    bombX = random.nextInt(COLS) * TILE_SIZE;
                    bombY = random.nextInt(ROWS) * TILE_SIZE;
                    point = new Point(bombX, bombY);
                } while (Barrier.isObstacle(bombX / TILE_SIZE, bombY / TILE_SIZE));

                Boom boom = new Boom(point);
                pendingBooms.add(boom);
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

        if (!pendingBooms.isEmpty()) {
            boomList.addAll(pendingBooms);
            pendingBooms.clear();
        }

        Iterator<Boom> iter = boomList.iterator();
        while (iter.hasNext()) {
            Boom boom = iter.next();
            Point boomTile = new Point(boom.getX() / TILE_SIZE, boom.getY() / TILE_SIZE);

            if (player.getPosition().equals(boomTile)) {
                boom.triggerExplosion();
            }

            for(Allies ally : alliesManager.getAlliesQueue()) {
                if(ally.getPosition().equals(boomTile)) {
                    boom.triggerExplosion();
                }
            }

            if (boom.isExplosionOver()) {
                iter.remove();
                if (player.getPosition().equals(boomTile)) {
                    player.playerIsHit();
                }
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
