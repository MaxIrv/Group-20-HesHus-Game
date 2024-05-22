package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Currently doesn't calculate a score
 */
public class LeaderboardScreen implements Screen {
    private HustleGame game;
    Stage leaderboardStage;
    private Window inputWindow;

    private String playerName;
    Viewport viewport;
    OrthographicCamera camera;
    ScrollPane scrollPane;
    String leaderboardFile;

    /**
     * A screen to display a 'Leaderboard' screen when the player has finished the game.
     *
     * @param game An instance of HustleGame
     */
    public LeaderboardScreen (final HustleGame game) {
        this.game = game;

        leaderboardStage= new Stage(new FitViewport(game.WIDTH, game.HEIGHT));
        camera = new OrthographicCamera();
        viewport = new FitViewport(game.WIDTH, game.HEIGHT, camera);
        camera.setToOrtho(false, game.WIDTH, game.HEIGHT);

        // Create the window
        Window leaderboardWindow = new Window("", game.skin);
        leaderboardStage.addActor(leaderboardWindow);

        // Table for UI elements in window
        Table leaderboardTable = new Table();
        leaderboardWindow.add(leaderboardTable);

        leaderboardTable.row();

        // Title
        Label title = new Label("Leaderboard", game.skin, "button");
        leaderboardTable.add(title).padTop(10);
        leaderboardTable.row();

        // Exit button
        TextButton exitButton = new TextButton("Main Menu", game.skin);
        leaderboardTable.row();
        leaderboardTable.add(exitButton).bottom().width(300).padTop(10);


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

        // Centre the window
        leaderboardWindow.setX((viewport.getWorldWidth() / 2) - (leaderboardWindow.getWidth() / 2));
        leaderboardWindow.setY((viewport.getWorldHeight() / 2) - (leaderboardWindow.getHeight() / 2));

        setupInputWindow();

    }

    /**
     * Set up the input layer and display it.
     */
    private void setupInputWindow(){
        //Group nameInputGroup = new Group();
        //leaderboardStage.addActor(nameInputGroup);

        // Grey background
        Image inputModeLayer = new Image(new Texture(Gdx.files.internal("Interface/transparent_grey.png")));
        inputModeLayer.setBounds(0,0,game.WIDTH, game.HEIGHT);
        inputModeLayer.setOrigin(game.WIDTH/2f,game.HEIGHT/2f);

        inputWindow = new Window("",game.skin);
        leaderboardStage.addActor(inputWindow);
        inputWindow.setWidth(300);
        inputWindow.setPosition((game.WIDTH/2f)-inputWindow.getWidth()/2,
                (game.HEIGHT/2f)-inputWindow.getHeight()/2);
        //nameInputGroup.addActor(inputBox);

        Label nameLabel = new Label("", game.skin);
        nameLabel.setPosition(
                (inputWindow.getX()/2f) - inputWindow.getWidth()/2,
                (inputWindow.getY()/2f) - inputWindow.getHeight()/2);
        inputWindow.addActor(nameLabel);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyTyped(char character){
                if (Character.isLetter(character) || Character.valueOf(character).equals(' ')){
                    String current_text = nameLabel.getText().toString();
                    if (current_text.length() <= 10){
                        nameLabel.setText(current_text+character);
                    }
                }
                return false;
            }

            @Override
            public boolean keyDown(int keycode){
                if (keycode == Input.Keys.ENTER){
                    closeInputLayer();
                    playerName = nameLabel.getText().toString();
                }
                if (keycode == Input.Keys.BACKSPACE){
                    String current_label = nameLabel.getText().toString();
                    nameLabel.setText(current_label.substring(0,current_label.length()-1));

                }
                return false;
            }

        });
    }

    /**
     * Close the inputLayer
     *
     */
    private void closeInputLayer(){
        Gdx.input.setInputProcessor(leaderboardStage);
        setInputWindowVisibility(false);
    }

    /**
     * Set the state of the inputLayer
     * @param value the value to change to
     */
    public void setInputWindowVisibility(boolean value){
        inputWindow.setVisible(value);
    }

    /**
     * Is the inputLayer open and displayed
     * @return whether the inputLayer is open
     */
    public boolean isInputWindowOpen(){
        return inputWindow.isVisible();
    }

    /**
     * Reads and returns text read from the provided text file path
     * @param filepath The path to the text file
     * @return The contents of the file as a String
     */
    public String readTextFile(String filepath) {
        FileHandle file = Gdx.files.internal(filepath);

        if (!file.exists()) {
            System.out.println("WARNING: Couldn't load file " + filepath);
            return "Couldn't load " + filepath;
        } else {
            return file.readString();
        }
    }

    /**
     * Get a list of leaderboard entries from the csv file.
     * @param filepath The path of the leaderboard csv
     * @return A list of string arrays
     */
    private List<String[]>  fetchLeaderboardEntries(String filepath){
        FileHandle file = Gdx.files.internal(filepath);
        List<String[]> entries = new ArrayList<>();
        if (!file.exists()) {
            System.out.println("WARNING: Couldn't load file " + filepath);
            // If the leaderboard doesn't exist then return an empty list.
        }
        else{
            String text = file.readString();
            List<String> lines = Arrays.asList(text.split("\\r?\\n"));
            for (String line : lines) {
                entries.add(line.split(","));
            }
        }
        return entries;
    }

    /**
     * Append leaderboard entry to the csv file
     * @param filepath The path of the leaderboard csv
     * @param entry a single entry as a string array
     */
    private void storeLeaderboardEntry(String filepath,String[] entry){
        FileHandle file = Gdx.files.internal(filepath);
        String writeString = String.join(",",entry);
        writeString += "\n";
    }

    /**
     * Renders the screen and the background each frame
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

    /**
     * Correctly resizes the onscreen elements when the window is resized
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {
        leaderboardStage.getViewport().update(width, height);
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
