package scenes;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import main.GamePanel;
import ui.Button;

public class Menu {
    private Button playButton;
    private Button settingsButton;
    private Button exitButton;
    private GamePanel gamePanel;
    private BufferedImage backgroundImage;
    private float animationOffset = 0;

    public Menu(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        // Load background image (optional)
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/resources/background.png"));
        } catch (Exception e) {
            System.out.println("Failed to load background image: " + e.getMessage());
        }

        // Button dimensions and spacing
        int buttonWidth = 250;
        int buttonHeight = 60;
        int buttonSpacing = 30;
        int fullWidth = 1000;
        int centerX = (fullWidth - buttonWidth) / 2;
        int totalButtonHeight = 3 * buttonHeight + 2 * buttonSpacing;
        int startY = (GamePanel.ROWS * GamePanel.TILE_SIZE - totalButtonHeight) / 2; 

        playButton = new Button("Play", centerX, startY, buttonWidth, buttonHeight);
        settingsButton = new Button("Settings", centerX, startY + buttonHeight + buttonSpacing, buttonWidth, buttonHeight);
        exitButton = new Button("Exit", centerX, startY + 2 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Draw background
        int panelWidth = gamePanel.getPreferredSize().width;
        int panelHeight = GamePanel.ROWS * GamePanel.TILE_SIZE;
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, panelWidth, panelHeight, null);
        } else {
            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(135, 206, 250),
                    0, panelHeight, Color.WHITE, true
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, panelWidth, panelHeight);
        }

        // Subtle animated overlay
        g2d.setColor(new Color(255, 255, 255, 30));
        g2d.fillRect(0, (int) (animationOffset % panelHeight), panelWidth, 50);
        animationOffset += 0.5f;

        // Draw title with shadow
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.setFont(new Font("Arial", Font.BOLD, 60));
        String title = "Tactical Game";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        int titleX = (panelWidth - titleWidth) / 2;
        int titleY = 120; // Fixed position for title
        g2d.drawString(title, titleX + 3, titleY + 3);

        g2d.setColor(new Color(20, 20, 80));
        g2d.drawString(title, titleX, titleY);

        drawButtonWithShadow(g2d, playButton);
        drawButtonWithShadow(g2d, settingsButton);
        drawButtonWithShadow(g2d, exitButton);

        System.out.println("Offset: " + animationOffset);

    }

    private void drawButtonWithShadow(Graphics2D g2d, Button button) {
        Rectangle2D bounds = button.getBounds();
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillRect((int) bounds.getX() + 5, (int) bounds.getY() + 5,
                (int) bounds.getWidth(), (int) bounds.getHeight());
        button.draw(g2d);
    }

    public void onMouseClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        if (playButton.getBounds().contains(mouseX, mouseY)) {
            gamePanel.startGame();
            System.out.println("Play Button Clicked: Starting game!");
        } else if (settingsButton.getBounds().contains(mouseX, mouseY)) {
            GameStates.SetGameState(GameStates.SETTINGS);
            gamePanel.repaint();
            System.out.println("Settings Button Clicked: Switching to SETTINGS state!");
        } else if (exitButton.getBounds().contains(mouseX, mouseY)) {
            gamePanel.exitGame();
            System.out.println("Exit Button Clicked: Exiting game!");
        }
    }

    public void onMouseMoved(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        playButton.setHovered(playButton.getBounds().contains(mouseX, mouseY));
        settingsButton.setHovered(settingsButton.getBounds().contains(mouseX, mouseY));
        exitButton.setHovered(exitButton.getBounds().contains(mouseX, mouseY));
    }

    public void update() {
        animationOffset += 0.5f;
    }

}