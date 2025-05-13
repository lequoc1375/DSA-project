package main;

import java.awt.*;
import javax.swing.*;
import main.GamePanel;
import scenes.Playing;

public class ControlPanel extends JPanel {
    private GamePanel gamePanel;
    private JButton stopButton, resumeButton;
    private JLabel healthLabel; // New JLabel for health
    private boolean isStopped = false;

    public ControlPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setPreferredSize(new Dimension(200, 800));
        setBackground(Color.LIGHT_GRAY);
        setLayout(null);

        initComponents();
    }

    private void initComponents() {
        stopButton = new JButton("Stop");
        resumeButton = new JButton("Resume");

        // Initialize health label
        healthLabel = new JLabel("HP: 0");
        healthLabel.setFont(new Font("Arial", Font.BOLD, 16));
        healthLabel.setForeground(Color.RED);
        healthLabel.setBounds(10, 90, 180, 30); // Position below buttons

        // Set positions and sizes for buttons
        stopButton.setBounds(10, 10, 180, 30);
        resumeButton.setBounds(10, 10, 180, 30);

        // Hide Resume and Menu initially
        resumeButton.setVisible(false);

        // Add event listeners
        stopButton.addActionListener(e -> toggleStop());
        resumeButton.addActionListener(e -> resumeGame());

        // Add components to panel
        add(stopButton);
        add(resumeButton);
        add(healthLabel); // Add health label
    }

    private void toggleStop() {
        isStopped = true;
        stopButton.setVisible(false);
        resumeButton.setVisible(true);
        gamePanel.stopGame();
        updateHealthLabel(); // Update health when stopping
    }

    private void resumeGame() {
        isStopped = false;
        stopButton.setVisible(true);
        resumeButton.setVisible(false);
        gamePanel.resumeGame();
        updateHealthLabel(); // Update health when resuming
    }

    // Method to update the health label
    public void updateHealthLabel() {
        Playing playing = gamePanel.getPlaying();
        if (playing != null && playing.getPlayer() != null) {
            healthLabel.setText("HP: " + playing.getPlayer().getPlayerHealth());
        } else {
            healthLabel.setText("HP: 0");
        }
    }

    public boolean isStopped() {
        return isStopped;
    }
}