package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import main.GamePanel;

public class ControlPanel extends JPanel {
    private GamePanel gamePanel;
    private JButton stopButton, resumeButton, exitButton;
    private boolean isStopped = false;

    public ControlPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setPreferredSize(new Dimension(100, 800)); // Chiều rộng 100px, cao bằng màn hình game
        setBackground(Color.LIGHT_GRAY);
        setLayout(null);

        initComponents();
    }

    private void initComponents() {
        stopButton = new JButton("Stop");
        resumeButton = new JButton("Resume");
        exitButton = new JButton("Exit");

        // Đặt vị trí và kích thước
        stopButton.setBounds(10, 10, 80, 30);
        resumeButton.setBounds(10, 50, 80, 30);
        exitButton.setBounds(10, 90, 80, 30);

        // Ẩn Resume và Exit ban đầu
        resumeButton.setVisible(false);
        exitButton.setVisible(false);

        // Thêm sự kiện
        stopButton.addActionListener(e -> toggleStop());
        resumeButton.addActionListener(e -> resumeGame());
        exitButton.addActionListener(e -> exitGame());

        // Thêm nút vào panel
        add(stopButton);
        add(resumeButton);
        add(exitButton);
    }

    private void toggleStop() {
        isStopped = true;
        stopButton.setVisible(false);
        resumeButton.setVisible(true);
        exitButton.setVisible(true);
        gamePanel.stopGame();
    }

    private void resumeGame() {
        isStopped = false;
        stopButton.setVisible(true);
        resumeButton.setVisible(false);
        exitButton.setVisible(false);
        gamePanel.resumeGame();
    }

    private void exitGame() {
        gamePanel.exitGame();
    }

    public boolean isStopped() {
        return isStopped;
    }
}