package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import main.GamePanel;

public class MouseHandler extends MouseAdapter {
    private GamePanel gamePanel;
    public static int mouseX = 0;
    public static int mouseY = 0;
    public MouseHandler(GamePanel gamePanel) {

        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gamePanel.onMouseClick(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
}
