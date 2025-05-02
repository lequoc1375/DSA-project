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

public class Setting {
    private Button soundButton;
    private Button difficultyButton;
    private Button backButton;
    private boolean soundOn = true;
    private String difficulty = "Normal";

    public Setting() {

        int buttonWidth = 200;
        int buttonHeight = 50;
        int buttonSpacing = 20;
        int centerX = (GamePanel.COLS * GamePanel.TILE_SIZE - buttonWidth) / 2; // 380
        int totalHeight = 3 * buttonHeight + 2 * buttonSpacing; // 190
        int startY = (GamePanel.ROWS * GamePanel.TILE_SIZE - totalHeight) / 2; // 385


        soundButton = new Button("Sound: ON", centerX, startY, buttonWidth, buttonHeight);
        difficultyButton = new Button("Difficulty: " + difficulty, centerX, startY + buttonHeight + buttonSpacing, buttonWidth, buttonHeight);
        backButton = new Button("Back", centerX, startY + 2 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;


        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(100, 149, 237), // Cornflower blue
                0, GamePanel.ROWS * GamePanel.TILE_SIZE, Color.WHITE, true
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, GamePanel.COLS * GamePanel.TILE_SIZE, GamePanel.ROWS * GamePanel.TILE_SIZE);


        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        String title = "Settings";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (GamePanel.COLS * GamePanel.TILE_SIZE - titleWidth) / 2, 150);

        // Draw buttons with shadow effect
        drawButtonWithShadow(g2d, soundButton);
        drawButtonWithShadow(g2d, difficultyButton);
        drawButtonWithShadow(g2d, backButton);
    }

    private void drawButtonWithShadow(Graphics2D g2d, Button button) {

        g2d.setColor(new Color(0, 0, 0, 50)); // Semi-transparent black
        Rectangle2D bounds = button.getBounds();
        g2d.fillRect((int) bounds.getX() + 5, (int) bounds.getY() + 5,
                (int) bounds.getWidth(), (int) bounds.getHeight());


        button.draw(g2d);
    }

    public void onMouseClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        if (soundButton.getBounds().contains(mouseX, mouseY)) {
            soundOn = !soundOn;
            soundButton = new Button("Sound: " + (soundOn ? "ON" : "OFF"),
                    soundButton.getBounds().x, soundButton.getBounds().y,
                    (int) soundButton.getBounds().width, (int) soundButton.getBounds().height);
            System.out.println("Sound toggled: " + (soundOn ? "ON" : "OFF"));
        } else if (difficultyButton.getBounds().contains(mouseX, mouseY)) {
            if (difficulty.equals("Easy")) {
                difficulty = "Normal";
            } else if (difficulty.equals("Normal")) {
                difficulty = "Hard";
            } else {
                difficulty = "Easy";
            }
            difficultyButton = new Button("Difficulty: " + difficulty,
                    difficultyButton.getBounds().x, difficultyButton.getBounds().y,
                    (int) difficultyButton.getBounds().width, (int) difficultyButton.getBounds().height);
            System.out.println("Difficulty set to: " + difficulty);
        } else if (backButton.getBounds().contains(mouseX, mouseY)) {
            GameStates.SetGameState(GameStates.MENU);
            System.out.println("Back Button Clicked: Returning to MENU state!");
        }
    }
}