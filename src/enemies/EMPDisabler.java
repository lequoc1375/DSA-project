package enemies;

import entity.MovedObject.Allies;
import entity.MovedObject.Player;
import entity.MovedObject.PurpleAllies;
import manager.MoveManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.Supplier;
import javax.swing.Timer;

public class EMPDisabler extends Enemy {
    private Timer activationTimer;
    private Timer cooldownTimer;
    private boolean empActive = false;
    private final int empRadius = 200;
    private final int shieldDiameter = 30;
    private final int shieldRadius = shieldDiameter / 2;

    private MoveManager player;
    private Player player1;
    private Supplier<List<Allies>> alliesSupplier;
    private Supplier<List<MoveManager>> alliesManagerSupplier;
    private int width = 8, height = 8;

    public EMPDisabler(int x, int y, int health, float fireRate, MoveManager player, Supplier<List<Allies>> alliesSupplier, Player player1, Supplier<List<MoveManager>> alliesManagerSupplier) {
        super(x, y, health, fireRate, new Color(28, 59, 129));
        this.player = player;
        this.alliesSupplier = alliesSupplier;
        this.player1 = player1;
        this.alliesManagerSupplier = alliesManagerSupplier;
        startCooldown();
    }

    @Override
    public void update() {
        super.update();
        attack();
    }

    @Override
    public void attack() {
        List<Allies> allies = alliesSupplier.get();
        if (empActive) {
            applyEMPEffect(allies, alliesManagerSupplier.get());
        } else {
            if (player1.getEmpSource() == this) {
                player1.setEmpDisabled(false);
                player1.setEmpSource(null);
            }
            for (Allies ally : allies) {
                if (ally.getEmpSource() == this) {
                    ally.setWeaponDisabled(false);
                    ally.setEmpSource(null);
                }
            }
        }
    }

    private void applyEMPEffect(List<Allies> allies, List<MoveManager> alliesManager) {

        if (PurpleAllies.isBlessing) {
            // Gỡ EMP cho player
            player1.setEmpDisabled(false);
            // Gỡ EMP cho tất cả allies
            for (Allies ally : allies) {
                ally.setWeaponDisabled(false);
            }
            return;
        }
        int centerX = x * 16 + width / 2;
        int centerY = y * 16 + height / 2;

        int playerCenterX = player.getPixelPosition().x;
        int playerCenterY = player.getPixelPosition().y;
        double distancePlayer = Point.distance(centerX, centerY, playerCenterX, playerCenterY);

        if (distancePlayer <= empRadius) {
            player1.setEmpDisabled(true);
            player1.setEmpSource(this);
        } else if (player1.getEmpSource() == this) {
            player1.setEmpDisabled(false);
            player1.setEmpSource(null);
        }

        int minSize = Math.min(allies.size(), alliesManager.size());
        for (int i = 0; i < minSize; i++) {
            Allies ally = allies.get(i);
            MoveManager allyMove = alliesManager.get(i);

            int allyCenterX = allyMove.getPixelPosition().x;
            int allyCenterY = allyMove.getPixelPosition().y;
            double distanceAlly = Point.distance(centerX, centerY, allyCenterX, allyCenterY);

            if (distanceAlly <= empRadius) {
                ally.setWeaponDisabled(true);
                ally.setEmpSource(this);
            } else if (ally.getEmpSource() == this) {
                ally.setWeaponDisabled(false);
                ally.setEmpSource(null);
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = x * 16 + width;
        int centerY = y * 16 + height;

        if (empActive) {
            g2d.setColor(new Color(28, 59, 129));
            g2d.drawOval(centerX - empRadius, centerY - empRadius, empRadius * 2, empRadius * 2);
            drawLightningEffect(g2d, centerX, centerY, empRadius);
        }

        if (player1.isEmpDisabled() && player1.getEmpSource() == this) {
            int playerCenterX = player.getPixelPosition().x;
            int playerCenterY = player.getPixelPosition().y;
            g2d.setColor(new Color(28, 59, 129));
            g2d.drawOval(playerCenterX - shieldRadius, playerCenterY - shieldRadius, shieldDiameter, shieldDiameter);
            drawLightningEffect(g2d, playerCenterX, playerCenterY, shieldRadius);
        }

        List<Allies> allies = alliesSupplier.get();
        List<MoveManager> alliesManager = alliesManagerSupplier.get();

        int minSize = Math.min(allies.size(), alliesManager.size());
        for (int i = 0; i < minSize; i++) {
            Allies ally = allies.get(i);
            MoveManager allyMove = alliesManager.get(i);

            if (ally.isEmpDisabled() && ally.getEmpSource() == this) {
                int allyCenterX = allyMove.getPixelPosition().x;
                int allyCenterY = allyMove.getPixelPosition().y;
                g2d.setColor(new Color(28, 59, 129));
                g2d.drawOval(allyCenterX - shieldRadius, allyCenterY - shieldRadius, shieldDiameter, shieldDiameter);
                drawLightningEffect(g2d, allyCenterX, allyCenterY, shieldRadius);
            }
        }

    }

    private void drawLightningEffect(Graphics2D g2d, int centerX, int centerY, int maxLength) {
        g2d.setStroke(new BasicStroke(2));
        for (int i = 0; i < 5; i++) {
            double angle = Math.random() * 2 * Math.PI;
            int endX = centerX + (int)(maxLength * Math.cos(angle));
            int endY = centerY + (int)(maxLength * Math.sin(angle));
            g2d.drawLine(centerX, centerY, endX, endY);
        }
    }

    private void activateEMP() {
        empActive = true;
        activationTimer = new Timer(20000, (ActionEvent e) -> {
            empActive = false;
            activationTimer.stop();
            startCooldown();
        });
        activationTimer.setRepeats(false);
        activationTimer.start();
    }

    private void startCooldown() {
        cooldownTimer = new Timer(5000, (ActionEvent e) -> {
            cooldownTimer.stop();
            activateEMP();
        });
        cooldownTimer.setRepeats(false);
        cooldownTimer.start();
    }

    public boolean isEMPActive() {
        return empActive;
    }
}
