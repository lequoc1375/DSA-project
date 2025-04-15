package entity.MovedObject;

import controller.MouseHandler;
import manager.MoveManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

import static controller.MouseHandler.mouseX;
import static controller.MouseHandler.mouseY;

public class OrangeAllies extends Allies {
    private boolean isTeleportCooldown = false;
    private Timer teleportCooldownTimer;
    private final double teleportRange = 120.0;

    private MoveManager player;

    public OrangeAllies(int x, int y, MoveManager player) {
        super(x, y, new Color(255, 165, 0));
        this.player = player;
    }

    public void useSkill() {
        if (isTeleportCooldown) return;

        // Lấy tọa độ trung tâm của player (theo pixel)
        Point2D.Float playerPos = player. getPixelPos2D();
        float playerCenterX = playerPos.x;
        float playerCenterY = playerPos.y;


        float dx = mouseX - playerCenterX;
        float dy = mouseY - playerCenterY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance == 0) return;

        double targetX, targetY;

        if (distance <= teleportRange) {
            targetX = mouseX;
            targetY = mouseY;
        } else {

            double dirX = dx / distance;
            double dirY = dy / distance;

            targetX = playerCenterX + dirX * teleportRange;
            targetY = playerCenterY + dirY * teleportRange;
        }


        player.setPixelPos(new Point2D.Float((float) targetX, (float) targetY));
        this.x = (int) targetX;
        this.y = (int) targetY;

        System.out.println("Teleported to: " + targetX + ", " + targetY);

        startTeleportCooldown();
    }

    private void startTeleportCooldown() {
        isTeleportCooldown = true;
        teleportCooldownTimer = new Timer(3000, e -> isTeleportCooldown = false);
        teleportCooldownTimer.setRepeats(false);
        teleportCooldownTimer.start();
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        super.draw(g, x, y);

    }

    @Override
    public void update() {
        super.update();
    }
}
