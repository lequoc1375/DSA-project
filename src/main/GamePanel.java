package main;

import controller.KeyHandler;
import controller.MouseHandler;
import java.awt.*;
import java.awt.event.MouseEvent;
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

    public GamePanel() {
        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
        setFocusable(true);
        requestFocusInWindow();
        soundManager = new SoundManager();
        playing = new Playing(); 
        menu = new Menu(); 
        setting = new Setting(this); 
        scenesManager = new ScenesManager(this);
        addMouseListener(new MouseHandler(this));
        addMouseMotionListener(new MouseHandler(this));
        addKeyListener(new KeyHandler(playing));
        new Thread(this).start();
    }
    
    @Override
    public void run() {
        while (true) {
            if (GameStates.gameStates == GameStates.PLAYING) {
                playing.updateGame(TIME_STEP);
            }
            if (soundManager.isSoundOn()) {
                soundManager.playBackground(); 
            }
            repaint();
            try {
                Thread.sleep((int) (TIME_STEP * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        scenesManager.render(g);
    }
    
    public void onMouseClick(MouseEvent e) {
        if (GameStates.gameStates == GameStates.PLAYING) {
            playing.onMouseClick(e);
        } else if (GameStates.gameStates == GameStates.MENU) {
            menu.onMouseClick(e);
        } else if (GameStates.gameStates == GameStates.SETTINGS) {
            setting.onMouseClick(e);
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