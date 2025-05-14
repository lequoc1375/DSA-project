package main;

import java.awt.*;
import javax.swing.*;

import java.io.*;
import main.GamePanel;
import manager.SoundManager;
import scenes.Playing;

public class ControlPanel extends JPanel {
    private GamePanel gamePanel;
    private SoundManager soundManager;
    private JButton playButton, stopButton, resumeButton, replayButton, muteButton;
    private JLabel healthLabel;
    private JLabel currentScoreLabel;
    private JLabel highestScoreTitleLabel, highestScoreValueLabel; 
    private JLabel lastScoreTitleLabel, lastScoreValueLabel;
    private boolean isStopped = false;
    private boolean isStarted = false; 
    private boolean isMuted = false;
    private long startTime; 
    private long pausedTime; 
    private long totalPausedDuration;
    private int currentScore; 
    private int highestScore;
    private int lastScore;
    private static final String SCORE_FILE = "highestScore.txt";
    private static final String LAST_SCORE_FILE = "lastScore.txt";

    public ControlPanel(GamePanel gamePanel, SoundManager soundManager) {
        this.gamePanel = gamePanel;
        this.soundManager = soundManager;
        setPreferredSize(new Dimension(200, 800));
        setBackground(Color.LIGHT_GRAY);
        setLayout(null);

        loadHighestScore(); 
        loadLastScore();
        initComponents();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Gradient nền
        Graphics2D g2d = (Graphics2D) g;
        Color color1 = new Color(10, 10, 30);
        Color color2 = new Color(30, 60, 90);
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void initComponents() {
        playButton = createStyledButton("PLAY");
        stopButton = createStyledButton("STOP");
        resumeButton = createStyledButton("RESUME");
        replayButton = createStyledButton("REPLAY");
        muteButton = createStyledButton("MUTE");

        // Highest Score Title
        highestScoreTitleLabel = new JLabel("Highest Score");
        highestScoreTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        highestScoreTitleLabel.setForeground(new Color(248, 200, 102));
        highestScoreTitleLabel.setBounds(0, 20, 200, 30);
        highestScoreTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Highest Score Value
        highestScoreValueLabel = new JLabel("" + highestScore);
        highestScoreValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        highestScoreValueLabel.setForeground(new Color(255, 252, 242));
        highestScoreValueLabel.setBounds(0, 60, 200, 30);
        highestScoreValueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Last Score Title
        lastScoreTitleLabel = new JLabel("Last Score");
        lastScoreTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lastScoreTitleLabel.setForeground(new Color(101, 174, 174));
        lastScoreTitleLabel.setBounds(0, 110, 200, 30);
        lastScoreTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Last Score Value
        lastScoreValueLabel = new JLabel("" + lastScore);
        lastScoreValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lastScoreValueLabel.setForeground(new Color(255, 252, 242));
        lastScoreValueLabel.setBounds(0, 150, 200, 30);
        lastScoreValueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Current Score
        currentScoreLabel = new JLabel("Score: 0");
        currentScoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        currentScoreLabel.setForeground(new Color(230, 230, 230));
        currentScoreLabel.setBounds(10, 200, 180, 30);

        // Health Label
        healthLabel = new JLabel("HP: 1");
        healthLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        healthLabel.setForeground(new Color(200, 50, 50));
        healthLabel.setBounds(10, 240, 180, 30);

        // Buttons
        playButton.setBounds(10, 300, 180, 50);
        stopButton.setBounds(10, 300, 180, 50);
        resumeButton.setBounds(10, 300, 180, 50);
        replayButton.setBounds(10, 360, 180, 50);
        muteButton.setBounds(10, 550, 180, 50);

        playButton.setVisible(true);
        stopButton.setVisible(false);
        resumeButton.setVisible(false);
        replayButton.setVisible(false);
        muteButton.setVisible(true);

        playButton.addActionListener(e -> {
            startGame();
            gamePanel.getGameArea().requestFocusInWindow();
        });
        stopButton.addActionListener(e -> {
            toggleStop();
            gamePanel.getGameArea().requestFocusInWindow();
        });
        resumeButton.addActionListener(e -> {
            resumeGame();
            gamePanel.getGameArea().requestFocusInWindow();
        });
        replayButton.addActionListener(e -> {
            replayGame();
            gamePanel.getGameArea().requestFocusInWindow();
        });
        muteButton.addActionListener(e -> {
            toggleMute();
            gamePanel.getGameArea().requestFocusInWindow();
        });

        add(playButton);
        add(stopButton);
        add(resumeButton);
        add(replayButton);
        add(muteButton);
        add(healthLabel);
        add(currentScoreLabel);
        add(highestScoreTitleLabel);
        add(highestScoreValueLabel);
        add(lastScoreTitleLabel);
        add(lastScoreValueLabel);
    }

    public void checkPlayerHealth() {
        Playing playing = gamePanel.getPlaying();
        if(playing.getPlayer().getPlayerHealth() == 0) {
            replayGame();
        }
    }

    private void startGame() {
        isStarted = true;
        isStopped = false;
        playButton.setVisible(false);
        stopButton.setVisible(true);
        resumeButton.setVisible(false);
        replayButton.setVisible(false);
        gamePanel.startGame();
        startTime = System.currentTimeMillis();
        totalPausedDuration = 0;
        currentScore = 0;
        currentScoreLabel.setText("Score: 0");
        updateHealthLabel();
    }

    public void toggleStop() {
        isStopped = true;
        stopButton.setVisible(false);
        resumeButton.setVisible(true);
        replayButton.setVisible(true);
        gamePanel.stopGame();
        updateHealthLabel();
        pausedTime = System.currentTimeMillis(); 
        updateScore();
    }

    private void resumeGame() {
        isStopped = false;
        stopButton.setVisible(true);
        resumeButton.setVisible(false);
        replayButton.setVisible(false);
        gamePanel.resumeGame();
        updateHealthLabel();
        totalPausedDuration += System.currentTimeMillis() - pausedTime;
    }

    private void replayGame() {
        isStarted = false;
        isStopped = false;
        playButton.setVisible(true);
        stopButton.setVisible(false);
        resumeButton.setVisible(false);
        replayButton.setVisible(false);
        onPlayerDeath(); 
        System.out.println("replay 1");
        gamePanel.replayGame();
        System.out.println("replay 2");
        updateHealthLabel();
        System.out.println("replay 3");
    }

    public void toggleMute() {
        isMuted = !isMuted;
        muteButton.setText(isMuted ? "UNMUTE" : "MUTE");
        if (isMuted) {
            soundManager.pauseSound();
        } else {
            soundManager.resumeSound();
        }
    }

    public void updateHealthLabel() {
        Playing playing = gamePanel.getPlaying();
        if (playing != null && playing.getPlayer() != null) {
            healthLabel.setText("HP: " + playing.getPlayer().getPlayerHealth());
        } else {
            healthLabel.setText("HP: 0");
        }
    }

    public void updateScore() {
        if (isStarted && !isStopped) {
            long currentTime = System.currentTimeMillis();
            currentScore = (int) ((currentTime - startTime - totalPausedDuration) / 1000);
            currentScoreLabel.setText("Score: " + currentScore);

            if (currentScore > highestScore) {
                highestScore = currentScore;
                saveHighestScore();
                highestScoreValueLabel.setText("" + highestScore);
            }
        }
    }

    public void onPlayerDeath() {
        updateScore();
        lastScore = currentScore;
        lastScoreValueLabel.setText(String.valueOf(lastScore));
        saveLastScore();
        startTime = System.currentTimeMillis();
        totalPausedDuration = 0;
        currentScore = 0;
        currentScoreLabel.setText("Score: 0");
        isStarted = false;
        isStopped = true;
        playButton.setVisible(true);
        stopButton.setVisible(false);
        resumeButton.setVisible(false);
    }

    private void loadHighestScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                highestScore = Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            highestScore = 0;
        }
    }

    private void saveHighestScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE))) {
            writer.write(String.valueOf(highestScore));
        } catch (IOException e) {
            System.out.println("Cannot save the highest score.");
        }
    }

    private void saveLastScore() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(LAST_SCORE_FILE))) {
        writer.write(String.valueOf(currentScore));
    } catch (IOException e) {
        System.out.println("Cannot save the last score.");
    }
}

    private void loadLastScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LAST_SCORE_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                lastScore = Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            lastScore = 0;
        }
    }

    private void setHoverEffect(JButton button, Color normal, Color hover) {
        button.setBackground(normal);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normal);
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;
            private boolean isPressed = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Bóng đổ
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 20, 20);

                // Gradient nền dựa trên trạng thái
                GradientPaint gp;
                if (isPressed) {
                    gp = new GradientPaint(0, 0, new Color(50, 100, 150), 
                                        0, getHeight(), new Color(20, 60, 100));
                } else if (isHovered) {
                    gp = new GradientPaint(0, 0, new Color(100, 160, 210), 
                                        0, getHeight(), new Color(50, 100, 150));
                } else {
                    gp = new GradientPaint(0, 0, new Color(70, 130, 180), 
                                        0, getHeight(), new Color(30, 80, 120));
                }
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 20, 20); // Bo góc

                super.paintComponent(g);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Tắt viền mặc định
            }

            // Cập nhật trạng thái hover và pressed
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        isHovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        isHovered = false;
                        repaint();
                    }

                    @Override
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        isPressed = true;
                        repaint();
                    }

                    @Override
                    public void mouseReleased(java.awt.event.MouseEvent evt) {
                        isPressed = false;
                        repaint();
                    }
                });
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false); 
        button.setBorderPainted(false); 
        button.setFocusPainted(false); 
        button.setPreferredSize(new Dimension(180, 50)); 
        button.setVerticalAlignment(SwingConstants.CENTER); 
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setMargin(new Insets(0, 0, 0, 0)); 
        return button;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public boolean isStarted() {
        return isStarted;
    }
}

