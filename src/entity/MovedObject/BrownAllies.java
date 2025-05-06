package entity.MovedObject;

import manager.MoveManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BrownAllies extends Allies {
    private boolean isActive = false;
    private boolean isCooldown = false;
    private Timer cooldownTimer;
    private Timer shieldDurationTimer;
    private MoveManager player;
    private Player playerObject;
    private int shieldDuration;
    private int cooldown;

    public BrownAllies(int x, int y, MoveManager playerMove, Player player) {
        super(x, y, new Color(139, 69, 19));
        this.player = playerMove;
        player.setBrownAllies(this);
        applyUpgradeStats();
    }

    private void applyUpgradeStats() {
        switch (this.getLevel()) {
            case 1:
                shieldDuration = 5000;
                cooldown = 3000;
                break;
            case 2:
                shieldDuration = 10000;
                cooldown = 2500;
                break;
            case 3:
                shieldDuration = 15000;
                cooldown = 2000;
                break;
            case 4:
                shieldDuration = 20000;
                cooldown = 1500;
                break;
            case 5:
                shieldDuration = 25000;
                cooldown = 1000;
                break;
            case 6:
                shieldDuration = 30000;
                cooldown = 1000;
                break;
            default:
                shieldDuration = 5000;
                cooldown = 3000;
        }
    }

    @Override
    public void setWeaponDisabled(boolean disabled) {
        super.setWeaponDisabled(disabled);
        if (disabled && isActive) {
            breakShieldEarly();
        }
    }

    public void useSkill() {
        if (isEmpDisabled()) {
            System.out.println("Shield skill disabled by EMP!");
            return;
        }
        if (!isCooldown && !isActive) {
            isActive = true;
            shieldDurationTimer = new Timer(shieldDuration, (ActionEvent e) -> deactivateShield());
            shieldDurationTimer.setRepeats(false);
            shieldDurationTimer.start();
        } else if (isCooldown) {
            System.out.println("Shield is on cooldown!");
        } else {
            System.out.println("Shield already active!");
        }
    }

    private void deactivateShield() {
        isActive = false;
        startCooldown();
    }

    private void startCooldown() {
        isCooldown = true;
        cooldownTimer = new Timer(cooldown, (ActionEvent e) -> isCooldown = false);
        cooldownTimer.setRepeats(false);
        cooldownTimer.start();
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        super.draw(g, x, y);
        if (isActive) {
            g.setColor(Color.BLUE);
            Point p = player.getPixelPosition();
            int cx = p.x, cy = p.y, r = 20;
            g.drawOval(cx - r/2, cy - r/2, r, r);
        }
    }

    @Override
    public void update() {
        super.update();
        applyUpgradeStats();
    }

    public void breakShieldEarly() {
        if (isActive) {
            shieldDurationTimer.stop();
            deactivateShield();
            System.out.println("Shield broken");
        }
    }

    public boolean isShieldActive()    { return isActive; }
    public boolean isSkillOnCooldown() { return isCooldown; }
}
