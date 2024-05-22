package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.skloch.game.events.DayUpdatedEvent;
import com.skloch.game.events.EnergyUpdatedEvent;
import com.skloch.game.events.EventBus;
import com.skloch.game.events.FadeBlackScreenEvent;
import com.skloch.game.events.GameStatsUpdatedEvent;
import com.skloch.game.events.TimeUpdatedEvent;
import com.skloch.game.events.dialoguebox.DialogueScrollEvent;
import com.skloch.game.events.dialoguebox.DialogueSetOptions;
import com.skloch.game.events.dialoguebox.DialogueSetText;
import com.skloch.game.events.dialoguebox.DialogueUpdateState;
import com.skloch.game.interfaces.GameLogicInterface;
import com.skloch.game.interfaces.GameScreenProvider;

/**
 * A class to display the game's user interface, including the energy bar, time, stats, and dialogue
 * box. This class is called by the GameScreen class to render the GUI.
 */
public class GameUiInterface implements com.skloch.game.interfaces.GameUiInterface {
  private final HustleGame game;
  private final GameScreen gameScreen;
  private final GameLogicInterface gameLogic;
  private final GameScreenProvider gameScreenProvider;
  private final Stage uiStage;

  private final Label timeLabel;
  private final Label dayLabel;
  private final Label hoursRecreationalLabel;
  private final Label hoursStudiedLabel;
  private final Label mealsEatenLabel;
  private final Label hoursSleptLabel;
  private final Label interactionLabel;

  private Image energyBar;
  private Window escapeMenu;
  private Image blackScreen;

  public Table uiTable;
  public final DialogueBox dialogueBox;

  /**
   * Constructs a GameUI object to display the game's user interface, including the energy bar,
   * time, stats, and dialogue box.
   *
   * @param game The game object
   * @param gameScreen The game screen object
   * @param gameLogic The game logic object
   * @param eventBus The event bus for subscribing to game events
   * @param gameScreenProvider The game screen provider
   */
  public GameUiInterface(
      HustleGame game,
      GameScreen gameScreen,
      GameLogicInterface gameLogic,
      EventBus eventBus,
      GameScreenProvider gameScreenProvider) {
    this.game = game;
    this.gameScreen = gameScreen;
    this.gameLogic = gameLogic;
    this.gameScreenProvider = gameScreenProvider;

    // Create a stage for the user interface to be on
    this.uiStage = new Stage(new FitViewport(game.width, game.height));

    this.timeLabel = new Label(formatTime((int) gameLogic.getDaySeconds()), game.skin, "time");
    this.dayLabel = new Label(String.format("Day %d", gameLogic.getDay()), game.skin, "day");
    this.hoursRecreationalLabel =
        new Label(
            String.format("Played for %d hours", gameLogic.getHoursRecreational()),
            game.skin,
            "day");
    this.hoursStudiedLabel =
        new Label(
            String.format("Studied for %d hours", gameLogic.getHoursStudied()), game.skin, "day");
    this.mealsEatenLabel =
        new Label(String.format("Eaten %d times", gameLogic.getMealsEaten()), game.skin, "day");
    this.hoursSleptLabel =
        new Label(String.format("Slept for %d hours", gameLogic.getHoursSlept()), game.skin, "day");
    this.interactionLabel = new Label("E - Interact", game.skin, "default");

    // Dialogue box
    this.dialogueBox = new DialogueBox(game.skin);

    // Subscribe to events
    eventBus.subscribe(TimeUpdatedEvent.class, this::onTimeUpdated);
    eventBus.subscribe(DayUpdatedEvent.class, this::onDayUpdated);
    eventBus.subscribe(GameStatsUpdatedEvent.class, this::onGameStatsUpdated);
    eventBus.subscribe(EnergyUpdatedEvent.class, this::onEnergyUpdated);
    eventBus.subscribe(FadeBlackScreenEvent.class, this::fadeBlackScreenEvent);
    eventBus.subscribe(DialogueSetText.class, this::dialogueSetText);
    eventBus.subscribe(DialogueSetOptions.class, this::dialogueSetOptions);
    eventBus.subscribe(DialogueUpdateState.class, this::updateDialogueBox);
    eventBus.subscribe(DialogueScrollEvent.class, this::dialogueScroll);
  }

  /**
   * Handles the DialogueSetText event to set the text in the dialogue box.
   *
   * @param event The DialogueSetText event
   */
  private void dialogueSetText(DialogueSetText event) {
    String text = event.getText();
    String eventKey = event.getEventKey();
    if (eventKey != null) {
      dialogueBox.setText(text, eventKey);
    } else {
      dialogueBox.setText(text);
    }
  }

  /**
   * Handles the DialogueSetOptions event to set the options in the dialogue box.
   *
   * @param event The DialogueSetOptions event
   */
  private void dialogueSetOptions(DialogueSetOptions event) {
    String[] options = event.getOptions();
    String[] events = event.getEvents();
    dialogueBox.getSelectBox().setOptions(options, events);
  }

