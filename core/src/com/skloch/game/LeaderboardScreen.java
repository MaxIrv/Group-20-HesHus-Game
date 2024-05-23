package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.skloch.game.interfaces.LeaderboardScreenInterface;
import java.util.List;

public class LeaderboardScreen implements Screen, LeaderboardScreenInterface {
  private HustleGame game;
  private LeaderboardManager leaderboardManager;
  private Stage leaderboardStage;
  private Window inputWindow;
  private Label[] nameLabels;
  private Label[] scoreLabels;
  private int numberEntriesDisplayed;
  private Viewport viewport;
  private OrthographicCamera camera;

  public LeaderboardScreen(final HustleGame game, float playerScore) {
    this.game = game;
    this.leaderboardManager = new LeaderboardManager();
    this.leaderboardManager.setPlayerScore(playerScore);
    setupUI();
  }

  private void setupUI() {
    leaderboardStage = new Stage(new FitViewport(game.width, game.height));
    camera = new OrthographicCamera();
    viewport = new FitViewport(game.width, game.height, camera);
    camera.setToOrtho(false, game.width, game.height);

    Window leaderboardWindow = new Window("", game.skin);
    leaderboardStage.addActor(leaderboardWindow);

    Table leaderboardUiTable = new Table();
    Table leaderboardResultsTable = new Table();
    leaderboardWindow.add(leaderboardUiTable);

    Label title = new Label("Leaderboard", game.skin, "button");
    leaderboardResultsTable.add(title).padTop(10);
    leaderboardResultsTable.row();

    numberEntriesDisplayed = 5;
    nameLabels = new Label[numberEntriesDisplayed];
    scoreLabels = new Label[numberEntriesDisplayed];

    List<String[]> entries = leaderboardManager.fetchLeaderboardEntries();

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

    TextButton exitButton = new TextButton("Main Menu", game.skin);
    leaderboardUiTable.add(exitButton).bottom().width(300).padTop(10);

    exitButton.addListener(new ChangeListener() {
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
    leaderboardWindow.setX((viewport.getWorldWidth() / 2) - (leaderboardWindow.getWidth() / 2));
    leaderboardWindow.setY((viewport.getWorldHeight() / 2) - (leaderboardWindow.getHeight() / 2));

    setupInputWindow();
  }

  private void setupInputWindow() {
    inputWindow = new Window("", game.skin);
    leaderboardStage.addActor(inputWindow);

    inputWindow.setWidth(600);
    inputWindow.setHeight(600);
    inputWindow.setPosition((game.width / 2f) - inputWindow.getWidth() / 2, (game.height / 2f) - inputWindow.getHeight() / 2);

    Table inputTable = new Table();
    Label prompt = new Label("Type your name", game.skin, "button");
    Label nameLabel = new Label("", game.skin, "button");

    nameLabel.setPosition((inputWindow.getX() / 2f) - nameLabel.getWidth() / 2, (inputWindow.getY() / 2f) - nameLabel.getHeight() / 2);

    inputTable.add(prompt);
    inputTable.row();
    inputTable.add(nameLabel);

    inputWindow.add(inputTable);

    Gdx.input.setInputProcessor(new InputAdapter() {
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
          leaderboardManager.setPlayerName(nameLabel.getText().toString());
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

  private void closeInputLayer() {
    Gdx.input.setInputProcessor(leaderboardStage);
    leaderboardManager.storeLeaderboardEntry(new String[]{leaderboardManager.getPlayerName(), String.format("%f", leaderboardManager.getPlayerScore())});
    updateLeaderboard();
    setInputWindowVisibility(false);
  }

  private void updateLeaderboard() {
    List<String[]> entries = leaderboardManager.fetchLeaderboardEntries();
    List<String[]> sortedEntries = leaderboardManager.getSortedEntries(entries);

    int pos = 0;
    for (int i = sortedEntries.size() - 1; i > (sortedEntries.size() - numberEntriesDisplayed - 1) && i >= 0; i--) {
      nameLabels[pos].setText(String.format("%s", sortedEntries.get(i)[0]));
      scoreLabels[pos].setText(String.format("%.1f", Float.parseFloat(sortedEntries.get(i)[1])) + "%");
      pos++;
    }
  }

  public void setInputWindowVisibility(boolean value) {
    inputWindow.setVisible(value);
  }

  public boolean isInputWindowOpen() {
    return inputWindow.isVisible();
  }

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
    leaderboardManager.setPlayerScore(score);
  }

  @Override
  public float getPlayerScore() {
    return leaderboardManager.getPlayerScore();
  }

  @Override
  public void setPlayerName(String name) {
    leaderboardManager.setPlayerName(name);
  }

  @Override
  public String getPlayerName() {
    return leaderboardManager.getPlayerName();
  }

  @Override
  public void resize(int width, int height) {
    leaderboardStage.getViewport().update(width, height);
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
}
