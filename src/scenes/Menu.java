package scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import ui.Button;
import main.GamePanel;

public class Menu {
    private Button playButton;
    private Button settingsButton;
    private Button exitButton;

    public Menu() {

        int buttonWidth = 200;
        int buttonHeight = 50;
        int buttonSpacing = 20;
        int centerX = (GamePanel.COLS * GamePanel.TILE_SIZE - buttonWidth) / 2;
        int totalHeight = 3 * buttonHeight + 2 * buttonSpacing; // 190
        int startY = (GamePanel.ROWS * GamePanel.TILE_SIZE - totalHeight) / 2;


        playButton = new Button("Play", centerX, startY, buttonWidth, buttonHeight);
        settingsButton = new Button("Settings", centerX, startY + buttonHeight + buttonSpacing, buttonWidth, buttonHeight);
        exitButton = new Button("Exit", centerX, startY + 2 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;


        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(135, 206, 250), // Sky blue
                0, GamePanel.ROWS * GamePanel.TILE_SIZE, Color.WHITE, true
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, GamePanel.COLS * GamePanel.TILE_SIZE, GamePanel.ROWS * GamePanel.TILE_SIZE);

        // Draw title
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        String title = "Tactical Game";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (GamePanel.COLS * GamePanel.TILE_SIZE - titleWidth) / 2, 150);

        // Draw buttons with shadow effect
        drawButtonWithShadow(g2d, playButton);
        drawButtonWithShadow(g2d, settingsButton);
        drawButtonWithShadow(g2d, exitButton);
    }

    private void drawButtonWithShadow(Graphics2D g2d, Button button) {
        g2d.setColor(new Color(0, 0, 0, 50));
        Rectangle2D bounds = button.getBounds();
        g2d.fillRect((int) bounds.getX() + 5, (int) bounds.getY() + 5,
                (int) bounds.getWidth(), (int) bounds.getHeight());


        button.draw(g2d);
    }

    public void onMouseClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        if (playButton.getBounds().contains(mouseX, mouseY)) {
            GameStates.SetGameState(GameStates.PLAYING);
            System.out.println("Play Button Clicked: Switching to PLAYING state!");
        } else if (settingsButton.getBounds().contains(mouseX, mouseY)) {
            GameStates.SetGameState(GameStates.SETTINGS); // Switch to SETTINGS state
            System.out.println("Settings Button Clicked: Switching to SETTINGS state!");
        } else if (exitButton.getBounds().contains(mouseX, mouseY)) {
            System.exit(0);
        }
    }
}