  /**
   * Handles the DialogueUpdateState event to update the state of the dialogue box.
   *
   * @param event The DialogueUpdateState event
   */
  private void updateDialogueBox(DialogueUpdateState event) {
    DialogueUpdateState.State state = event.getState();
    switch (state) {
      case HIDE:
        dialogueBox.hide();
        break;
      case HIDE_SELECT_BOX:
        dialogueBox.hideSelectBox();
        break;
      case SHOW:
        dialogueBox.show();
        break;
      default:
        break;
    }
  }

  /**
   * Handles the DialogueScrollEvent to scroll the text in the dialogue box.
   *
   * @param event The DialogueScrollEvent
   */
  private void dialogueScroll(DialogueScrollEvent event) {
    dialogueBox.scrollText(event.getScrollSpeed());
  }

  /**
   * Creates the UI elements, including the energy bar, time, stats, and dialogue box.
   *
   * @param worldWidth The width of the world
   * @param worldHeight The height of the world
   */
  @Override
  public void create_ui(float worldWidth, float worldHeight) {
    // Add a black image over everything first
    blackScreen = new Image(new Texture(Gdx.files.internal("Sprites/black_square.png")));
    blackScreen.setSize(worldWidth, worldHeight);
    blackScreen.addAction(Actions.alpha(0f));

    dialogueBox.setPos((worldWidth - dialogueBox.getWidth()) / 2f, 15f);
    dialogueBox.hide();

    // UI table to put everything in
    uiTable = new Table();
    uiTable.setSize(game.width, game.height);
    uiStage.addActor(uiTable);

    // Load energy bar elements
    Group energyGroup = new Group();
    energyGroup.setDebug(true);
    energyBar = new Image(new Texture(Gdx.files.internal("Interface/Energy Bar/green_bar.png")));
    Image energyBarOutline =
        new Image(new Texture(Gdx.files.internal("Interface/Energy Bar/bar_outline.png")));
    energyBarOutline.setPosition(worldWidth - energyBarOutline.getWidth() - 15, 15);
    energyBar.setPosition(energyBarOutline.getX() + 16, energyBarOutline.getY() + 16);
    energyGroup.addActor(energyBar);
    energyGroup.addActor(energyBarOutline);

    Table statsTable = new Table();
    statsTable.setFillParent(true);

    statsTable.add(mealsEatenLabel).top().right();
    statsTable.row();
    statsTable.add(hoursStudiedLabel).top().right();
    statsTable.row();
    statsTable.add(hoursRecreationalLabel).top().right();
    statsTable.row();
    statsTable.add(hoursSleptLabel).top().right();
    statsTable.top().right().padRight(10).padTop(10);

    // Table to display date and time
    Table timeTable = new Table();
    timeTable.setFillParent(true);
    timeTable.add(timeLabel).uniformX();
    timeTable.row();
    timeTable.add(dayLabel).uniformX().left().padTop(2);
    timeTable.top().left().padLeft(10).padTop(10);

    // Set the order of rendered UI elements
    uiTable.add(interactionLabel).padTop(300);
    uiStage.addActor(statsTable);
    uiStage.addActor(energyGroup);
    uiStage.addActor(timeTable);
    uiStage.addActor(blackScreen);
    uiStage.addActor(dialogueBox.getWindow());
    uiStage.addActor(dialogueBox.getSelectBox().getWindow());

    setupEscapeMenu(worldWidth, worldHeight);

    // Display a little good morning message
    dialogueBox.show();
    dialogueBox.setText(gameLogic.getWakeUpMessage());
  }

  /**
   * Updates the UI elements, including the energy bar, time, stats, and dialogue box.
   *
   * @param delta The time since the last frame
   */
  @Override
  public void render_ui(float delta) {
    check_interaction();

    // Update UI elements
    uiStage.getViewport().apply();
    uiStage.act(delta);
    uiStage.draw();
  }

  /** Checks if the player is near an object and displays the interaction label. */
  private void check_interaction() {
    interactionLabel.setVisible(false);
    if (!gameScreenProvider.isDialogueBoxVisible()
        && !isEscapeMenuVisible()
        && !gameScreenProvider.isPlayerSleeping()) {
      if (gameScreenProvider.isPlayerNearObject()) {
        interactionLabel.setVisible(true);
        // Change text whether pressing E will interact or just read text
        if (gameScreenProvider.getPlayerClosestObject().get("event") != null) {
          interactionLabel.setText("E - Interact");
        } else if (gameScreenProvider.getPlayerClosestObject().get("text") != null) {
          interactionLabel.setText("E - Read Sign");
        }
      }
    }
  }

  /**
   * Resizes the UI elements to fit the screen.
   *
   * @param width The new width of the screen
   * @param height The new height of the screen
   */
  @Override
  public void resize_ui(int width, int height) {
    uiStage.getViewport().update(width, height, true);
  }

  @Override
  public Stage getUiStage() {
    return uiStage;
  }

  @Override
  public void dispose() {
    uiStage.dispose();
  }

  /**
   * Handles the TimeUpdatedEvent to update the time label.
   *
   * @param event The TimeUpdatedEvent
   */
  private void onTimeUpdated(TimeUpdatedEvent event) {
    timeLabel.setText(formatTime((int) event.getDaySeconds()));
  }

