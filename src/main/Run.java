package main;

import javax.swing.*;

public class Run {
    public static void main(String[] args) {
        JFrame jf = new JFrame();
        GamePanel gamePanel = new GamePanel();
        jf.add(gamePanel);
        jf.pack();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}
