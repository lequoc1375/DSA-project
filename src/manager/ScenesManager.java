package manager;

import java.awt.Graphics;
import main.GamePanel;
import scenes.GameStates;

public class ScenesManager {
	private GamePanel gamepanel;

	public ScenesManager(GamePanel gamePanel) {
		this.gamepanel = gamePanel;
	}

	public void render(Graphics g) {
		switch (GameStates.gameStates) {
			case PLAYING:
				gamepanel.getPlaying().draw(g);
				break;
			case MENU:
				gamepanel.getMenu().draw(g);
				break;
			case ROLL:
				break;

		}
	}
}


