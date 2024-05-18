package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import java.io.Console;
import java.util.Map;
import java.util.Objects;

/**
 * Handles the majority of the game logic, rendering and user inputs of the game.
 * Responsible for rendering the player and the map, and calling events.
 */
public class GameScreen implements Screen {
    final HustleGame game;
    final GameLogic gameLogic;
    final GameRenderer gameRenderer;
    final GameUI gameUI;
    public final OrthographicCamera camera;
    public Label timeLabel;
    public Label dayLabel;
    public Label hoursRecreationalLabel;
    public Label hoursStudiedLabel;
    public Label mealsEatenLabel;
    public Label hoursSleptLabel;

    public Player player;
    public Window escapeMenu;
    public final Viewport viewport;
    public OrthogonalTiledMapRenderer mapRenderer;
    public Stage uiStage;
    public Label interactionLabel;
//    private OptionDialogue optionDialogue;
    public Table uiTable;
    public Image energyBar;
    public DialogueBox dialogueBox;
    public Image blackScreen;
    private String currentMap = "campus";

    // This syntax is super weird but welcome to Java, each row denotes one entry
    private final Map<String, String> mapPaths = Map.of(
            "campus", "East Campus/east_campus.tmx",
            "town", "Town/town.tmx"
    );
    protected InputMultiplexer inputMultiplexer;


    /**
     *
     * @param game An instance of the class HustleGame containing variables that only need to be loaded or
     *             initialised once.
     * @param avatarChoice Which avatar the player has picked, 0 for the more masculine avatar, 1 for the more feminine
     */
    public GameScreen(final HustleGame game, int avatarChoice) {
        Gdx.app.log("GameScreen", "Creating GameScreen");
        // Important game variables
        this.game = game;
        this.game.gameScreen = this;

        Gdx.app.log("GameScreen", "Creating GameLogic");
        this.gameLogic = new GameLogic(game, this, avatarChoice);
        this.player = gameLogic.getPlayer();

        // Camera and viewport settings
        camera = new OrthographicCamera();
        viewport = new FitViewport(game.WIDTH, game.HEIGHT, camera);
        camera.setToOrtho(false, game.WIDTH, game.HEIGHT);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.app.log("GameScreen", "Creating GameUI");
        this.gameUI = new GameUI(game, this);

        Gdx.app.log("GameScreen", "Creating UI Stage");
        this.gameRenderer = new GameRenderer(game, this);
        Gdx.app.log("GameScreen", "finished creating game ui");
        // USER INTERFACE

        this.uiStage = gameUI.getUiStage();

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
        Gdx.app.log("GameScreen", "make input adapter");

        // Since we need to listen to inputs from the stage and from the keyboard
        // Use an input multiplexer to listen for one inputadapter and then the other
        // inputMultiplexer needs to be established before hand since we reference it on resume() when going
        // back to this screen from the settings menu
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gameKeyBoardInput);
        inputMultiplexer.addProcessor(uiStage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        Gdx.app.log("GameScreen", "game logic setup map");

        gameLogic.setupMap(true);

        Gdx.app.log("GameScreen", "game ui create ui");
        gameUI.create_ui();
        Gdx.app.log("GameScreen", "game ui setup escape menu");
        gameRenderer.initial_render();
    }

    @Override
    public void show() {

    }

