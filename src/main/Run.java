package main;

import javax.swing.*;

public class Run {
    public static void main(String[] args) {
        JFrame jf = new JFrame();
        GamePanel gamePanel = new GamePanel();
        jf.add(gamePanel);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jf.setUndecorated(false);
        jf.pack();
        jf.setLocationRelativeTo(null);
        gamePanel.requestFocusInWindow();
        jf.setVisible(true);
    }
}
