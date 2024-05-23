package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.skloch.game.interfaces.LeaderboardScreenInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/** Allow the player to enter a score and displays the leaderboard. */
public class LeaderboardScreen implements Screen,LeaderboardScreenInterface{
  private HustleGame game;
  Stage leaderboardStage;
  private Window inputWindow;

  private String playerName;

  private final int numberEntriesDisplayed;

  String leaderboardFilePath = "scores.csv";
  Label[] nameLabels;
  Label[] scoreLabels;

  float playerScore;

  Viewport viewport;
  OrthographicCamera camera;
  ScrollPane scrollPane;
  String leaderboardFile;

  /**
   * A screen to display a 'Leaderboard' screen when the player has finished the game.
   *
   * @param game An instance of HustleGame
   */
  public LeaderboardScreen(final HustleGame game, float playerScore){

    this.playerScore = playerScore;
    this.game = game;

    leaderboardStage = new Stage(new FitViewport(game.width, game.height));
    camera = new OrthographicCamera();
    viewport = new FitViewport(game.width, game.height, camera);
    camera.setToOrtho(false, game.width, game.height);

    // Create the window
    Window leaderboardWindow = new Window("", game.skin);
    leaderboardStage.addActor(leaderboardWindow);

    // Table for UI elements in window
    Table leaderboardUiTable = new Table();
    Table leaderboardResultsTable = new Table();
    leaderboardWindow.add(leaderboardUiTable);

    // Title
    Label title = new Label("Leaderboard", game.skin, "button");
    leaderboardResultsTable.add(title).padTop(10);
    leaderboardResultsTable.row();


    numberEntriesDisplayed = 5;
    nameLabels = new Label[numberEntriesDisplayed];
    scoreLabels = new Label[numberEntriesDisplayed];

    List<String[]> entries = fetchLeaderboardEntries(leaderboardFilePath);

    int len = entries.size();

    for (int i = 0; i < numberEntriesDisplayed; i++) {
      Label numb = new Label(String.format("%d", i + 1), game.skin, "button");
      Label name = new Label("-", game.skin, "button");
      Label score = new Label("-", game.skin, "button");

      nameLabels[i] = name;
      scoreLabels[i] = score;
      leaderboardResultsTable.add(numb);
      leaderboardResultsTable.add(name);
      leaderboardResultsTable.add(score);
      leaderboardResultsTable.row();
    }

    leaderboardUiTable.add(leaderboardResultsTable);
    leaderboardUiTable.row();
    leaderboardUiTable.row();

    // Exit button
    TextButton exitButton = new TextButton("Main Menu", game.skin);
    leaderboardUiTable.add(exitButton).bottom().width(300).padTop(10);

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

    leaderboardWindow.pack();

    leaderboardWindow.setSize(600, 600);

    // Centre the window
    leaderboardWindow.setX((viewport.getWorldWidth() / 2) - (leaderboardWindow.getWidth() / 2));
    leaderboardWindow.setY((viewport.getWorldHeight() / 2) - (leaderboardWindow.getHeight() / 2));

    setupInputWindow();
  }

