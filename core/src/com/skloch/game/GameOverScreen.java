package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Screen that displays when the player loses the game. Displays the player's score and allows them
 * to return to the main menu or view the leaderboard.
 */
public class GameOverScreen implements Screen {
  private HustleGame game;
  Stage gameOverStage;
  Viewport viewport;
  OrthographicCamera camera;
  private TextButton exitButton;
  private TextButton leaderboardButton;

  /**
   * Constructor for the GameOverScreen.
   *
   * @param game the game object
   * @param hoursStudied the number of hours studied
   * @param hoursRecreational the number of hours spent on recreational activities
   * @param hoursSlept the number of hours slept
   * @param mealsEaten the number of meals eaten
   * @param score the player's score
   */
  public GameOverScreen(final HustleGame game, int hoursStudied, int hoursRecreational,
                        int hoursSlept, int mealsEaten, float score) {
    this.game = game;
    gameOverStage = new Stage(new FitViewport(game.width, game.height));
    Gdx.input.setInputProcessor(gameOverStage);

    camera = new OrthographicCamera();
    viewport = new FitViewport(game.width, game.height, camera);
    camera.setToOrtho(false, game.width, game.height);

    // Create the window
    Window gameOverWindow = new Window("", game.skin);
    gameOverStage.addActor(gameOverWindow);

    // Table for UI elements in window
    Table gameOverTable = new Table();
    gameOverWindow.add(gameOverTable);

    // Title
    Label title = new Label("Game Over!", game.skin, "button");
    gameOverTable.add(title).padTop(10);
    gameOverTable.row();

    Table scoresTable = new Table();
    gameOverTable.add(scoresTable);
    gameOverTable.row();

    // Create logic object and get scores
    GameOverScreenLogic logic =
        new GameOverScreenLogic(game, hoursStudied, hoursRecreational, hoursSlept, mealsEaten,
            score);
    String[][] scores = logic.getScores();
    populateScoresTable(scoresTable, scores, game.skin);

    // Exit button
    exitButton = new TextButton("Main Menu", game.skin);
    gameOverTable.add(exitButton).bottom().width(300).padTop(10);
    leaderboardButton = new TextButton("Leaderboard", game.skin);
    gameOverTable.row();
    gameOverTable.add(leaderboardButton).width(300).bottom().padTop(10);

    exitButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        game.soundManager.playButton();
        game.soundManager.stopOverworldMusic();
        dispose();
        game.setScreen(new MenuScreen(game));
      }
    });

    leaderboardButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        game.soundManager.playButton();
        dispose();
        game.setScreen(new LeaderboardScreen(game, score));
      }
    });

    gameOverWindow.pack();

    gameOverWindow.setSize(600, 600);

    // Centre the window
    gameOverWindow.setX((viewport.getWorldWidth() / 2) - (gameOverWindow.getWidth() / 2));
    gameOverWindow.setY((viewport.getWorldHeight() / 2) - (gameOverWindow.getHeight() / 2));
  }

  private void populateScoresTable(Table scoresTable, String[][] scores, Skin skin) {
    for (String[] score : scores) {
      scoresTable.add(new Label(score[0], skin, "interaction")).padBottom(3);
      scoresTable.add(new Label(score[1], skin, "button")).padBottom(3);
      scoresTable.row();
    }
  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1);

    game.blueBackground.draw();

    gameOverStage.act(delta);
    gameOverStage.draw();

    camera.update();
  }

  @Override
  public void resize(int width, int height) {
    gameOverStage.getViewport().update(width, height);
    viewport.update(width, height);
  }

  @Override
  public void show() {
  }

  @Override
  public void hide() {
  }

  @Override
  public void pause() {
  }

  @Override
  public void resume() {
  }

  @Override
  public void dispose() {
  }

  // Getters and setters for testing
  public TextButton getExitButton() {
    return exitButton;
  }

  public TextButton getLeaderboardButton() {
    return leaderboardButton;
  }
}
