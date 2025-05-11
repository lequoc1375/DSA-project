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
import scenes.Menu;
import scenes.Playing;
import scenes.Setting;

public class GamePanel extends JPanel implements Runnable {
    public static final int COLS = 50;
    public static final int ROWS = 50;
    public static final int TILE_SIZE = 16;
    private static final float TIME_STEP = 0.01887f;
    private ScenesManager scenesManager;
    private Menu menu;
    private Playing playing;
    private Setting setting;
    private SoundManager soundManager;
    private ControlPanel controlPanel;
    private ReentrantLock lock = new ReentrantLock();
    private boolean isRunning = true;

    public GamePanel() {
        setLayout(new BorderLayout()); 

       
        JPanel gameArea = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE);
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
        gameArea.addKeyListener(new KeyHandler(playing));

   
        soundManager = new SoundManager();
        playing = new Playing();
        menu = new Menu();
        setting = new Setting(this);
        scenesManager = new ScenesManager(this);
        controlPanel = new ControlPanel(this);

      
        add(gameArea, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);

    
        setPreferredSize(new Dimension(COLS * TILE_SIZE + 100, ROWS * TILE_SIZE));

     
        new Thread(() -> {
            while (true) {
                if (isRunning || !controlPanel.isStopped()) {
                    gameArea.repaint();
                }
                try {
                    Thread.sleep(16); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

     
        new Thread(() -> {
            while (true) {
                if (isRunning && !controlPanel.isStopped() && GameStates.gameStates == GameStates.PLAYING) {
                    lock.lock();
                    try {
                        playing.updateGame(TIME_STEP);
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

       
        new Thread(() -> {
            while (true) {
                if (isRunning && !controlPanel.isStopped() && soundManager.isSoundOn()) {
                    lock.lock();
                    try {
                        soundManager.playBackground();
                    } finally {
                        lock.unlock();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void run() {
      
    }

    
    public  void onMouseClick(MouseEvent e) {
        lock.lock();
        try {
            if (GameStates.gameStates == GameStates.PLAYING) {
                playing.onMouseClick(e);
            } else if (GameStates.gameStates == GameStates.MENU) {
                menu.onMouseClick(e);
            } else if (GameStates.gameStates == GameStates.SETTINGS) {
                setting.onMouseClick(e);
            }
        } finally {
            lock.unlock();
        }
    }

    public void stopGame() {
        lock.lock();
        try {
            isRunning = false;
            System.out.println("Game stopped");
        } finally {
            lock.unlock();
        }
    }

    public void resumeGame() {
        lock.lock();
        try {
            isRunning = true;
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

    public Playing getPlaying() {
        return playing;
    }

    public Menu getMenu() {
        return menu;
    }

    public Setting getSetting() {
        return setting;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }
}