package entity.MovedObject;

import manager.MoveManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PurpleAllies extends Allies {
    private MoveManager player;
    public static boolean isBlessing = false;
    public boolean isCooldown = false;
    private Timer cooldownTimer;
    private boolean isBlessingActive = false;
    public PurpleAllies(int x, int y, MoveManager player) {
        super(x, y, new Color(128, 0, 128)); // Màu tím
        this.player = player;
    }

    public void bless() {
        if (isCooldown) return;
        isBlessing = true;
        Timer blessingTimer = new Timer(5000, e -> isBlessing = false);
        blessingTimer.setRepeats(false);
        blessingTimer.start();

        startCooldown();
    }

    private void startCooldown() {
        isCooldown = true;

        cooldownTimer = new Timer(1000, (ActionEvent event) -> {
            isCooldown = false;
        });
        cooldownTimer.setRepeats(false);
        cooldownTimer.start();
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        super.draw(g, x, y);

        if (isBlessing) {
            g.setColor(Color.GREEN);
            int playerCenterX = player.getPixelPosition().x;
            int playerCenterY = player.getPixelPosition().y;
            int radius = 16;

            g.drawOval(playerCenterX - radius / 2, playerCenterY - radius / 2, radius, radius);
        }
    }

    @Override
    public void update() {
        super.update();
    }
}
