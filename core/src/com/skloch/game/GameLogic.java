package com.skloch.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Map;

public class GameLogic {
    private final HustleGame game;
    private final GameScreen gameScreen;
    public final EventManager eventManager;
    private Player player;
    public int energy = 100;
    public int hoursStudied, hoursRecreational, hoursSlept, mealsEaten;
    public float daySeconds;
    public int day = 1;
    public boolean sleeping = false;
    private String currentMap = "campus";
    private final Map<String, String> mapPaths = Map.of(
            "campus", "East Campus/east_campus.tmx",
            "town", "Town/town.tmx"
    );

    public GameLogic(HustleGame game, GameScreen gameScreen, int avatarChoice) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.eventManager = new EventManager(gameScreen, this);

        hoursStudied = hoursRecreational = hoursSlept = mealsEaten = 0;

        // Create a player class
        if (avatarChoice == 1) {
            player = new Player("avatar1");
        } else {
            player = new Player("avatar2");
        }

        // Set initial time
        daySeconds = (8 * 60); // 8:00 am

        // Start music
        game.soundManager.playOverworldMusic();
    }

    public void update(float delta) {
        // Update sound timers
        game.soundManager.processTimers(delta);

        // Increment the time and possibly day
        if (!gameScreen.escapeMenu.isVisible() && !sleeping) {
            passTime(Gdx.graphics.getDeltaTime());
        }
        gameScreen.timeLabel.setText(formatTime((int) daySeconds));

        // Freeze the player's movement for this frame if any menus are visible
        player.setFrozen(gameScreen.escapeMenu.isVisible() || gameScreen.dialogueBox.isVisible() || sleeping);

        gameScreen.dialogueBox.scrollText(0.8f);

        // Let the player move to keyboard presses if not frozen
        // Player.move() handles player collision
        // Also play a footstep sound if they are moving
        player.move(delta);
        if (player.isMoving()) {
            game.soundManager.playFootstep();
        } else {
            game.soundManager.footstepBool = false;
        }
    }

    /**
     * Load and set up the map. Passes collidable objects to the player.
     *
     * @param firstLoad whether this is the first map being loaded, determines
     *                  whether to place the player at the spawn or respawn location.
     */
    public void setupMap(boolean firstLoad) {
        // Setup map
        float unitScale = game.mapScale / game.mapSquareSize;
        gameScreen.mapRenderer = new OrthogonalTiledMapRenderer(game.map, unitScale);

        // Get the dimensions of the top layer
        TiledMapTileLayer layer0 = (TiledMapTileLayer) game.map.getLayers().get(0);
        // Set the player to the middle of the map
        player.setPos(layer0.getWidth()*game.mapScale / 2f, layer0.getHeight()*game.mapScale / 2f);
        // Put camera on player
        gameScreen.camera.position.set(player.getCentreX(), player.getCentreY(), 0);

        // Clear collidables from the player, as they may be from a different map.
        player.clearCollidables();

        // Give objects to player
        for (int layer : game.objectLayers) {
            // Get all objects on the layer
            MapObjects objects = game.map.getLayers().get(layer).getObjects();

            // Loop through each, handing them to the player
            for (int i = 0; i < objects.getCount(); i++) {
                // Get the properties of each object
                MapProperties properties = objects.get(i).getProperties();
                // If this is the spawn object, move the player there and don't collide
                if ((properties.get("spawn") != null && firstLoad) || (properties.get("respawn") != null && !firstLoad)) {
                    player.setPos(((float) properties.get("x")) * unitScale, ((float) properties.get("y")) * unitScale);
                    gameScreen.camera.position.set(player.getPosAsVec3());
                }
                // If not a spawn point make collidable
                else if (properties.get("spawn") == null && properties.get("respawn") == null) {
                    // Make a new gameObject with these properties, passing along the scale the map is rendered
                    // at for accurate coordinates
                    player.addCollidable(new GameObject(properties, unitScale));
                }
            }
        }

        // Set the player to not go outside the bounds of the map
        // Assumes the bottom left corner of the map is at 0, 0
        player.setBounds(
                new Rectangle(
                        0,
                        0,
                        game.mapProperties.get("width", Integer.class) * game.mapScale,
                        game.mapProperties.get("height", Integer.class) * game.mapScale
                )
        );

    }

    /**
     * Switch from the current map to another map as specified by its asset path.
     *
     * @param mapName the name of the map one of "town" and "campus"
     */
    public void switchMap(String mapName) {
        if (!mapPaths.containsKey(mapName)){
            mapName="campus";
        }

        // If we are switching to the home map then set "onHomeMap" attribute
        currentMap = mapName;
        game.switch_map(mapPaths.get(mapName));
        setupMap(false);
    }

    /**
     * Get the map the player is currently on.
     *
     * @return the name of the current map.
     */
    public String getCurrentMap(){
        return currentMap;
    }

    /**
     * Add a number of seconds to the time elapsed in the day
     *
     * @param delta The time in seconds to add
     */
    public void passTime(float delta) {
        daySeconds += delta;
        while (daySeconds >= 1440) {
            daySeconds -= 1440;
            day += 1;
            gameScreen.dayLabel.setText(String.format("Day %s", day));
        }

        if (day >= 8) {
            GameOver();
        }
    }

    /**
     * Takes a time in seconds and formats it a time in the format HH:MMam/pm
     *
     * @param seconds The seconds elapsed in a day
     * @return A formatted time on a 12 hour clock
     */
    public String formatTime(int seconds) {
        // Takes a number of seconds and converts it into a 12 hour clock time
        int hour = Math.floorDiv(seconds, 60);
        String minutes = String.format("%02d", (seconds - hour * 60));

        // Make 12 hour
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
     * Sets the player's energy level and updates the onscreen bar
     *
     * @param energy An int between 0 and 100
     */
    public void setEnergy(int energy) {
        this.energy = energy;
        if (this.energy > 100) {
            this.energy = 100;
        }
        // Update energy bar
         gameScreen.energyBar.setScaleY(this.energy / 100f);
    }

    /**
     * @return The player's energy out of 100
     */
    public int getEnergy() {
        return this.energy;
    }

    /**
     * Decreases the player's energy by a certain amount
     *
     * @param energy The energy to decrement
     */
    public void decreaseEnergy(int energy) {
        this.energy = this.energy - energy;
        if (this.energy < 0) {
            this.energy = 0;
        }
        // Update energy bar
        gameScreen.energyBar.setScaleY(this.energy / 100f);
    }

    /**
     * Adds an amount of hours studied to the total hours studied
     *
     * @param hours The amount of hours to add
     */
    public void addStudyHours(int hours) {
        hoursStudied += hours;

        // Update hours studied
        gameScreen.hoursStudiedLabel.setText(String.format("Studied for %d hours",hoursStudied));
    }

    /**
     * Adds an amount of recreational hours to the total amount for the current day
     *
     * @param hours The amount of hours to add
     */
    public void addRecreationalHours(int hours) {
        hoursRecreational += hours;
        gameScreen.hoursRecreationalLabel.setText(String.format("Played for %d hours",hoursRecreational));
    }

    /**
     * Adds an amount of meals to the total number of meals
     */
    public void addMeal() {
        mealsEaten++;
        gameScreen.mealsEatenLabel.setText(String.format("Eaten %d times",mealsEaten));
    }

    /**
     * @return Returns 'breakfast', 'lunch' or 'dinner' depending on the time of day
     */
    public String getMeal() {
        int hours = Math.floorDiv((int) daySeconds, 60);
        if (hours >= 7 && hours <= 10) {
            //Breakfast between 7:00-10:59am
            return "breakfast";
        } else if (hours > 10 && hours <= 16) {
            // Lunch between 10:00am and 4:59pm
            return "lunch";
        } else if (hours > 16 && hours <= 21) {
            // Dinner served between 4:00pm and 9:59pm
            return "dinner";
        } else {
            // Nothing is served between 10:00pm and 6:59am
            return "food";
        }
    }

    /**
     * @return A wake up message based on the time left until the exam
     */
    public String getWakeUpMessage() {
        int daysLeft = 8 - day;
        if (daysLeft != 1) {
            return String.format("You have %d days left until your exam!\nRemember to eat, study and have fun, but don't overwork yourself!", daysLeft);
        } else {
            return "Your exam is tomorrow! I hope you've been studying! Remember not to overwork yourself and get enough sleep!";
        }
    }

    /**
     * @param sleeping Sets the value of sleeping
     */
    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }

    /**
     * @return true if the player is sleeping
     */
    public boolean getSleeping() {
        return sleeping;
    }

    /**
     * @param hours Add this amount of hours to the total hours slept
     */
    public void addSleptHours(int hours) {
        hoursSlept += hours;
        gameScreen.hoursSleptLabel.setText(String.format("Slept for %d hours",hoursSlept));
    }

    /**
     * @return The number of seconds elapsed in the day
     */
    public float getSeconds() {
        return daySeconds;
    }

    /**
     * @return An object of player handled by GameLogic
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Ends the game, called at the end of the 7th day, switches to a screen that displays a score
     */
    public void GameOver() {
        game.setScreen(new GameOverScreen(game, hoursStudied, hoursRecreational, hoursSlept,mealsEaten));
    }
}

