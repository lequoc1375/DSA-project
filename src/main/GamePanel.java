package main;

import controller.KeyHandler;
import controller.MouseHandler;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.*;
import manager.ScenesManager;
import manager.SoundManager;
import scenes.GameStates;
import scenes.Playing;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {
    public static final int COLS = 50;
    public static final int ROWS = 50;
    public static final int TILE_SIZE = 16;
    private static final float TIME_STEP = 0.01887f;
    private ScenesManager scenesManager;
    private SoundManager soundManager;
    private Playing playing;
    private ControlPanel controlPanel;
    private GuidePanel guidePanel;
    private ReentrantLock lock = new ReentrantLock();
    private boolean isRunning = false;
    private boolean isGuideVisible = false;
    private boolean isStart = false;
    private JPanel gameArea;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public GamePanel() {
        setLayout(new BorderLayout());

        // Initialize CardLayout for switching between gameArea and guidePanel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        gameArea = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                int width = controlPanel.isVisible() ? 
                            COLS * TILE_SIZE : 
                            COLS * TILE_SIZE + controlPanel.getPreferredSize().width;
                return new Dimension(width, ROWS * TILE_SIZE);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                lock.lock();
                try {
                    scenesManager.render(g);
                } finally {
                    lock.unlock();
                }
            }
        };
        gameArea.setFocusable(true);
        gameArea.requestFocusInWindow();
        addMouseListener(new MouseHandler(this));
        addMouseMotionListener(new MouseHandler(this));
        playing = new Playing();
        scenesManager = new ScenesManager(this);
        soundManager = new SoundManager();
        controlPanel = new ControlPanel(this, soundManager);

        gameArea.addKeyListener(new KeyHandler(playing));
        
        
        // Initialize GuidePanel
        guidePanel = new GuidePanel(this);
        guidePanel.setVisible(true); // Ensure it's visible when shown

        // Add gameArea and guidePanel to cardPanel
        cardPanel.add(gameArea, "GameArea");
        cardPanel.add(guidePanel, "GuidePanel");
        
        // Add cardPanel to CENTER and controlPanel to EAST
        add(cardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);


        setPreferredSize(new Dimension(COLS * TILE_SIZE + 200, ROWS * TILE_SIZE));

        // Thread render
        new Thread(() -> {
            while (true) {
                if (isRunning || !controlPanel.isStopped()) {
                    gameArea.repaint();
                }
                try {
                    Thread.sleep(16); // ~60 FPS
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Thread manage Playing
        new Thread(() -> {
            while (true) {
                if (isRunning && !controlPanel.isStopped()) {
                    lock.lock();
                    try {
                        switch (GameStates.gameStates) {
                            case PLAYING -> {
                                playing.updateGame(TIME_STEP);
                                controlPanel.updateHealthLabel();
                                controlPanel.updateScore();
                                controlPanel.checkPlayerHealth();
                            }
                        }
                    } finally {
                        lock.unlock();
                    }
                }
                try {
                    Thread.sleep((int) (TIME_STEP * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void run() {}

    public void onMouseClick(MouseEvent e) {
        lock.lock();
        try {
            if(GameStates.gameStates == GameStates.PLAYING)
            playing.onMouseClick(e);
        } finally {
            lock.unlock();
        }
    }

    public void onMouseMoved(MouseEvent e) {
        lock.lock();
        try {

        } finally {
            lock.unlock();
        }
    }

    public void startGame() {
        lock.lock();
        try {
            GameStates.SetGameState(GameStates.PLAYING);
            playing.startGame();
            isRunning = true;
            isStart = true;
            cardLayout.show(cardPanel, "GameArea"); // Ensure gameArea is visible
            System.out.println("Game started");
        } finally {
            lock.unlock();
        }
    }

    public void stopGame() {
        lock.lock();
        try {
            isRunning = false;
            playing.pauseGame();
            System.out.println("Game stopped");
        } finally {
            lock.unlock();
        }
    }

    public void resumeGame() {
        lock.lock();
        try {
            isRunning = true;
            playing.startGame();
            System.out.println("Game resumed");
        } finally {
            lock.unlock();
        }
    }

    public void exitGame() {
        lock.lock();
        try {
            isRunning = false;
            System.exit(0);
        } finally {
            lock.unlock();
        }
    }

    public void replayGame() {
        lock.lock();
        try {
            playing.replayGame();
            isRunning = true;
            cardLayout.show(cardPanel, "GameArea"); 
            System.out.println("Game replayed");
        } finally { 
            lock.unlock();
        }
    }

    public void toggleGuide() {
        isGuideVisible = !isGuideVisible;
        if (isGuideVisible) {
            stopGame();
            cardLayout.show(cardPanel, "GuidePanel");
        } else {
            cardLayout.show(cardPanel, "GameArea"); 
            if(isStart) {
                resumeGame();
            }
        }
    }

    public Playing getPlaying() {
        return playing;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public JPanel getGameArea() {
        return gameArea;
    }

    public JPanel getGuidePanel() {
        return guidePanel;
    }
}