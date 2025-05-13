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
    private JButton playButton, stopButton, resumeButton, replayButton;
    private JLabel healthLabel;
    private JLabel currentScoreLabel;
    private JLabel highestScoreTitleLabel, highestScoreValueLabel; 
    private boolean isStopped = false;
    private boolean isStarted = false; 
    private boolean isMuted = false;
    private long startTime; 
    private long pausedTime; 
    private long totalPausedDuration;
    private int currentScore; 
    private int highestScore;
    private static final String SCORE_FILE = "highscore.txt";

    public ControlPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setPreferredSize(new Dimension(200, 800));
        setBackground(Color.LIGHT_GRAY);
        setLayout(null);

        loadHighScore(); 
        initComponents();
    }

    private void initComponents() {
        soundManager = new SoundManager();

        playButton = new JButton("Play");
        stopButton = new JButton("Stop");
        resumeButton = new JButton("Resume");
        replayButton = new JButton("Replay");

        // Initialize high score label
        highestScoreTitleLabel = new JLabel("Highest Score");
        highestScoreTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        highestScoreTitleLabel.setForeground(Color.BLUE);
        highestScoreTitleLabel.setBounds(0, 20, 200, 30);
        highestScoreTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        highestScoreValueLabel = new JLabel("" + highestScore);
        highestScoreValueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        highestScoreValueLabel.setForeground(Color.BLUE);
        highestScoreValueLabel.setBounds(0, 70, 200, 30);
        highestScoreValueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Initialize health label
        healthLabel = new JLabel("HP: 1");
        healthLabel.setFont(new Font("Arial", Font.BOLD, 20));
        healthLabel.setForeground(Color.RED);
        healthLabel.setBounds(10, 200, 180, 30);

        // Initialize current score label
        currentScoreLabel = new JLabel("Score: 0");
        currentScoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        currentScoreLabel.setForeground(Color.BLACK);
        currentScoreLabel.setBounds(10, 150, 180, 30);

        // Set positions and sizes for buttons
        playButton.setBounds(10, 250, 180, 50);
        stopButton.setBounds(10, 250, 180, 50);
        resumeButton.setBounds(10, 250, 180, 50);
        replayButton.setBounds(10, 310, 180, 50);

        // Set initial visibility
        playButton.setVisible(true);
        stopButton.setVisible(false);
        resumeButton.setVisible(false);
        replayButton.setVisible(false);
        // Add event listeners
        playButton.addActionListener(e -> startGame());
        stopButton.addActionListener(e -> toggleStop());
        resumeButton.addActionListener(e -> resumeGame());
        replayButton.addActionListener(e -> replayGame());

        add(playButton);
        add(stopButton);
        add(resumeButton);
        add(replayButton);
        add(healthLabel);
        add(currentScoreLabel);
        add(highestScoreTitleLabel);
        add(highestScoreValueLabel);
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

    private void toggleStop() {
        isStopped = true;
        stopButton.setVisible(false);
        resumeButton.setVisible(true);
        replayButton.setVisible(true);
        gamePanel.stopGame();
        updateHealthLabel();
        pausedTime = System.currentTimeMillis(); // Ghi lại thời gian tạm dừng
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
                saveHighScore();
                highestScoreValueLabel.setText("" + highestScore);
            }
        }
    }

    public void onPlayerDeath() {
        updateScore();
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

    private void loadHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                highestScore = Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            highestScore = 0;
        }
    }

    private void saveHighScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE))) {
            writer.write(String.valueOf(highestScore));
        } catch (IOException e) {
            System.out.println("Cannot save the highest score.");
        }
    }

    public boolean isStopped() {
        return isStopped;
    }

    public boolean isStarted() {
        return isStarted;
    }
}