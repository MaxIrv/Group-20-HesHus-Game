package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class SettingsScreen implements Screen {
    private HustleGame game;
    private Stage optionStage;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Window optionMenu;
    public Slider musicSlider;
    public Slider sfxSlider;

    // Use an object since we don't know what type of screen we will be passed
    public Screen previousScreen;


    public SettingsScreen(final HustleGame game, Screen previousScreen) {
        // An option screen to let the player adjust the volume of music and sound effects
        this.game = game;
        this.previousScreen = previousScreen;
        optionStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(optionStage);

        camera = new OrthographicCamera();
        viewport = new FitViewport(game.WIDTH, game.HEIGHT, camera);
        camera.setToOrtho(false, game.WIDTH, game.HEIGHT);

        // Create the window
        optionMenu = new Window("", game.skin);
        optionStage.addActor(optionMenu);
        optionMenu.setModal(true);

        // Table for UI elements
        Table optionTable = new Table();
        optionMenu.add(optionTable).prefHeight(600);

        // Create all the UI elements
        // musicSlider and sfxSlider need to be accessible in render so they are already declared
        TextButton exitButton = new TextButton("Exit", game.skin);
        Label title = new Label("Settings", game.skin, "button");
        Label musicTitle = new Label("Music Volume", game.skin, "interaction");
        musicSlider = new Slider(0, 100, 1, false, game.skin, "default-horizontal");
        Label sfxTitle = new Label("SFX Volume", game.skin, "interaction");
        sfxSlider = new Slider(0, 100, 1, false, game.skin, "default-horizontal");
        Table sliderTable = new Table();
        // optionTable.setDebug(true);
        // sliderTable.setDebug(true);

        // Default values
        musicSlider.setValue(game.musicVolume*100);
        sfxSlider.setValue(game.sfxVolume*100);

        // Add to a smaller table to centre the labels and slider bars
        sliderTable.add(musicTitle).padRight(20);
        sliderTable.add(musicSlider).prefWidth(250);
        sliderTable.row().padTop(20);
        sliderTable.add(sfxTitle).padRight(20).right();
        sliderTable.add(sfxSlider).prefWidth(250);

        // Window UI elements
        optionTable.add(title).top().padTop(40).padBottom(50);
        optionTable.row();
        optionTable.add(sliderTable).fillX();
        optionTable.row();
        optionTable.add(exitButton).pad(40, 50, 60, 50).width(300).bottom().expandY();

        optionMenu.pack();

        optionMenu.setSize(600, 600);

        // Centre the window
        optionMenu.setX(((float) Gdx.graphics.getWidth() / 2) - (optionMenu.getWidth() / 2));
        optionMenu.setY(((float) Gdx.graphics.getHeight() / 2) - (optionMenu.getHeight() / 2));

        // Create exit button listener
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                game.setScreen(previousScreen);
                previousScreen.resume();
            }
        });


    }


    @Override
    public void render (float delta) {
        ScreenUtils.clear(0.53f, 0.81f, 0.92f, 1);

        camera.update();
        // game.batch.setProjectionMatrix(camera.combined);

        optionStage.act(delta);
        optionStage.draw();

        // Volumes should be between 0 and 1
        game.musicVolume = musicSlider.getValue() / 100;
        game.sfxVolume = sfxSlider.getValue() / 100;

    }


    @Override
    public void resize(int width, int height) {

    }

    // Other required methods
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
        optionStage.dispose();
    }
}