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
        if (isOnCooldown)
            return;

        isSlowed = true;
        isOnCooldown = true;
        float originSpeed = Player.speed;

        player.setSpeed(80);

        effectTimer = new Timer(6000, (ActionEvent e) -> {
            player.setSpeed(originSpeed);
            isSlowed = false;

        });
        effectTimer.setRepeats(false);
        effectTimer.start();

        Timer cooldownTimer = new Timer(15000, (ActionEvent ev) -> {
            isOnCooldown = false;

        });
        cooldownTimer.setRepeats(false);
        cooldownTimer.start();
    }

    @Override
    public void update() {
        super.update();

        if (!isOnCooldown && !isSlowed) {
            attack();
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
    }
}
