package enemies;

import entity.MovedObject.Player;
import entity.Bullet;
import manager.MoveManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import static main.GamePanel.TILE_SIZE;

public class Sniper extends Enemy {
    private MoveManager player;
    private boolean showLaser = false;
    private int laserTargetX;
    private int laserTargetY;
    private int attackRange = 300;
    private boolean isAttacking = false;
    private Timer attackTimer = null;
    private List<Bullet> enemyBullets;

    public Sniper(int x, int y, int health, float fireRate, MoveManager player) {
        super(x, y, health, fireRate, new Color(139, 0, 0));
        this.player = player;
        enemyBullets = new ArrayList<>();
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


        attackTimer = new Timer(5000, (ActionEvent e) -> {
            System.out.println("Timer triggered!");
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

        for (Bullet bullet : enemyBullets) {
            bullet.move();
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        if (showLaser) {
            Graphics2D g2d = (Graphics2D) g;
            // Thiết lập các Rendering Hints để cải thiện chất lượng vẽ
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int centerX = this.x * TILE_SIZE + TILE_SIZE / 2;
            int centerY = this.y * TILE_SIZE + TILE_SIZE / 2;
            int targetX = player.getPixelPosition().x ;
            int targetY = player.getPixelPosition().y ;

            // Tạo hiệu ứng gradient cho laser
            GradientPaint gp = new GradientPaint(centerX, centerY, Color.RED, targetX, targetY, Color.ORANGE, true);
            g2d.setPaint(gp);

            // Dùng BasicStroke với độ dày 6 và đầu đường cong tròn để vẽ laser mượt mà
            g2d.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(centerX, centerY, targetX, targetY);
        }

        for (Bullet bullet : enemyBullets) {
            bullet.draw(g);
        }
    }


}
