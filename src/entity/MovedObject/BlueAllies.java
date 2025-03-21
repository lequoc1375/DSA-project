package entity.MovedObject;

import entity.Bullet;
import enemies.Enemy;
import manager.EnemiesManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import static main.GamePanel.TILE_SIZE;

public class BlueAllies extends Allies {
    private List<Bullet> allyBullets;

    private int attackRange = 150;
    private boolean isAttacking = false;
    private Timer attackTimer = null;

    public BlueAllies(int x, int y) {
        super(x, y, new Color(30, 144, 255));
        this.allyBullets = new ArrayList<>();
    }

    private Enemy getNearestEnemy() {
        Enemy nearestEnemy = null;
        double minDistance = attackRange;

        for (Enemy enemy : EnemiesManager.getEnemies()) {
            int enemyCenterX = enemy.getX() * TILE_SIZE + TILE_SIZE / 2;
            int enemyCenterY = enemy.getY() * TILE_SIZE + TILE_SIZE / 2;
            int allyCenterX = this.x * TILE_SIZE + TILE_SIZE / 2;
            int allyCenterY = this.y * TILE_SIZE + TILE_SIZE / 2;

            double dx = enemyCenterX - allyCenterX;
            double dy = enemyCenterY - allyCenterY;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance <= attackRange && (nearestEnemy == null || distance < minDistance)) {
                nearestEnemy = enemy;
                minDistance = distance;
            }
        }
        return nearestEnemy;
    }

    public void attack() {
        isAttacking = true;
        attackTimer = new Timer(2000, (ActionEvent e) -> {
            Enemy target = getNearestEnemy();
            System.out.println("Target: " + target);
            if (target != null) {
                int startX = getX();
                int startY = getY();
                Point startPos = new Point(startX, startY);

                int targetX = target.getX();
                int targetY = target.getY();

                System.out.println("Ally Bullet Start Pos: (" + startX + ", " + startY + ")");
                System.out.println("Ally Bullet Target Pos: (" + targetX + ", " + targetY + ")");

                double dx = (targetX - startX) * TILE_SIZE + TILE_SIZE / 2.0;
                double dy = (targetY - startY) * TILE_SIZE + TILE_SIZE / 2.0;
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance != 0) {
                    dx /= distance;
                    dy /= distance;
                }

                double bulletSpeed = 1.2;
                double vx = dx * bulletSpeed;
                double vy = dy * bulletSpeed;

                Bullet bullet = new Bullet(startPos, vx, vy);
                allyBullets.add(bullet);
                System.out.println("Firing bullet");
            }
            isAttacking = false;
        });
        attackTimer.setRepeats(false);
        attackTimer.start();
    }

    @Override
    public void update() {
        super.update();

        if (getNearestEnemy() != null && !isAttacking) {
            attack();
        }


        List<Bullet> toRemove = new ArrayList<>();
        for (Bullet bullet : allyBullets) {
            bullet.move();
            if (bullet.isOutOfBounds()) {
                toRemove.add(bullet);
            }
        }
        allyBullets.removeAll(toRemove);
    }


    @Override
    public void draw(Graphics g, int x, int y) {
        super.draw(g, x, y);
        for (Bullet bullet : allyBullets) {
            bullet.draw(g);
        }
    }
}
