package entity.MovedObject;

import manager.MoveManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PurpleAllies extends Allies {
    private MoveManager player;
    public static boolean isBlessing = false;
    public boolean isCooldown = false;
    private boolean isBlessingActive = false;
    private Timer cooldownTimer;
    private Timer blessingTimer;
    private int blessingDuration;
    private int cooldownDuration;

    public PurpleAllies(int x, int y, MoveManager player) {
        super(x, y, new Color(128, 0, 128)); 
        this.player = player;
        applyUpgradeStats();
    }

    private void applyUpgradeStats() {
        switch (this.getLevel()) {
            case 1:
                blessingDuration = 3000;
                cooldownDuration = 6000;
                break;
            case 2:
                blessingDuration = 4000;
                cooldownDuration = 5000;
                break;
            case 3:
                blessingDuration = 5000;
                cooldownDuration = 4000;
                break;
            case 4:
                blessingDuration = 6000;
                cooldownDuration = 3000;
                break;
            case 5:
                blessingDuration = 7000;
                cooldownDuration = 2000;
                break;
            case 6:
                blessingDuration = 10000;
                cooldownDuration = 1000;
                break;
            default:
                blessingDuration = 3000;
                cooldownDuration = 6000;
        }
    }

    public void bless() {
        if (isCooldown) return;
        isBlessing = true;
        blessingTimer = new Timer(blessingDuration*10, e -> isBlessing = false);
        blessingTimer.setRepeats(false);
        blessingTimer.start();

        startCooldown();
    }

    private void startCooldown() {
        isCooldown = true;

        cooldownTimer = new Timer(cooldownDuration, (ActionEvent event) -> {
            isCooldown = false;
        });
        cooldownTimer.setRepeats(false);
        cooldownTimer.start();
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        super.draw(g, x, y);

        if (isBlessing) {
            g.setColor(new Color(0, 255, 127));
            int playerCenterX = player.getPixelPosition().x;
            int playerCenterY = player.getPixelPosition().y;
            int radius = 16;

            g.drawOval(playerCenterX - radius / 2, playerCenterY - radius / 2, radius, radius);
        }
    }

    @Override
    public void update() {
        super.update();
        applyUpgradeStats();
    }
}
