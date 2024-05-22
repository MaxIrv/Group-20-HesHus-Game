package com.skloch.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.skloch.game.interfaces.ISoundManager;
import java.util.function.Predicate;

/**
 * A class that is initially created by DesktopLauncher, loads consistent files at the start of the game and initialises lots of important classes.
 * Loads the map, ui skin, text files and makes sound manager and more
 */
public class HustleGame extends Game {
	public SpriteBatch batch;
	public int WIDTH;
	public int HEIGHT;
	public Skin skin;
	public String credits, tutorialText;
	public ShapeRenderer shapeRenderer;
	public ISoundManager soundManager;
	public Stage blueBackground;
	public Achievement studyStreak;
	public Achievement bookWorm;
	public Achievement eatStreak;
	public Achievement funStreak;
	public Achievement allNighter;


	/**
	 * A class to initialise a lot of the assets required for the game, including the map, sound and UI skin.
	 * A instance of this object should be shared to most screens to allow resources to be shared and disposed of
	 * correctly.
	 * Should be created in DesktopLauncher,
	 *
	 * @param width Width of the window
	 * @param height Height of the window
	 */
	public HustleGame (int width, int height) {
		WIDTH = width;
		HEIGHT = height;
	}

	/**
	 * Loads resources used throughout the game.
	 * Creates a new spritebatch
	 * Loads the UI skin to use
	 * Loads the map and configures which layers are background, foreground and object layers
	 * Loads a shape renderer for debug options
	 * Loads a sound manager to play sounds
	 * Loads credit and tutorial texts
	 * Creates a stage with a blue background for screens to use
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("Interface/BlockyInterface.json"));

		// Define background, foreground and object layers
		// IMPORTANT: CHANGE THESE WHEN UPDATING THE LAYERS IN YOUR EXPORTED MAP FROM TILED
		// Bottom most layer on 'layers' tab is 0

		shapeRenderer = new ShapeRenderer();
		soundManager = new SoundManager();

		// Make a stage with a blue background that any screen can draw
		Image blueImage = new Image(new Texture(Gdx.files.internal("Sprites/white_square.png")));
		blueImage.setColor(0.53f, 0.81f, 0.92f, 1);
		blueImage.setName("blue image");
		blueBackground = new Stage();
		blueBackground.addActor(blueImage);

		credits = readTextFile("Text/credits.txt");
		tutorialText = readTextFile("Text/tutorial_text.txt");

		// Generates all achievements
		Predicate<Integer> studyStreakNo = i -> (i > 4);
		studyStreak = new Achievement("Serial Studier", "Study 5 days in a row.", studyStreakNo);

		Predicate<Integer> studyTwiceToday = i -> (i == 1);
		bookWorm = new Achievement("Bookworm", "Study twice in the same day.", studyTwiceToday);

		Predicate<Integer> eatStreakNo = i -> (i > 4);
		eatStreak = new Achievement("Foodie", "Eat 5 days in a row.", eatStreakNo);

		Predicate<Integer> funStreakNo = i -> (i > 4);
		funStreak = new Achievement("Smelling the Roses", "Have fun 5 days in a row.", funStreakNo);

		Predicate<Integer> noSleep = i -> (i == 1);
		allNighter = new Achievement("The Longest Day (and Night)", "Pull an all-nighter.", noSleep);

		this.setScreen(new MenuScreen(this));
	}

	/**
	 * Very important, renders the game, remove super.render() to get a black screen
	 */
	@Override
	public void render () {
		super.render();
	}

	/**
	 * Disposes of elements that are loaded at the start of the game
	 */
	@Override
	public void dispose () {
		batch.dispose();
		blueBackground.dispose();
		skin.dispose();
		shapeRenderer.dispose();
		soundManager.dispose();
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
}
