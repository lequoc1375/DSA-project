package entity.MovedObject;

import entity.Bullet;
import enemies.Enemy;
import manager.EnemiesManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import static main.GamePanel.TILE_SIZE;

public class BlueAllies extends Allies {
    private List<Bullet> allyBullets;

    private int attackRange = 150;
    private boolean isAttacking = false;
    private Timer attackTimer = null;
    private boolean isEmpDisabled = false;
    public BlueAllies(int x, int y) {
        super(x, y, new Color(30, 144, 255));
        this.allyBullets = new ArrayList<>();
    }

    @Override
    public void setWeaponDisabled(boolean disabled) {
        super.setWeaponDisabled(disabled);
        this.isEmpDisabled = disabled;
         if (disabled) {
             if (attackTimer != null && attackTimer.isRunning()) {
                 attackTimer.stop();
                 isAttacking = false;
             }
         }
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

                double dx = (targetX - startX) * TILE_SIZE + TILE_SIZE / 2;
                double dy = (targetY - startY) * TILE_SIZE + TILE_SIZE / 2;
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
            }
            isAttacking = false;
        });
        attackTimer.setRepeats(false);
        attackTimer.start();
    }

    @Override
    public void update() {
        super.update();

        if (!isEmpDisabled && getNearestEnemy() != null && !isAttacking) {
            attack();
        }

        List<Bullet> toRemove = new ArrayList<>();
        for (Bullet bullet : allyBullets) {
            bullet.move();
            if (bullet.isOutOfBounds()) {
                toRemove.add(bullet);
                continue;
            }

            for (Enemy enemy : EnemiesManager.getEnemies()) {
                if(bullet.getBounds().intersects(new Rectangle( enemy.getX() * TILE_SIZE + TILE_SIZE / 2,  enemy.getY() * TILE_SIZE + TILE_SIZE / 2, TILE_SIZE, TILE_SIZE))) {
                    System.out.println("Enemies is attacked");
                    toRemove.add(bullet);
                    break;
                }
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
