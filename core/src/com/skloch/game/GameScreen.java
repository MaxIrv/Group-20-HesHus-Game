package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.skloch.game.events.EventBus;
import com.skloch.game.interfaces.GameLogicInterface;
import com.skloch.game.interfaces.GameRendererInterface;
import com.skloch.game.interfaces.GameScreenProvider;
import com.skloch.game.interfaces.GameUiInterface;
import com.skloch.game.interfaces.PlayerInterface;

/**
 * Handles the majority of the game logic, rendering and user inputs of the game. Responsible for
 * rendering the player and the map, and calling events.
 */
public class GameScreen implements Screen, GameScreenProvider {
  private int studyStreakCounter;
  private int bookWormCounter;
  private int eatStreakCounter;
  private int funStreakCounter;
  private int noSleepCounter;

  final HustleGame game;
  final GameLogicInterface gameLogic;
  final GameRendererInterface gameRenderer;
  final GameUiInterface gameUi;
  final GameMap gameMap;
  final EventBus eventBus;
  private PlayerInterface player;
  protected InputMultiplexer inputMultiplexer;

  /**
   * Constructor for the GameScreen class. Initialises the game, player, map, renderer and UI.
   *
   * @param game         An instance of the class HustleGame containing variables that only need to be
   *                     loaded or initialised once.
   * @param avatarChoice Which avatar the player has picked, 0 for the more masculine avatar, 1 for
   *                     the more feminine avatar.
   */
  public GameScreen(final HustleGame game, int avatarChoice) {
    // Important game variables
    this.game = game;
    this.eventBus = new EventBus();

    this.gameMap = new GameMap();
    this.gameLogic = new GameLogic(game, this, avatarChoice, eventBus);
    this.player = gameLogic.getPlayer();

    this.gameUi = new GameUi(game, this, gameLogic, eventBus, this);

    this.gameRenderer = new GameRenderer(game, eventBus, gameMap);

    // USER INTERFACE

    // Create and center the yes/no box that appears when interacting with objects
    //        optionDialogue = new OptionDialogue("", 400, this.game.skin, game.soundManager);
    //        Window optWin = optionDialogue.getWindow();
    //        optionDialogue.setPos(
    //                (viewport.getWorldWidth() / 2f) - (optWin.getWidth() / 2f),
    //                (viewport.getWorldHeight() / 2f) - (optWin.getHeight() / 2f) - 150
    //        );
    //        // Use addActor to add windows to the scene
    //        uiTable.addActor(optionDialogue.getWindow());
    //        optionDialogue.setVisible(false);

    // Create the keyboard input adapter that defines events to be called based on
    // specific button presses
    InputAdapter gameKeyBoardInput = makeInputAdapter();

    // Since we need to listen to inputs from the stage and from the keyboard
    // Use an input multiplexer to listen for one inputadapter and then the other
    // inputMultiplexer needs to be established before hand since we reference it on resume() when
    // going
    // back to this screen from the settings menu
    inputMultiplexer = new InputMultiplexer();
    inputMultiplexer.addProcessor(gameKeyBoardInput);
    inputMultiplexer.addProcessor(gameUi.getUiStage());
    Gdx.input.setInputProcessor(inputMultiplexer);

    gameLogic.setupMap(true, gameMap);

    gameUi.createUi(gameRenderer.getWorldWidth(), gameRenderer.getWorldHeight());

    gameRenderer.initialRender();
  }

  @Override
  public void show() {
  }

  /**
   * Renders the player, updates sound, renders the map and updates any UI elements Called every
   * frame.
   *
   * @param delta The time in seconds since the last render.
   */
  @Override
  public void render(float delta) {
    // Set delta to a constant value to minimise stuttering issues when moving the camera and player
    // Solution found here:
    // https://www.reddit.com/r/libgdx/comments/5z6qaf/can_someone_help_me_understand_timestepsstuttering/
    delta = 0.016667f;

    gameLogic.update(delta);
    gameRenderer.render(delta, gameLogic.getPlayer(), gameMap);
    gameUi.renderUi(delta);

    // Load timer bar - needs fixing and drawing
    // TextureAtlas blueBar = new
    // TextureAtlas(Gdx.files.internal("Interface/BlueTimeBar/BlueBar.atlas"));
    // Skin blueSkin = new Skin(blueBar);
    // ProgressBar timeBar = new ProgressBar(0, 200, 1, false, blueSkin);
    // timeBar.act(delta);

    // Debug - Draw player hitboxes
    //         drawHitboxes();

    // Debug - print the event value of the closest object to the player if there is one
    //        if (player.getClosestObject() != null) {
    //            System.out.println(player.getClosestObject().get("event"));
    //        }
  }

