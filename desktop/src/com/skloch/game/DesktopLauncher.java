package com.skloch.game;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * DesktopLauncher class to launch the Heslington Hustle game. Please note that on macOS your
 * application needs to be started with the -XstartOnFirstThread JVM argument.
 */
public class DesktopLauncher {
  // Constants
  private static final int WINDOW_WIDTH = 1280;
  private static final int MAX_WINDOW_WIDTH = 1920;
  private static final int WINDOW_HEIGHT = 720;
  private static final int MAX_WINDOW_HEIGHT = 1080;
  private static final int FOREGROUND_FPS = 60;

  /**
   * Main method to launch the game.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

    config.setWindowSizeLimits(
        WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2, MAX_WINDOW_WIDTH, MAX_WINDOW_HEIGHT);
    config.setTitle("Heslington Hustle");
    config.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
    config.useVsync(true);
    config.setForegroundFPS(FOREGROUND_FPS);

    config.setWindowIcon(Files.FileType.Internal, "Icons/icon_16x16.png");
    config.setWindowIcon(Files.FileType.Internal, "Icons/icon_32x32.png");
    config.setWindowIcon(Files.FileType.Internal, "Icons/icon_128x128.png");

    new Lwjgl3Application(new HustleGame(WINDOW_WIDTH, WINDOW_HEIGHT), config);
  }
}