  /** Set up the input layer and display it. */
  private void setupInputWindow() {
    // Group nameInputGroup = new Group();
    // leaderboardStage.addActor(nameInputGroup);

    inputWindow = new Window("", game.skin);
    leaderboardStage.addActor(inputWindow);

    inputWindow.setWidth(600);
    inputWindow.setHeight(600);

    inputWindow.setPosition(
        (game.width / 2f) - inputWindow.getWidth() / 2,
        (game.height / 2f) - inputWindow.getHeight() / 2);
    // nameInputGroup.addActor(inputBox);
    Table inputTable = new Table();
    Label prompt = new Label("Type your name", game.skin, "button");
    Label nameLabel = new Label("", game.skin, "button");

    nameLabel.setPosition(
        (inputWindow.getX() / 2f) - nameLabel.getWidth() / 2,
        (inputWindow.getY() / 2f) - nameLabel.getHeight() / 2);

    inputTable.add(prompt);
    inputTable.row();
    inputTable.add(nameLabel);

    inputWindow.add(inputTable);

    Gdx.input.setInputProcessor(
        new InputAdapter() {
          @Override
          public boolean keyTyped(char character) {
            if (Character.isLetter(character) || Character.valueOf(character).equals(' ')) {
              String currentText = nameLabel.getText().toString();
              if (currentText.length() <= 10) {
                nameLabel.setText(currentText + character);
              }
            }
            return false;
          }

          @Override
          public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.ENTER) {
              playerName = nameLabel.getText().toString();
              closeInputLayer();
            }
            if (keycode == Input.Keys.BACKSPACE) {
              String currentLabel = nameLabel.getText().toString();
              if (!currentLabel.isEmpty()) {
                nameLabel.setText(currentLabel.substring(0, currentLabel.length() - 1));
              }
            }
            return false;
          }
        });
  }

  /** Close the inputLayer. */
  private void closeInputLayer() {
    Gdx.input.setInputProcessor(leaderboardStage);
    storeLeaderboardEntry(
        leaderboardFilePath, new String[] {playerName, String.format("%f", playerScore)});
    updateLeaderboard();
    setInputWindowVisibility(false);
  }

  private void updateLeaderboard() {
    List<String[]> entries = fetchLeaderboardEntries(leaderboardFilePath);
    Comparator<String[]> cmpr = Comparator.comparing(arr -> Float.parseFloat(arr[1]));
    entries.sort(cmpr);

    int pos = 0;
    for (int i = entries.size() - 1;
        i > (entries.size() - numberEntriesDisplayed - 1) && i >= 0;
        i--) {
      nameLabels[pos].setText(String.format("%s", entries.get(i)[0]));
      scoreLabels[pos].setText(String.format("%.1f", Float.parseFloat(entries.get(i)[1])) + "%");
      pos++;
    }
  }

  /**
   * Set the state of the inputLayer.
   *
   * @param value the value to change to
   */
  public void setInputWindowVisibility(boolean value) {
    inputWindow.setVisible(value);
  }

  /**
   * Is the inputLayer open and displayed.
   *
   * @return whether the inputLayer is open
   */
  public boolean isInputWindowOpen() {
    return inputWindow.isVisible();
  }

  /**
   * Get a list of leaderboard entries from the csv file.
   *
   * @param filepath The path of the leaderboard csv
   * @return A list of string arrays
   */
  private List<String[]> fetchLeaderboardEntries(String filepath) {
    FileHandle file = Gdx.files.local(filepath);
    List<String[]> entries = new ArrayList<>();
    if (!file.exists()) {
      System.out.println("WARNING: Couldn't load file " + filepath);
      // If the leaderboard doesn't exist then return an empty list.
    } else {
      String text = file.readString();
      List<String> lines = Arrays.asList(text.split("\\r?\\n"));
      for (String line : lines) {
        entries.add(line.split(","));
      }
    }
    return entries;
  }

  /**
   * Append leaderboard entry to the csv file.
   *
   * @param filepath The path of the leaderboard csv
   * @param entry a single entry as a string array
   */
  private void storeLeaderboardEntry(String filepath, String[] entry) {
    FileHandle file = Gdx.files.local(filepath);
    String writeString = String.join(",", entry);
    writeString += "\n";
    file.writeString(writeString, true);
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

    leaderboardStage.act(delta);
    leaderboardStage.draw();

    camera.update();
  }

  @Override
  public void setPlayerScore(float score) {
    playerScore =score;
  }

  public float getPlayerScore() {
    return playerScore;
  }

  public String getPlayerName(){ return playerName; }

  @Override
  public void setPlayerName(String name){ playerName = name; }

  /**
   * Correctly resizes the onscreen elements when the window is resized.
   *
   * @param width the width of the window after resize
   * @param height the height of the window after resize
   */
  @Override
  public void resize(int width, int height) {
    leaderboardStage.getViewport().update(width, height);
    viewport.update(width, height);
  }

  // Other required methods from Screen
  @Override
  public void show() {}

  @Override
  public void hide() {}

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void dispose() {}
}
