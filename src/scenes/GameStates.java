package scenes;

 public enum GameStates {
	PLAYING, MENU, SETTINGS;

	public static GameStates gameStates = PLAYING;

	public static void SetGameState(GameStates state) {
		gameStates = state;
	}
}