  /**
   * Handles the DayUpdatedEvent to update the day label.
   *
   * @param event The DayUpdatedEvent
   */
  private void onDayUpdated(DayUpdatedEvent event) {
    dayLabel.setText(String.format("Day %d", event.getDay()));
  }

  /**
   * Handles the GameStatsUpdatedEvent to update the game stats labels.
   *
   * @param event The GameStatsUpdatedEvent
   */
  private void onGameStatsUpdated(GameStatsUpdatedEvent event) {
    timeLabel.setText(formatTime((int) event.getDaySeconds()));
    dayLabel.setText(String.format("Day %d", event.getDay()));
    hoursRecreationalLabel.setText(
        String.format("Played for %d hours", event.getHoursRecreational()));
    hoursStudiedLabel.setText(String.format("Studied for %d hours", event.getHoursStudied()));
    mealsEatenLabel.setText(String.format("Eaten %d times", event.getMealsEaten()));
    hoursSleptLabel.setText(String.format("Slept for %d hours", event.getHoursSlept()));
  }

  /**
   * Handles the EnergyUpdatedEvent to update the energy bar.
   *
   * @param event The EnergyUpdatedEvent
   */
  private void onEnergyUpdated(EnergyUpdatedEvent event) {
    energyBar.setScaleY(event.getEnergy() / 100f);
  }

  /**
   * Handles the FadeBlackScreenEvent to fade the black screen.
   *
   * @param event The FadeBlackScreenEvent
   */
  private void fadeBlackScreenEvent(FadeBlackScreenEvent event) {
    Action fadeInAction = event.getAction();
    RunnableAction runnableAction = event.getRunnableAction();

    if (runnableAction != null) {
      blackScreen.addAction(Actions.sequence(fadeInAction, runnableAction));
    } else {
      blackScreen.addAction(fadeInAction);
    }
  }

  /**
   * Formats a number of seconds into a 12-hour clock time.
   *
   * @param seconds The number of seconds
   * @return The time in 12-hour clock format
   */
  private String formatTime(int seconds) {
    int hour = Math.floorDiv(seconds, 60);
    String minutes = String.format("%02d", (seconds - hour * 60));

    // Make 12-hour clock format
    if (hour == 24 || hour == 0) {
      return String.format("12:%sam", minutes);
    } else if (hour == 12) {
      return String.format("12:%spm", minutes);
    } else if (hour > 12) {
      return String.format("%d:%spm", hour - 12, minutes);
    } else {
      return String.format("%d:%sam", hour, minutes);
    }
  }

  /**
   * Checks if the escape menu is visible.
   *
   * @return true if the escape menu is visible, false otherwise
   */
  public boolean isEscapeMenuVisible() {
    return escapeMenu.isVisible();
  }

  /**
   * Sets the visibility of the escape menu.
   *
   * @param visible true to make the escape menu visible, false to hide it
   */
  public void setEscapeMenuVisible(boolean visible) {
    escapeMenu.setVisible(visible);
  }

  /**
   * Configures everything needed to display the escape menu window when the player presses
   * 'escape'.
   *
   * @param worldWidth The width of the world
   * @param worldHeight The height of the world
   */
  private void setupEscapeMenu(float worldWidth, float worldHeight) {
    escapeMenu = new Window("", game.skin);
    uiStage.addActor(escapeMenu);
    escapeMenu.setModal(true);

    Table escapeTable = new Table();
    escapeTable.setFillParent(true);
    escapeMenu.add(escapeTable);

    TextButton resumeButton = new TextButton("Resume", game.skin);
    escapeTable.add(resumeButton).pad(60, 80, 10, 80).width(300);
    escapeTable.row();

    TextButton settingsButton = new TextButton("Settings", game.skin);
    escapeTable.add(settingsButton).pad(10, 50, 10, 50).width(300);
    escapeTable.row();

    TextButton exitButton = new TextButton("Exit", game.skin);
    escapeTable.add(exitButton).pad(10, 50, 60, 50).width(300);

    escapeMenu.pack();
    escapeMenu.setX((worldWidth / 2) - (escapeMenu.getWidth() / 2));
    escapeMenu.setY((worldHeight / 2) - (escapeMenu.getHeight() / 2));

    // Create button listeners
    resumeButton.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            if (escapeMenu.isVisible()) {
              game.soundManager.playButton();
              game.soundManager.playOverworldMusic();
              escapeMenu.setVisible(false);
            }
          }
        });

    settingsButton.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            if (escapeMenu.isVisible()) {
              game.soundManager.playButton();
              game.setScreen(new SettingsScreen(game, gameScreen));
            }
          }
        });

    exitButton.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            if (escapeMenu.isVisible()) {
              game.soundManager.playButton();
              game.soundManager.stopOverworldMusic();
              dispose();
              game.setScreen(new MenuScreen(game));
            }
          }
        });

    escapeMenu.setVisible(false);
  }

  /**
   * Returns the dialogue box.
   *
   * @return The dialogue box
   */
  public DialogueBox getDialogueBox() {
    return dialogueBox;
  }
}
