package entity.MovedObject;

import manager.MoveManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static main.GamePanel.TILE_SIZE;

public class BrownAllies extends Allies {
    private boolean isActive = false; // Khiên đang bật
    private boolean isCooldown = false; // Có đang trong cooldown không
    private Timer cooldownTimer;
    private Timer shieldDurationTimer;
    private MoveManager player;
    public BrownAllies(int x, int y, MoveManager playerMove) {
        super(x, y, new Color(139, 69, 19));
        this.player = playerMove;
    }

    public void useSkill() {
        if (!isCooldown && !isActive) {
            isActive = true;


            shieldDurationTimer = new Timer(5000, (ActionEvent e) -> {
                deactivateShield();
            });
            shieldDurationTimer.setRepeats(false);
            shieldDurationTimer.start();
        } else if (isCooldown) {
            System.out.println("Skill is on cooldown!");
        } else {
            System.out.println("Shield is already active!");
        }
    }


    private void deactivateShield() {
        isActive = false;
        startCooldown();
    }

    private void startCooldown() {
        isCooldown = true;
        cooldownTimer = new Timer(13000, (ActionEvent e) -> {
            isCooldown = false;
        });
        cooldownTimer.setRepeats(false);
        cooldownTimer.start();
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        super.draw(g, x, y);
        if (isActive) {
            g.setColor(Color.BLUE);
            int playerCenterX = player.getPixelPosition().x ;
            int playerCenterY = player.getPixelPosition().y ;
            int radius = 20;
            g.drawOval(playerCenterX - radius / 2, playerCenterY - radius / 2, radius, radius);

        }
    }

    @Override
    public void update() {
        super.update();
    }


    public void breakShieldEarly() {
        if (isActive) {
            shieldDurationTimer.stop();
            deactivateShield();
        }
    }

    public boolean isShieldActive() {
        return isActive;
    }

    public boolean isSkillOnCooldown() {
        return isCooldown;
    }
}
