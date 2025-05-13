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
		gamePanel.getPlaying().draw(g);
	}
}