package manager;

import java.io.File;
import java.net.URL;
import javax.sound.sampled.*;

public class SoundManager {
    private Clip backgroundClip;
    private Clip effectClip;
    private boolean soundOn = true;

    public SoundManager() {
        try {
           
            URL backgroundUrl = getClass().getClassLoader().getResource("resources/gamemusic.wav");

            if (backgroundUrl == null) {
                backgroundUrl = getClass().getClassLoader().getResource("resources/gamemusic.wav");
            }
            AudioInputStream backgroundStream = AudioSystem.getAudioInputStream(backgroundUrl);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(backgroundStream);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); 


            URL effectUrl = getClass().getResource("/resources/click.wav");
            if (effectUrl == null) {
                effectUrl = new File("click.wav").toURI().toURL();
            }
            AudioInputStream effectStream = AudioSystem.getAudioInputStream(effectUrl);
            effectClip = AudioSystem.getClip();
            effectClip.open(effectStream);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Sound files not found. Please add background.wav and click.wav to resources folder or project directory.");
        }
    }

    public void toggleSound() {
        soundOn = !soundOn;
        if (soundOn) {
            resumeSound();
        } else {
            pauseSound();
        }
    }

    public void playEffect() {
        if (soundOn && effectClip != null) {
            effectClip.setFramePosition(0); 
            effectClip.start();
        }
    }

    public void playBackground() {
        if (soundOn && backgroundClip != null && !backgroundClip.isRunning()) {
            backgroundClip.setFramePosition(0); 
            backgroundClip.start();
        }
    }

    public void pauseSound() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }

    public void resumeSound() {
        if (backgroundClip != null && !backgroundClip.isRunning()) {
            backgroundClip.start();
        }
    }

    public boolean isSoundOn() {
        return soundOn;
    }
}