package entity.MovedObject;

import controller.MouseHandler;
import main.GamePanel;
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
    public boolean isUseSkill = false;
    private MoveManager player;

    public OrangeAllies(int x, int y, MoveManager player) {
        super(x, y, new Color(255, 165, 0));
        this.player = player;
    }


    public void useSkill() {
        if (isTeleportCooldown) return;


        Point2D.Float playerPos = player.getPixelPos2D();
        float px = playerPos.x, py = playerPos.y;


        float dx = mouseX - px;
        float dy = mouseY - py;
        double distance = Math.hypot(dx, dy);
        if (distance == 0) return;


        double targetX = px + dx / distance * Math.min(distance, teleportRange);
        double targetY = py + dy / distance * Math.min(distance, teleportRange);


        int gridX = (int)(targetX / GamePanel.TILE_SIZE);
        int gridY = (int)(targetY / GamePanel.TILE_SIZE);
        Point newGrid = new Point(gridX, gridY);


        player.teleportTo(newGrid);


        isUseSkill = false;
        isTeleportCooldown = true;
        new Timer(3000, e -> isTeleportCooldown = false).start();
    }


    private void startTeleportCooldown() {
        isUseSkill = false;
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

    public void setUseSkill(boolean useSkill) {
        isUseSkill = useSkill;
    }
}
