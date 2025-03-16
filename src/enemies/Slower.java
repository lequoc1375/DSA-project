package enemies;

import entity.MovedObject.Player;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class Slower extends Enemy {
    private Timer effectTimer;
    private boolean isSlowed = false;
    private boolean isOnCooldown = false;
    private Player player;

    public Slower(int x, int y, int health, float fireRate, Player player) {
        super(x, y, health, fireRate, new Color(204, 153, 0));
        this.player = player;
    }

    @Override
public void attack() {
    if (isOnCooldown) return; 

    isSlowed = true;
    isOnCooldown = true; 
    float originSpeed = Player.speed;

    System.out.println("Before slow: " + player.getSpeed());
    player.setSpeed(80);
    System.out.println("After slow: " + player.getSpeed());

    effectTimer = new Timer(6000, (ActionEvent e) -> {
        player.setSpeed(originSpeed); // Khôi phục tốc độ
        isSlowed = false;
        System.out.println("Speed restored: " + player.getSpeed());

        // Bắt đầu cooldown
        new Timer(9000, (ActionEvent ev) -> {
            isOnCooldown = false; // Reset cooldown
            System.out.println("Cooldown ended, can attack again.");
        }).start();
    });

    effectTimer.setRepeats(false);
    effectTimer.start();
}

    @Override
    public void update() {
        super.update();
        attack();
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        
        
        if (isSlowed) {
            g.setColor(new Color(255, 153, 51, 150));
            g.fillOval(player.getX() - 3, player.getY() - 3, 8 + 3, 8 + 3);
        }
    }
}
