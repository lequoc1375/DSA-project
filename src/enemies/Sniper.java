package enemies;

import entity.Bullet;
import entity.MovedObject.Player;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import static main.GamePanel.TILE_SIZE;
import manager.MoveManager;

public class Sniper extends Enemy {
    private MoveManager player;
    private boolean showLaser = false;
    private int laserTargetX;
    private int laserTargetY;
    private int attackRange = 200;
    private boolean isAttacking = false;
    private Timer attackTimer = null;
    private List<Bullet> enemyBullets;
    private Rectangle bulletRect;
    private Player playerObject;
    public Sniper(int x, int y, int health, float fireRate, MoveManager player, Player player1) {
        super(x, y, health, fireRate, new Color(58, 78, 72));
        this.player = player;
        enemyBullets = new ArrayList<>();
        playerObject = player1;
    }

    private boolean isPlayerWithinRange() {
        int sniperCenterX = this.x * TILE_SIZE + TILE_SIZE / 2;
        int sniperCenterY = this.y * TILE_SIZE + TILE_SIZE / 2;
        int playerCenterX = player.getPixelPosition().x;
        int playerCenterY = player.getPixelPosition().y;
        double dx = playerCenterX - sniperCenterX;
        double dy = playerCenterY - sniperCenterY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance <= attackRange;
    }

    @Override
    public void attack() {
        showLaser = true;
        laserTargetX =player.getPixelPosition().x ;
        laserTargetY =player.getPixelPosition().y ;
        isAttacking = true;


        attackTimer = new Timer((int)fireRate, (ActionEvent e) -> {
            if (isPlayerWithinRange()) {
                int startX = this.x ;
                int startY = this.y ;
                Point startPos = new Point(startX, startY);

                int targetX = player.getPixelPosition().x ;
                int targetY = player.getPixelPosition().y ;

                double dx = targetX - (startX * TILE_SIZE + TILE_SIZE / 2.0);
                double dy = targetY - (startY * TILE_SIZE + TILE_SIZE / 2.0);
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance != 0) {
                    dx /= distance;
                    dy /= distance;
                }
                double bulletSpeed = 2;
                double vx = dx * bulletSpeed;
                double vy = dy * bulletSpeed;

                Bullet bullet = new Bullet(startPos, vx, vy);
                enemyBullets.add(bullet);

            } else {

            }
            showLaser = false;
            isAttacking = false;
        });
        attackTimer.setRepeats(false);
        attackTimer.start();
    }

    @Override
    public void update() {
        super.update();

        if (!isPlayerWithinRange()) {
            if (showLaser) {
                showLaser = false;
            }
            if (isAttacking && attackTimer != null) {
                attackTimer.stop();
                isAttacking = false;

            }
        } else {
            if (!isAttacking) {
                attack();
            }
        }

        Iterator<Bullet> iterator = enemyBullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.move();

            if (bullet.getBounds().intersects(player.getBound())) {
                iterator.remove();
                playerObject.playerIsHit();
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        if (showLaser) {
            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int centerX = this.x * TILE_SIZE + TILE_SIZE / 2;
            int centerY = this.y * TILE_SIZE + TILE_SIZE / 2;
            int targetX = player.getPixelPosition().x ;
            int targetY = player.getPixelPosition().y ;

            GradientPaint gp = new GradientPaint(centerX, centerY, new Color(58, 78, 72), targetX, targetY, Color.RED, true);
            g2d.setPaint(gp);

            g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(centerX, centerY, targetX, targetY);
        }

        for (Bullet bullet : enemyBullets) {
            bullet.draw(g);
        }

    }


}