  @Override
  public boolean isDialogueBoxVisible() {
    return gameUi.getDialogueBox().isVisible();
  }

  public boolean isEscapeMenuVisible() {
    return gameUi.isEscapeMenuVisible();
  }

  @Override
  public boolean isPlayerNearObject() {
    return gameLogic.isPlayerNearObject();
  }

  @Override
  public boolean isPlayerSleeping() {
    return gameLogic.isSleeping();
  }

  @Override
  public GameObject getPlayerClosestObject() {
    return gameLogic.getPlayerClosestObject();
  }

  @Override
  public PlayerInterface getPlayer() {
    return player;
  }

  public void blackScreenAction() {
  }

  @Override
  public GameMap getGameMap() {
    return gameMap;
  }

  @Override
  public void resize(int width, int height) {
    gameUi.resizeUi(width, height);
    gameRenderer.resizeViewport(width, height);
  }

  @Override
  public void pause() {
  }

  /**
   * Called when switching back to this gameScreen.
   */
  @Override
  public void resume() {
    // Set the input multiplexer back to this stage
    Gdx.input.setInputProcessor(inputMultiplexer);

    // I'm not sure why, but there's a small bug where exiting the settings menu doesn't make the
    // previous
    // button on the previous screen update, so it's stuck in the 'over' configuration until the
    // user moves the mouse.
    // Uncomment the below line to bring the bug back
    // It's an issue with changing screens, and I can't figure out why it happens, but setting the
    // mouse position
    // to exactly where it is seems to force the stage to update itself and fixes the visual issue.

    Gdx.input.setCursorPosition(Gdx.input.getX(), Gdx.input.getY());
  }

  @Override
  public void hide() {
  }

  /**
   * Disposes of certain elements, called when the game is closed.
   */
  @Override
  public void dispose() {
    gameUi.dispose();
    gameRenderer.dispose();
    gameMap.dispose();
  }

  //  /** DEBUG - Draws the player's 3 hitboxes Uncomment use at the bottom of render to use */
  //    public void drawHitboxes () {
  //        game.shapeRenderer.setProjectionMatrix(camera.combined);
  //        game.shapeRenderer.begin(ShapeType.Line);
  //        // Sprite
  //        game.shapeRenderer.setColor(1, 0, 0, 1);
  //        game.shapeRenderer.rect(player.sprite.x, player.sprite.y, player.sprite.width,
  // player.sprite.height);
  //        // Feet hitbox
  //        game.shapeRenderer.setColor(0, 0, 1, 1);
  //        game.shapeRenderer.rect(player.feet.x, player.feet.y, player.feet.width,
  // player.feet.height);
  //        // Event hitbox
  //        game.shapeRenderer.setColor(0, 1, 0, 1);
  //        game.shapeRenderer.rect(player.eventHitbox.x, player.eventHitbox.y,
  // player.eventHitbox.width, player.eventHitbox.height);
  //        game.shapeRenderer.end();
  //    }

  // Functions related to game score and requirements

