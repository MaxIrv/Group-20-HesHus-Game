package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.math.Vector3;
import com.skloch.game.events.*;
import com.skloch.game.events.DialogueBoxEvents.DialogueScrollEvent;
import com.skloch.game.interfaces.GameScreenProvider;
import com.skloch.game.interfaces.IEventManager;
import com.skloch.game.interfaces.IGameLogic;
import com.skloch.game.interfaces.IPlayer;

import java.util.Map;

/**
 * A class that handles the game logic, including the player, time, energy, and map switching.
 * This class is called by the GameScreen class to update the game.
 */
public class GameLogic implements IGameLogic {
    private final HustleGame game;
    private final GameScreenProvider gameScreen;
    private final IEventManager eventManager;
    private final EventBus eventBus;
    private final IPlayer player;
    private int energy = 100;
    private int hoursStudied, hoursRecreational, hoursSlept, mealsEaten;
    private int studyStreakCounter, bookWormCounter, eatStreakCounter, funStreakCounter, noSleepCounter;

    private float daySeconds;
    private int day = 1;
    private boolean sleeping = false;
    private String currentMap = "campus";
    private final Map<String, String> mapPaths = Map.of(
            "campus", "East Campus/east_campus.tmx",
            "town", "Town/town.tmx"
    );

    public GameLogic(HustleGame game, GameScreenProvider gameScreen, int avatarChoice, EventBus eventBus) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.eventManager = new EventManager(this, eventBus);
        this.eventBus = eventBus;

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

    /**
     * Update the game logic, including the player, time, and sound timers.
     *
     * @param delta The time in seconds since the last frame
     */
    @Override
    public void update(float delta) {
        // Update sound timers
        game.soundManager.processTimers(delta);

        // Increment the time and possibly day
        if (!gameScreen.isEscapeMenuVisible() && !sleeping) {
            passTime(Gdx.graphics.getDeltaTime() *1000);
        }

        eventBus.publish(new TimeUpdatedEvent(daySeconds));

        // Freeze the player's movement for this frame if any menus are visible
        player.setFrozen(gameScreen.isEscapeMenuVisible()  || gameScreen.isDialogueBoxVisible() || sleeping);

        eventBus.publish(new DialogueScrollEvent(0.8f));

        // Let the player move to keyboard presses if not frozen
        // Player.move() handles player collision
        // Also play a footstep sound if they are moving
        player.move(delta);
        if (player.isMoving()) {
            game.soundManager.playFootstep();
        } else {
            game.soundManager.stopFootstep();
        }
    }

    /**
     * Load and set up the map. Passes collidable objects to the player.
     *
     * @param firstLoad whether this is the first map being loaded, determines
     *                  whether to place the player at the spawn or respawn location.
     */
    @Override
    public void setupMap(boolean firstLoad) {
        // Setup map
        float unitScale = game.mapScale / game.mapSquareSize;

        // Get the dimensions of the top layer
        TiledMapTileLayer layer0 = (TiledMapTileLayer) game.map.getLayers().get(0);
        // Set the player to the middle of the map
        player.setPos(layer0.getWidth()*game.mapScale / 2f, layer0.getHeight()*game.mapScale / 2f);
        // Publish event to update camera position to player position
        eventBus.publish(new CameraPositionEvent(new Vector3(player.getCentreX(), player.getCentreY(), 0)));

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
                    eventBus.publish(new CameraPositionEvent(player.getPosAsVec3()));
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
    @Override
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
    @Override
    public String getCurrentMap(){
        return currentMap;
    }

    /**
     * Add a number of seconds to the time elapsed in the day
     *
     * @param delta The time in seconds to add
     */
    @Override
    public void passTime(float delta) {
        daySeconds += delta;
        while (daySeconds >= 1440) {
            daySeconds -= 1440;
            day += 1;
            Gdx.app.log("test", "test");
            eventBus.publish(new DayUpdatedEvent(day));
        }

        if (day >= 8) {
            GameOver();
        }
    }

    /**
     * Sets the player's energy level and updates the onscreen bar
     *
     * @param energy An int between 0 and 100
     */
    @Override
    public void setEnergy(int energy) {
        this.energy = energy;
        if (this.energy > 100) {
            this.energy = 100;
        } else if (this.energy < 0) {
            this.energy = 0;
        }
        // Update energy bar
        eventBus.publish(new EnergyUpdatedEvent(this.energy));
    }

    /**
     * @return The player's energy out of 100
     */
    @Override
    public int getEnergy() {
        return this.energy;
    }

    /**
     * Decreases the player's energy by a certain amount
     *
     * @param energy The energy to decrement
     */
    @Override
    public void decreaseEnergy(int energy) {
        this.energy = this.energy - energy;
        if (this.energy < 0) {
            this.energy = 0;
        }
        // Update energy bar
        eventBus.publish(new EnergyUpdatedEvent(this.energy));
    }

    /**
     * Adds an amount of hours studied to the total hours studied
     *
     * @param hours The amount of hours to add
     */
    @Override
    public void addStudyHours(int hours) {
        hoursStudied += hours;
        updateStatsEvent();
    }

    /**
     * Adds an amount of recreational hours to the total amount for the current day
     *
     * @param hours The amount of hours to add
     */
    @Override
    public void addRecreationalHours(int hours) {
        hoursRecreational += hours;
//        gameScreen.hoursRecreationalLabel.setText(String.format("Played for %d hours",hoursRecreational));
    }

    /**
     * Adds an amount of meals to the total number of meals
     */
    @Override
    public void addMeal() {
        mealsEaten++;
        updateStatsEvent();
    }

    /**
     * @return Returns 'breakfast', 'lunch' or 'dinner' depending on the time of day
     */
    @Override
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
    @Override
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
    @Override
    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }

