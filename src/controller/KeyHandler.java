package controller;

import entity.MovedObject.BrownAllies;
import entity.MovedObject.OrangeAllies;
import entity.MovedObject.PurpleAllies;
import scenes.Playing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    private Playing playing;
    public KeyHandler(Playing playing) {
        this.playing = playing;
    }
    @Override
    public void keyTyped(KeyEvent e) {


    }

    @Override
    public void keyPressed(KeyEvent e) {
        BrownAllies brownAllies = playing.getBrownAllies();
        OrangeAllies orangeAllies = playing.getOrangeAllies();
        PurpleAllies purpleAllies = playing.getPurpleAllies();
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_S && brownAllies != null && brownAllies.isEmpDisabled() == false) {
            brownAllies.useSkill();
            System.out.println("brown is pressed");
        }

        if (code == KeyEvent.VK_A && orangeAllies != null && orangeAllies.isEmpDisabled() == false) {
            orangeAllies.useSkill();
            orangeAllies.setUseSkill(true);
            System.out.println("Orange is pressed");
        }

        if (code == KeyEvent.VK_D && purpleAllies != null && purpleAllies.isEmpDisabled() == false) {
            purpleAllies.bless();
            System.out.println("Tims is pressed");
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
