package scenes;

 public enum GameStates {
	PLAYING, MENU, ROLL;

	public static GameStates gameStates = MENU;

	public static void SetGameState(GameStates state) {
		gameStates = state;
	}
}
