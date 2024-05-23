package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * A screen that displays the player's stats at the end of the game. Currently, doesn't calculate a
 * score
 */
public class GameOverScreen implements Screen {
  private HustleGame game;
  Stage gameOverStage;
  Viewport viewport;
  OrthographicCamera camera;
  ScrollPane scrollPane;

  /**
   * A screen to display a 'Game Over' screen when the player finishes their exams Currently does
   * not calculate a score, just shows the player's stats to them, as requested in assessment 1
   * Tracking them now will make win conditions easier to implement for assessment 2.
   *
   * @param game              An instance of HustleGame
   * @param hoursStudied      The hours studied in the playthrough
   * @param hoursRecreational The hours of fun had in the playthrough
   * @param hoursSlept        The hours slept in the playthrough
   * @param mealsEaten        The number of meals eaten over a playthrough
   */
  public GameOverScreen(
      final HustleGame game,
      int hoursStudied,
      int hoursRecreational,
      int hoursSlept,
      int mealsEaten,
      float score) {
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

    // Display scores
    scoresTable.add(new Label("Hours Studied", game.skin, "interaction")).padBottom(3);
    scoresTable.add(new Label(String.valueOf(hoursStudied), game.skin, "button")).padBottom(3);
    scoresTable.row();
    scoresTable.add(new Label("Recreational hours", game.skin, "interaction")).padBottom(3);
    scoresTable.add(new Label(String.valueOf(hoursRecreational), game.skin, "button")).padBottom(3);
    scoresTable.row();
    scoresTable.add(new Label("Hours Slept", game.skin, "interaction")).padBottom(3);
    scoresTable.add(new Label(String.valueOf(hoursSlept), game.skin, "button")).padBottom(3);
    scoresTable.row();
    scoresTable.add(new Label("Meals Eaten", game.skin, "interaction")).padBottom(3);
    scoresTable.add(new Label(String.valueOf(mealsEaten), game.skin, "button")).padBottom(3);
    scoresTable.row();
    if (this.game.studyStreak.getAchieved()) {
      scoresTable
          .add(new Label(game.studyStreak.getTitle(), game.skin, "interaction"))
          .padBottom(3);
    }
    if (this.game.bookWorm.getAchieved()) {
      scoresTable.add(new Label(game.bookWorm.getTitle(), game.skin, "interaction")).padBottom(3);
      scoresTable.row();
    }
    if (this.game.eatStreak.getAchieved()) {
      scoresTable.add(new Label(game.eatStreak.getTitle(), game.skin, "interaction")).padBottom(3);
    }
    if (this.game.funStreak.getAchieved()) {
      scoresTable.add(new Label(game.funStreak.getTitle(), game.skin, "interaction")).padBottom(3);
      scoresTable.row();
    }
    if (this.game.allNighter.getAchieved()) {
      scoresTable.add(new Label(game.allNighter.getTitle(), game.skin, "interaction")).padBottom(3);
      scoresTable.row();
    }

    // Exit button
    TextButton exitButton = new TextButton("Main Menu", game.skin);
    gameOverTable.add(exitButton).bottom().width(300).padTop(10);
    TextButton leaderboardButton = new TextButton("Leaderboard", game.skin);
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

    exitButton.addListener(
        new ChangeListener() {
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

  /**
   * Renders the screen and the background each frame.
   *
   * @param delta The time in seconds since the last render.
   */
  @Override
  public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1);

    game.blueBackground.draw();

    gameOverStage.act(delta);
    gameOverStage.draw();

    camera.update();
  }

  /**
   * Correctly resizes the onscreen elements when the window is resized.
   *
   * @param width  The new width of the window.
   * @param height The new height of the window.
   */
  @Override
  public void resize(int width, int height) {
    gameOverStage.getViewport().update(width, height);
    viewport.update(width, height);
  }

  // Other required methods from Screen
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
}
