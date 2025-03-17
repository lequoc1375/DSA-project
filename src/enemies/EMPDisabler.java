package enemies;

import entity.MovedObject.Allies;
import entity.MovedObject.Player;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.Timer;

public class EMPDisabler extends Enemy {
    private Timer activationTimer;
    private Timer cooldownTimer;
    private boolean empActive = false;
    private final int empRadius = 400; 
    private Player player;
    private List<Allies> allies;
    private int width = 8, height = 8;
   
    private final int shieldDiameter = 350;
    private final int shieldRadius = shieldDiameter / 2;

    public EMPDisabler(int x, int y, int health, float fireRate, Player player) {
        super(x, y, health, fireRate, new Color(0, 191, 255)); 
        this.player = player;
        this.allies = allies;
        startCooldown(); 
    }

    @Override
    public void update() {
        super.update();
        attack();
    }

    @Override
    public void attack() {
        if (empActive) {
            applyEMPEffect();
        } else {
            player.setEmpDisabled(false);
            for (Allies ally : allies) {
                ally.setWeaponDisabled(false);
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        Graphics2D g2d = (Graphics2D) g;
        if (empActive) {
            g2d.setColor(new Color(0, 191, 255, 100));
            g2d.drawOval(x*16 - empRadius / 2, y*16 - empRadius / 2, empRadius, empRadius);
            drawLightningEffect(g2d, x*16 + width / 2, y*16 + height / 2, empRadius / 2);
        }
        
       
        if (player.isEmpDisabled()) {
            int playerCenterX = player.getX()*16 + player.getWidth() / 2;
            int playerCenterY = player.getY()*16 + player.getHeight() / 2;
            g2d.setColor(new Color(0, 191, 255, 100));
            g2d.drawOval(playerCenterX - shieldRadius, playerCenterY - shieldRadius, shieldDiameter, shieldDiameter);
            drawLightningEffect(g2d, playerCenterX, playerCenterY, shieldRadius);
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

    
    private void applyEMPEffect() {
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        
     
        int playerCenterX = player.getX()*16 + player.getWidth() / 2;
        int playerCenterY = player.getY()*16 + player.getHeight() / 2;
        double distancePlayer = Point.distance(centerX, centerY, playerCenterX, playerCenterY);
        if (distancePlayer <= empRadius) {
            player.setEmpDisabled(true);
        }

       
        for (Allies ally : allies) {
            int allyCenterX = ally.getX() + ally.getWidth() / 2;
            int allyCenterY = ally.getY() + ally.getHeight() / 2;
            double distanceAlly = Point.distance(centerX, centerY, allyCenterX, allyCenterY);
            if (distanceAlly <= empRadius) {
                ally.setWeaponDisabled(true);
            }
        }
    }


    private void activateEMP() {
        empActive = true;
        activationTimer = new Timer(5000, (ActionEvent e) -> {
            empActive = false;
            activationTimer.stop();
            startCooldown();
        });
        activationTimer.setRepeats(false);
        activationTimer.start();
    }

    
    private void startCooldown() {
        cooldownTimer = new Timer(15000, (ActionEvent e) -> {
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
