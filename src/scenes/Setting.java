package scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import main.GamePanel;
import ui.Button;
import manager.SoundManager;

public class Setting {
    private Button soundButton;
    private Button backButton;
    private boolean soundOn = true; // Tracks sound state
    private GamePanel gamePanel; // Add reference to GamePanel
    private SoundManager soundManager; // Use GamePanel's SoundManager

    public Setting(GamePanel gamePanel) {
        this.gamePanel = gamePanel; // Store the GamePanel reference
        this.soundManager = gamePanel.getSoundManager(); // Use the shared SoundManager
        soundManager.playBackground(); // Start background music

        // Button dimensions and positions
        int buttonWidth = 200;
        int buttonHeight = 50;
        int buttonSpacing = 20;
        int centerX = (GamePanel.COLS * GamePanel.TILE_SIZE - buttonWidth) / 2; // 380
        int totalHeight = 2 * buttonHeight + buttonSpacing; // 120 (adjusted for 2 buttons)
        int startY = (GamePanel.ROWS * GamePanel.TILE_SIZE - totalHeight) / 2; // 420 (adjusted)

        // Initialize buttons
        soundButton = new Button("Sound: " + (soundOn ? "ON" : "OFF"), centerX, startY, buttonWidth, buttonHeight);
        backButton = new Button("Back", centerX, startY + buttonHeight + buttonSpacing, buttonWidth, buttonHeight);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Draw gradient background
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(100, 149, 237), // Cornflower blue
            0, GamePanel.ROWS * GamePanel.TILE_SIZE, Color.WHITE, true
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, GamePanel.COLS * GamePanel.TILE_SIZE, GamePanel.ROWS * GamePanel.TILE_SIZE);

        // Draw title
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        String title = "Settings";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (GamePanel.COLS * GamePanel.TILE_SIZE - titleWidth) / 2, 150);

        // Draw buttons with shadow effect
        drawButtonWithShadow(g2d, soundButton);
        drawButtonWithShadow(g2d, backButton);
    }

    private void drawButtonWithShadow(Graphics2D g2d, Button button) {
        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, 50)); // Semi-transparent black
        Rectangle2D bounds = button.getBounds();
        g2d.fillRect((int) bounds.getX() + 5, (int) bounds.getY() + 5, 
                     (int) bounds.getWidth(), (int) bounds.getHeight());

        // Draw the button itself
        button.draw(g2d);
    }

    public void onMouseClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        if (soundButton.getBounds().contains(mouseX, mouseY)) {
            soundOn = !soundOn; // Toggle sound
            soundManager.toggleSound(); // Update sound state
            soundButton = new Button("Sound: " + (soundOn ? "ON" : "OFF"), 
                                    soundButton.getBounds().x, soundButton.getBounds().y, 
                                    (int) soundButton.getBounds().width, (int) soundButton.getBounds().height);
            soundManager.playEffect(); // Play click sound on toggle
            System.out.println("Sound toggled: " + (soundOn ? "ON" : "OFF"));
        } else if (backButton.getBounds().contains(mouseX, mouseY)) {
            soundManager.playEffect(); // Play click sound on back
            GameStates.SetGameState(GameStates.MENU); // Return to MENU
            System.out.println("Back Button Clicked: Returning to MENU state!");
        }
    }
}