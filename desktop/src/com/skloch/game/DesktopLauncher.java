package com.skloch.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.skloch.game.HustleGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		int WIDTH = 1280;
		int HEIGHT = 720;
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowSizeLimits(320, 180, 1920, 1080);
		config.setTitle("Heslington Hustle DEMO!");
		config.setWindowedMode(WIDTH, HEIGHT);
		config.useVsync(true);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new HustleGame(WIDTH, HEIGHT), config);
	}
}