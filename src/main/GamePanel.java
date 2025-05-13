package main;

import controller.KeyHandler;
import controller.MouseHandler;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.*;
import manager.ScenesManager;
import manager.SoundManager;
import scenes.GameStates;
import scenes.Playing;

public class GamePanel extends JPanel implements Runnable {
    public static final int COLS = 50;
    public static final int ROWS = 50;
    public static final int TILE_SIZE = 16;
    private static final float TIME_STEP = 0.01887f;
    private ScenesManager scenesManager;
    private Playing playing;
    private ControlPanel controlPanel;
    private ReentrantLock lock = new ReentrantLock();
    private boolean isRunning = false;
    private JPanel gameArea;

    public GamePanel() {
        setLayout(new BorderLayout());

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
        gameArea.addMouseListener(new MouseHandler(this));
        gameArea.addMouseMotionListener(new MouseHandler(this));
        playing = new Playing();
        scenesManager = new ScenesManager(this);
        controlPanel = new ControlPanel(this);

        gameArea.addKeyListener(new KeyHandler(playing));

        add(gameArea, BorderLayout.CENTER);
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
            System.out.println("Game replayed");
        } finally {
            lock.unlock();
        }
    }

    public Playing getPlaying() {
        return playing;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }
}