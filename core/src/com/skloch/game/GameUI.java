package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameUI {
    private final HustleGame game;
    private final GameScreen gameScreen;
    public Table uiTable;
    public Stage uiStage;

    public GameUI(HustleGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;

        // Create a stage for the user interface to be on
        uiStage = new Stage(new FitViewport(game.WIDTH, game.HEIGHT));
        // Add a black image over everything first
        gameScreen.blackScreen = new Image(new Texture(Gdx.files.internal("Sprites/black_square.png")));
        gameScreen.blackScreen.setSize(gameScreen.viewport.getWorldWidth(), gameScreen.viewport.getWorldHeight());
        gameScreen.blackScreen.addAction(Actions.alpha(0f));


    }

    public void create_ui() {
        // UI table to put everything in
        gameScreen.uiTable = new Table();
        gameScreen.uiTable.setSize(game.WIDTH, game.HEIGHT);
        uiStage.addActor(gameScreen.uiTable);

        // Interaction label to prompt player
        gameScreen.interactionLabel = new Label("E - Interact", game.skin, "default");

        // Dialogue box
        gameScreen.dialogueBox = new DialogueBox(game.skin);
        gameScreen.dialogueBox.setPos(
                (gameScreen.viewport.getWorldWidth() - gameScreen.dialogueBox.getWidth()) / 2f,
                15f);
        gameScreen.dialogueBox.hide();

        // Load energy bar elements
        Group energyGroup = new Group();
        energyGroup.setDebug(true);
        gameScreen.energyBar = new Image(new Texture(Gdx.files.internal("Interface/Energy Bar/green_bar.png")));
        Image energyBarOutline = new Image(new Texture(Gdx.files.internal("Interface/Energy Bar/bar_outline.png")));
        energyBarOutline.setPosition(gameScreen.viewport.getWorldWidth()-energyBarOutline.getWidth() - 15, 15);
        gameScreen.energyBar.setPosition(energyBarOutline.getX()+16, energyBarOutline.getY()+16);
        energyGroup.addActor(gameScreen.energyBar);
        energyGroup.addActor(energyBarOutline);

        //Group statsGroup = new Group();
        Table statsTable = new Table();
        statsTable.setFillParent(true);

        gameScreen.mealsEatenLabel = new Label(String.format("Eaten %d times",gameScreen.gameLogic.mealsEaten), game.skin, "day");
        gameScreen.hoursStudiedLabel = new Label(String.format("Studied for %d hours",gameScreen.gameLogic.hoursStudied),game.skin,"day");
        gameScreen.hoursRecreationalLabel = new Label(String.format("Played for %d hours",gameScreen.gameLogic.hoursRecreational),game.skin,"day");
        gameScreen.hoursSleptLabel = new Label(String.format("Slept for %d hours",gameScreen.gameLogic.hoursSlept),game.skin,"day");

        statsTable.add(gameScreen.mealsEatenLabel).top().right();
        statsTable.row();
        statsTable.add(gameScreen.hoursStudiedLabel).top().right();
        statsTable.row();
        statsTable.add(gameScreen.hoursRecreationalLabel).top().right();
        statsTable.row();
        statsTable.add(gameScreen.hoursSleptLabel).top().right();
        statsTable.top().right().padRight(10).padTop(10);

        // Table to display date and time
        Table timeTable = new Table();
        timeTable.setFillParent(true);
        gameScreen.timeLabel = new Label(gameScreen.gameLogic.formatTime((int) gameScreen.gameLogic.daySeconds), game.skin, "time");
        gameScreen.dayLabel = new Label(String.format("Day %d", gameScreen.gameLogic.day), game.skin, "day");
        timeTable.add(gameScreen.timeLabel).uniformX();
        timeTable.row();
        timeTable.add(gameScreen.dayLabel).uniformX().left().padTop(2);
        timeTable.top().left().padLeft(10).padTop(10);

        // Set the order of rendered UI elements
        gameScreen.uiTable.add(gameScreen.interactionLabel).padTop(300);
        uiStage.addActor(statsTable);
        uiStage.addActor(energyGroup);
        uiStage.addActor(timeTable);
        uiStage.addActor(gameScreen.blackScreen);
        uiStage.addActor(gameScreen.dialogueBox.getWindow());
        uiStage.addActor(gameScreen.dialogueBox.getSelectBox().getWindow());
        gameScreen.setupEscapeMenu(uiStage);


    }

    public void render(float delta) {
        check_interaction();

        // Update UI elements
        uiStage.getViewport().apply();
        uiStage.act(delta);
        uiStage.draw();
    }

    /**
        * Check if the player is near an object and display the interaction label
     */
    private void check_interaction() {
        gameScreen.interactionLabel.setVisible(false);
        if (!gameScreen.dialogueBox.isVisible() && !gameScreen.escapeMenu.isVisible() && !gameScreen.gameLogic.sleeping) {
            if (gameScreen.player.nearObject()) {
                gameScreen.interactionLabel.setVisible(true);
                // Change text whether pressing E will interact or just read text
                if (gameScreen.player.getClosestObject().get("event") != null) {
                    gameScreen.interactionLabel.setText("E - Interact");
                } else if (gameScreen.player.getClosestObject().get("text") != null) {
                    gameScreen.interactionLabel.setText("E - Read Sign");
                }
            }
        }
    }

    public Stage getUiStage() {
        return uiStage;
    }
}