  /**
   * Generates an InputAdapter to handle game specific keyboard inputs.
   *
   * @return An InputAdapter for keyboard inputs
   */
  public InputAdapter makeInputAdapter() {
    return new InputAdapter() {
      @Override
      public boolean keyDown(int keycode) {
        // SHOW ESCAPE MENU CODE
        if (keycode == Input.Keys.ESCAPE) {
          if (gameUi.isEscapeMenuVisible()) {
            game.soundManager.playButton();
            game.soundManager.playOverworldMusic();
            gameUi.setEscapeMenuVisible(false);
          } else {
            // game.soundManager.pauseOverworldMusic();
            game.soundManager.playButton();
            gameUi.setEscapeMenuVisible(true);
          }
          // Return true to indicate the keydown event was handled
          return true;
        }

        // SHOW OPTION MENU / ACT ON OPTION MENU CODE
        if (keycode == Input.Keys.E || keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) {
          if (!gameUi.isEscapeMenuVisible()) {
            // If a dialogue box is visible, choose an option or advance text
            if (gameUi.getDialogueBox().isVisible()) {
              gameUi.getDialogueBox().enter(gameLogic.getEventManager());
              game.soundManager.playButton();

            } else if (player.nearObject() && !gameLogic.isSleeping()) {
              // If the object has an event associated with it
              if (player.getClosestObject().get("event") != null) {
                // Show a dialogue menu asking if they want to do an interaction with the object
                gameUi.getDialogueBox().show();
                gameUi
                    .getDialogueBox()
                    .getSelectBox()
                    .setOptions(
                        new String[] {"Yes", "No"},
                        new String[] {(String) player.getClosestObject().get("event"), "exit"});
                if (gameLogic
                    .getEventManager()
                    .hasCustomObjectInteraction((String) player.getClosestObject().get("event"))) {
                  gameUi
                      .getDialogueBox()
                      .setText(
                          gameLogic
                              .getEventManager()
                              .getObjectInteraction(
                                  (String) player.getClosestObject().get("event")));
                } else {
                  gameUi
                      .getDialogueBox()
                      .setText("Interact with " + player.getClosestObject().get("event") + "?");
                }
                gameUi.getDialogueBox().show();
                gameUi.getDialogueBox().getSelectBox().show();
                game.soundManager.playDialogueOpen();

              } else if (player.getClosestObject().get("text") != null) {
                // Otherwise, if it is a text object, just display its text
                gameUi.getDialogueBox().show();
                gameUi.getDialogueBox().setText((String) player.getClosestObject().get("text"));
              }
            }
            return true;
          }
        }

        // If an option dialogue is open it should soak up all keypresses
        if (gameUi.getDialogueBox().isVisible()
            && gameUi.getDialogueBox().getSelectBox().isVisible()
            && !gameUi.isEscapeMenuVisible()) {
          // Up or down
          if (keycode == Input.Keys.W || keycode == Input.Keys.UP) {
            gameUi.getDialogueBox().getSelectBox().choiceUp();
          } else if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN) {
            gameUi.getDialogueBox().getSelectBox().choiceDown();
          }

          return true;
        }

        return false;
      }
    };
  }

  // Study Streak
  public int getStudyStreakCounter() {
    return this.studyStreakCounter;
  }

  public void setStudyStreakCounter(int x) {
    this.studyStreakCounter = x;
  }

  public void addStudyStreakCounter(int x) {
    this.studyStreakCounter += x;
  }

  // Bookworm
  public int getBookWormCounter() {
    return this.bookWormCounter;
  }

  public void setBookWormCounter(int x) {
    this.bookWormCounter = x;
  }

  public void addBookWormCounter(int x) {
    this.bookWormCounter += x;
  }

  // Eat Streak
  public int getEatStreakCounter() {
    return this.eatStreakCounter;
  }

  public void setEatStreakCounter(int x) {
    this.eatStreakCounter = x;
  }

  public void addEatStreakCounter(int x) {
    this.eatStreakCounter += x;
  }

  // Fun Streak
  public int getFunStreakCounter() {
    return this.funStreakCounter;
  }

  public void setFunStreakCounter(int x) {
    this.funStreakCounter = x;
  }

  public void addFunStreakCounter(int x) {
    this.funStreakCounter += x;
  }

  // All Nighter
  public int getNoSleepCounter() {
    return this.noSleepCounter;
  }

  public void setNoSleepCounter(int x) {
    this.noSleepCounter = x;
  }

  public void addNoSleepCounter(int x) {
    this.noSleepCounter += x;
  }

  /**
   * Ends the game, called at the end of the 7th day, switches to a screen that displays a score.
   */
  public void gameOver() {
    game.setScreen(
        new GameOverScreen(
            game,
            gameLogic.getHoursStudied(),
            gameLogic.getHoursRecreational(),
            gameLogic.getHoursSlept(),
            gameLogic.getMealsEaten(),
            gameLogic.getPlayerScore()));
  }
}