    /**
     * Renders the player, updates sound, renders the map and updates any UI elements
     * Called every frame
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render (float delta) {
        // Set delta to a constant value to minimise stuttering issues when moving the camera and player
        // Solution found here: https://www.reddit.com/r/libgdx/comments/5z6qaf/can_someone_help_me_understand_timestepsstuttering/
        delta = 0.016667f;

        gameLogic.update(delta);
        gameRenderer.render(delta);
        gameUI.render(delta);

        // Load timer bar - needs fixing and drawing
        //TextureAtlas blueBar = new TextureAtlas(Gdx.files.internal("Interface/BlueTimeBar/BlueBar.atlas"));
        //Skin blueSkin = new Skin(blueBar);
        //ProgressBar timeBar = new ProgressBar(0, 200, 1, false, blueSkin);
        //timeBar.act(delta);

        // Debug - Draw player hitboxes
//         drawHitboxes();

        // Debug - print the event value of the closest object to the player if there is one
//        if (player.getClosestObject() != null) {
//            System.out.println(player.getClosestObject().get("event"));
//        }
    }


    /**
     * Configures everything needed to display the escape menu window when the player presses 'escape'
     * Doesn't return anything as the variable escapeMenu is used to store the window
     * Takes a table already added to the uiStage
     *
     * @param interfaceStage The stage that the escapeMenu should be added to
     */
    public void setupEscapeMenu(Stage interfaceStage) {
        // Configures an escape menu to display when hitting 'esc'
        // Escape menu
        escapeMenu = new Window("", game.skin);
        interfaceStage.addActor(escapeMenu);
        escapeMenu.setModal(true);

        Table escapeTable = new Table();
        escapeTable.setFillParent(true);

        escapeMenu.add(escapeTable);

        TextButton resumeButton = new TextButton("Resume", game.skin);
        TextButton settingsButton = new TextButton("Settings", game.skin);
        TextButton exitButton = new TextButton("Exit", game.skin);

        escapeTable.add(resumeButton).pad(60, 80, 10, 80).width(300);
        escapeTable.row();
        escapeTable.add(settingsButton).pad(10, 50, 10, 50).width(300);
        escapeTable.row();
        escapeTable.add(exitButton).pad(10, 50, 60, 50).width(300);

        escapeMenu.pack();

        // escapeMenu.setDebug(true);

        // Centre
        escapeMenu.setX((viewport.getWorldWidth() / 2) - (escapeMenu.getWidth() / 2));
        escapeMenu.setY((viewport.getWorldHeight() / 2) - (escapeMenu.getHeight() / 2));


        // Create button listeners

        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (escapeMenu.isVisible()) {
                    game.soundManager.playButton();
                    game.soundManager.playOverworldMusic();
                    escapeMenu.setVisible(false);
                }
            }
        });

        // SETTINGS BUTTON
        // I assign this object to a new var 'thisScreen' since the changeListener overrides 'this'
        // I wasn't sure of a better solution
        Screen thisScreen = this;
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (escapeMenu.isVisible()) {
                    game.soundManager.playButton();
                    game.setScreen(new SettingsScreen(game, thisScreen));
                }
            }
        });

        exitButton.addListener(new ChangeListener() {
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


    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height);
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    /**
     * Called when switching back to this gameScreen
     */
    @Override
    public void resume() {
        // Set the input multiplexer back to this stage
        Gdx.input.setInputProcessor(inputMultiplexer);

        // I'm not sure why, but there's a small bug where exiting the settings menu doesn't make the previous
        // button on the previous screen update, so it's stuck in the 'over' configuration until the
        // user moves the mouse.
        // Uncomment the below line to bring the bug back
        // It's an issue with changing screens, and I can't figure out why it happens, but setting the mouse position
        // to exactly where it is seems to force the stage to update itself and fixes the visual issue.

        Gdx.input.setCursorPosition(Gdx.input.getX(), Gdx.input.getY());
    }

    @Override
    public void hide() {
    }

    /**
     * Disposes of certain elements, called when the game is closed
     */
    @Override
    public void dispose () {
        uiStage.dispose();
        mapRenderer.dispose();
    }

    /**
     * DEBUG - Draws the player's 3 hitboxes
     * Uncomment use at the bottom of render to use
     */
    public void drawHitboxes () {
        game.shapeRenderer.setProjectionMatrix(camera.combined);
        game.shapeRenderer.begin(ShapeType.Line);
        // Sprite
        game.shapeRenderer.setColor(1, 0, 0, 1);
        game.shapeRenderer.rect(player.sprite.x, player.sprite.y, player.sprite.width, player.sprite.height);
        // Feet hitbox
        game.shapeRenderer.setColor(0, 0, 1, 1);
        game.shapeRenderer.rect(player.feet.x, player.feet.y, player.feet.width, player.feet.height);
        // Event hitbox
        game.shapeRenderer.setColor(0, 1, 0, 1);
        game.shapeRenderer.rect(player.eventHitbox.x, player.eventHitbox.y, player.eventHitbox.width, player.eventHitbox.height);
        game.shapeRenderer.end();
    }

    // Functions related to game score and requirements

    /**
     * Generates an InputAdapter to handle game specific keyboard inputs
     *
     * @return An InputAdapter for keyboard inputs
     */
    public InputAdapter makeInputAdapter() {
        return new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                // SHOW ESCAPE MENU CODE
                if (keycode == Input.Keys.ESCAPE) {
                    if (escapeMenu.isVisible()) {
                        game.soundManager.playButton();
                        game.soundManager.playOverworldMusic();
                        escapeMenu.setVisible(false);
                    } else {
                        // game.soundManager.pauseOverworldMusic();
                        game.soundManager.playButton();
                        escapeMenu.setVisible(true);
                    }
                    // Return true to indicate the keydown event was handled
                    return true;
                }

                // SHOW OPTION MENU / ACT ON OPTION MENU CODE
                if (keycode == Input.Keys.E || keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) {
                    if (!escapeMenu.isVisible()) {
                        // If a dialogue box is visible, choose an option or advance text
                        if (dialogueBox.isVisible()) {
                            dialogueBox.enter(gameLogic.eventManager);
                            game.soundManager.playButton();

                        } else if (player.nearObject() && !gameLogic.sleeping) {
                            // If the object has an event associated with it
                            if (player.getClosestObject().get("event") != null) {
                                // Show a dialogue menu asking if they want to do an interaction with the object
                                dialogueBox.show();
                                dialogueBox.getSelectBox().setOptions(new String[]{"Yes", "No"}, new String[]{(String) player.getClosestObject().get("event"), "exit"});
                                if (gameLogic.eventManager.hasCustomObjectInteraction((String) player.getClosestObject().get("event"))) {
                                    dialogueBox.setText(gameLogic.eventManager.getObjectInteraction((String) player.getClosestObject().get("event")));
                                } else {
                                    dialogueBox.setText("Interact with " + player.getClosestObject().get("event") + "?");
                                }
                                dialogueBox.show();
                                dialogueBox.getSelectBox().show();
                                game.soundManager.playDialogueOpen();

                            } else if (player.getClosestObject().get("text") != null) {
                                // Otherwise, if it is a text object, just display its text
                                dialogueBox.show();
                                dialogueBox.setText((String) player.getClosestObject().get("text"));
                            }
                        }
                        return true;
                    }
                }

                // If an option dialogue is open it should soak up all keypresses
                if (dialogueBox.isVisible() && dialogueBox.getSelectBox().isVisible() && !escapeMenu.isVisible()) {
                    // Up or down
                    if (keycode == Input.Keys.W || keycode == Input.Keys.UP) {
                        dialogueBox.getSelectBox().choiceUp();
                    } else if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN) {
                        dialogueBox.getSelectBox().choiceDown();
                    }

                    return true;

                }


                return false;
            }
        };
    }


    /**
     * Ends the game, called at the end of the 7th day, switches to a screen that displays a score
     */
    public void GameOver() {
        game.setScreen(new GameOverScreen(game, gameLogic.hoursStudied, gameLogic.hoursRecreational, gameLogic.hoursSlept, gameLogic.mealsEaten));
    }
}
