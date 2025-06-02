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
                throw new Exception("Background music file not found: resources/gamemusic.wav");
            }
            AudioInputStream backgroundStream = AudioSystem.getAudioInputStream(backgroundUrl);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(backgroundStream);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);

            // Load sound effect
            URL effectUrl = getClass().getClassLoader().getResource("resources/click.wav");
            if (effectUrl == null) {
                effectUrl = new File("resources/click.wav").toURI().toURL();
            }
            AudioInputStream effectStream = AudioSystem.getAudioInputStream(effectUrl);
            effectClip = AudioSystem.getClip();
            effectClip.open(effectStream);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Sound files not found. Ensure gamemusic.wav and click.wav are in the resources folder.");
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