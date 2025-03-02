package scenes;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import ui.Button;

public class Menu {
    private Button playButton;

    public Menu() {
        playButton = new Button("Playing", 100, 100, 200, 50);
    }

    public void draw(Graphics g) {
        playButton.draw(g);
    }

    public void onMouseClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        if (playButton.getBounds().contains(mouseX, mouseY)) {
            GameStates.SetGameState(GameStates.PLAYING);
            System.out.println("Button Clicked: Switching to PLAYING state!");
        }
    }
}
