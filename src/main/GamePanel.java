package main;

import controller.MouseHandler;
import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;
import scenes.Playing;

public class GamePanel extends JPanel implements Runnable {
    public static final int COLS = 40;
    public static final int ROWS = 40;
    public static final int TILE_SIZE = 16;
    private static final float TIME_STEP = 0.04f;
    
    private Playing playing;
    
    public GamePanel() {
        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
        playing = new Playing();
        addMouseListener(new MouseHandler(this));
        new Thread(this).start();
    }
    
    @Override
    public void run() {
        while (true) {
            playing.updateGame(TIME_STEP);
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
        playing.draw(g);
    }
    
    
    public void onMouseClick(MouseEvent e) {
        playing.onMouseClick(e);
    }
}
