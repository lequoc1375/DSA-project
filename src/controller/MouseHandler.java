package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import main.GamePanel;

public class MouseHandler extends MouseAdapter {
    private GamePanel gamePanel;

    public MouseHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        gamePanel.onMouseClick(e);
        
    }
}