    /**
     * @return true if the player is sleeping
     */
    @Override
    public boolean isSleeping() {
        return sleeping;
    }

    /**
     * @param hours Add this amount of hours to the total hours slept
     */
    @Override
    public void addSleptHours(int hours) {
        hoursSlept += hours;
        updateStatsEvent();
    }

    /**
     * @return The number of seconds elapsed in the day
     */
    @Override
    public float getSeconds() {
        return daySeconds;
    }

    /**
     * @return An object of player handled by GameLogic
     */
    @Override
    public IPlayer getPlayer() {
        return player;
    }

    /**
     * Ends the game, called at the end of the 7th day, switches to a screen that displays a score
     */
    @Override
    public void GameOver() {
        game.setScreen(new GameOverScreen(game, hoursStudied, hoursRecreational, hoursSlept,mealsEaten));
    }

    // Getters commands
    @Override
    public int getMealsEaten() {
        return mealsEaten;
    }
    @Override
    public int getHoursStudied() {
        return hoursStudied;
    }
    @Override
    public int getHoursRecreational() {
        return hoursRecreational;
    }
    @Override
    public int getHoursSlept() {
        return hoursSlept;
    }
    @Override
    public int getDay() {
        return day;
    }
    @Override
    public float getDaySeconds() {
        return daySeconds;
    }
    @Override
    public IEventManager getEventManager() {
        return eventManager;
    }
    @Override
    public boolean isPlayerNearObject() {
        return player.nearObject();
    }
    @Override
    public GameObject getPlayerClosestObject() {
        return player.getClosestObject();
    }

    /**
     * Update the stats on the UI
     */
    private void updateStatsEvent() {
        eventBus.publish(
                new GameStatsUpdatedEvent(
                        daySeconds, day, hoursRecreational, hoursStudied, mealsEaten, hoursSlept
                ));
    }

    // Study Streak
    public int getStudyStreakCounter() {return this.studyStreakCounter;}

    public void setStudyStreakCounter(int x) {this.studyStreakCounter = x;}

    public void addStudyStreakCounter(int x) {this.studyStreakCounter += x;}

    // Bookworm
    public int getBookWormCounter() {return this.bookWormCounter;}

    public void setBookWormCounter(int x) {this.bookWormCounter = x;}

    public void addBookWormCounter(int x) {this.bookWormCounter += x;}

    // Eat Streak
    public int getEatStreakCounter() {return this.eatStreakCounter;}

    public void setEatStreakCounter(int x) {this.eatStreakCounter = x;}

    public void addEatStreakCounter(int x) {this.eatStreakCounter += x;}

    // Fun Streak
    public int getFunStreakCounter() {return this.funStreakCounter;}

    public void setFunStreakCounter(int x) {this.funStreakCounter = x;}

    public void addFunStreakCounter(int x) {this.funStreakCounter += x;}

    // All Nighter
    public int getNoSleepCounter() {return this.noSleepCounter;}

    public void setNoSleepCounter(int x) {this.noSleepCounter = x;}

    public void addNoSleepCounter(int x) {this.noSleepCounter += x;}

    public HustleGame getGame() {
        return game;
    }
}

