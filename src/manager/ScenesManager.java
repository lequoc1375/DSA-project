package manager;

import java.awt.Graphics;
import main.GamePanel;
import scenes.GameStates;

public class ScenesManager {
	private GamePanel gamePanel;

	public ScenesManager(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	public void render(Graphics g) {
		switch (GameStates.gameStates) {
			case MENU:
				gamePanel.getMenu().draw(g);
				break;
			case PLAYING:
				gamePanel.getPlaying().draw(g);
				break;
			case SETTINGS:
				gamePanel.getSetting().draw(g); // Render the Setting screen
				break;
		}
	}
}