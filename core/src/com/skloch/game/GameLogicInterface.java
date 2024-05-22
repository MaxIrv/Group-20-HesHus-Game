package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ArrayMap;
import com.skloch.game.events.CameraPositionEvent;
import com.skloch.game.events.DayUpdatedEvent;
import com.skloch.game.events.EnergyUpdatedEvent;
import com.skloch.game.events.EventBus;
import com.skloch.game.events.GameStatsUpdatedEvent;
import com.skloch.game.events.MapSwitchEvent;
import com.skloch.game.events.TimeUpdatedEvent;
import com.skloch.game.events.dialoguebox.DialogueScrollEvent;
import com.skloch.game.interfaces.GameScreenProvider;
import com.skloch.game.interfaces.InterfaceEventManager;
import com.skloch.game.interfaces.PlayerInterface;

import java.util.Map;

/**
 * A class that handles the game logic, including the player, time, energy, and map switching. This
 * class is called by the GameScreen class to update the game.
 */
public class GameLogicInterface implements com.skloch.game.interfaces.GameLogicInterface {
  private final HustleGame game;
  private final GameScreenProvider gameScreen;
  private final InterfaceEventManager eventManager;
  private final EventBus eventBus;
  private final PlayerInterface player;
  private int energy = 100;
  private int hoursStudied = 0;
  private int hoursRecreational = 0;
  private int hoursSlept = 0;
  private int mealsEaten = 0;
  private int studyStreakCounter;
  private int bookWormCounter;
  private int eatStreakCounter;
  private int funStreakCounter;
  private int noSleepCounter;

  private float daySeconds;
  private int day = 1;
  private boolean sleeping = false;
  private String currentMap = "campus";

  /**
   * Constructor for the GameLogic class. Sets up the player, time, and event manager.
   *
   * @param game The game object
   * @param gameScreen The GameScreenProvider object
   * @param avatarChoice The avatar choice
   * @param eventBus The event bus
   */
  public GameLogicInterface(
      HustleGame game, GameScreenProvider gameScreen, int avatarChoice, EventBus eventBus) {
    this.game = game;
    this.gameScreen = gameScreen;
    this.eventManager = new com.skloch.game.InterfaceEventManager(this, eventBus);
    this.eventBus = eventBus;

    hoursStudied = hoursRecreational = hoursSlept = mealsEaten = 0;

    // Create a player class
    if (avatarChoice == 1) {
      player = new com.skloch.game.PlayerInterface("avatar1");
    } else {
      player = new com.skloch.game.PlayerInterface("avatar2");
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
      passTime(Gdx.graphics.getDeltaTime());
    }

    eventBus.publish(new TimeUpdatedEvent(daySeconds));

    // Freeze the player's movement for this frame if any menus are visible
    player.setFrozen(
        gameScreen.isEscapeMenuVisible() || gameScreen.isDialogueBoxVisible() || sleeping);

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
   * @param firstLoad whether this is the first map being loaded, determines whether to place the
   *     player at the spawn or respawn location.
   */
  @Override
  public void setupMap(boolean firstLoad, GameMap gameMap) {
    // Get the dimensions of the top layer
    TiledMapTileLayer layer0 = (TiledMapTileLayer) gameMap.getLayers().get(0);
    // Set the player to the middle of the map
    player.setPos(
        layer0.getWidth() * gameMap.mapScale / 2f, layer0.getHeight() * gameMap.mapScale / 2f);
    // Publish event to update camera position to player position
    eventBus.publish(
        new CameraPositionEvent(new Vector3(player.getCentreX(), player.getCentreY(), 0)));

    // Clear collidables from the player, as they may be from a different map.
    player.clearCollidables();

    float unitScale = gameMap.mapScale / gameMap.mapSquareSize;

    // Give objects to player
    for (int layer : gameMap.objectLayers) {
      // Get all objects on the layer
      MapObjects objects = gameMap.getLayers().get(layer).getObjects();

      // Loop through each, handing them to the player
      for (int i = 0; i < objects.getCount(); i++) {
        // Get the properties of each object
        MapProperties properties = objects.get(i).getProperties();
        // If this is the spawn object, move the player there and don't collide
        if ((properties.get("spawn") != null && firstLoad)
            || (properties.get("respawn") != null && !firstLoad)) {
          player.setPos(
              ((float) properties.get("x")) * unitScale, ((float) properties.get("y")) * unitScale);
          eventBus.publish(new CameraPositionEvent(player.getPosAsVec3()));
        } else if (properties.get("spawn") == null && properties.get("respawn") == null) {
          // If not a spawn point make collidable
          // Make a new gameObject with these properties, passing along the scale the map is
          // rendered
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
            gameMap.mapProperties.get("width", Integer.class) * gameMap.mapScale,
            gameMap.mapProperties.get("height", Integer.class) * gameMap.mapScale));
  }

  /**
   * Switch from the current map to another map as specified by its asset path.
   *
   * @param mapName the name of the map one of "town" and "campus"
   */
  @Override
  public void switchMap(String mapName) {
    Gdx.app.log("GameLogic", "Switching to map: " + mapName);
    Gdx.app.log("GameLogic", "Switching to map: " + mapName);
    // If we are switching to the home map then set "onHomeMap" attribute
    currentMap = mapName;
    gameScreen.getGameMap().switch_map(mapName);
    setupMap(false, gameScreen.getGameMap());
    eventBus.publish(new MapSwitchEvent(gameScreen.getGameMap()));
  }

  /**
   * Get the map the player is currently on.
   *
   * @return the name of the current map.
   */
  @Override
  public String getCurrentMap() {
    return currentMap;
  }

  /**
   * Add a number of seconds to the time elapsed in the day.
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
      gameOver();
    }
  }

  /**
   * Sets the player's energy level and updates the onscreen bar.
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

  @Override
  public int getEnergy() {
    return this.energy;
  }

  /**
   * Decreases the player's energy by a certain amount.
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
   * Adds an amount of hours studied to the total hours studied.
   *
   * @param hours The amount of hours to add
   */
  @Override
  public void addStudyHours(int hours) {
    hoursStudied += hours;
    updateStatsEvent();
  }

  /**
   * Adds an amount of recreational hours to the total amount for the current day.
   *
   * @param hours The amount of hours to add
   */
  @Override
  public void addRecreationalHours(int hours) {
    hoursRecreational += hours;
    //        gameScreen.hoursRecreationalLabel.setText(String.format("Played for %d
    // hours",hoursRecreational));
  }

  /** Adds an amount of meals to the total number of meals. */
  @Override
  public void addMeal() {
    mealsEaten++;
    updateStatsEvent();
  }

  /**
   * Gets the meal that should be served based on the time of day.
   *
   * @return Returns 'breakfast', 'lunch' or 'dinner' depending on the time of day
   */
  @Override
  public String getMeal() {
    int hours = Math.floorDiv((int) daySeconds, 60);
    if (hours >= 7 && hours <= 10) {
      // Breakfast between 7:00-10:59am
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
   * Returns a wake up message based on the time left until the exam.
   *
   * @return A wake up message based on the time left until the exam
   */
  @Override
  public String getWakeUpMessage() {
    int daysLeft = 8 - day;
    if (daysLeft != 1) {
      return String.format(
          "You have %d days left until your exam!\nRemember to eat, "
              + "study and have fun, but don't overwork yourself!",
          daysLeft);
    } else {
      return "Your exam is tomorrow! I hope you've been studying! "
          + "Remember not to overwork yourself and get enough sleep!";
    }
  }

  /**
   * Sets the player to be sleeping or not.
   *
   * @param sleeping Sets the value of sleeping
   */
  @Override
  public void setSleeping(boolean sleeping) {
    this.sleeping = sleeping;
  }

  /**
   * Checks if the player is sleeping.
   *
   * @return true if the player is sleeping
   */
  @Override
  public boolean isSleeping() {
    return sleeping;
  }

  /**
   * Increments the number of hours slept by a certain amount.
   *
   * @param hours Add this amount of hours to the total hours slept
   */
  @Override
  public void addSleptHours(int hours) {
    hoursSlept += hours;
    updateStatsEvent();
  }

  @Override
  public float getSeconds() {
    return daySeconds;
  }

  @Override
  public PlayerInterface getPlayer() {
    return player;
  }

  /**
   * Ends the game, called at the end of the 7th day, switches to a screen that displays a score.
   */
  @Override
  public void gameOver() {
    game.setScreen(
        new GameOverScreen(game, hoursStudied, hoursRecreational, hoursSlept, mealsEaten,getPlayerScore()));
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
  public InterfaceEventManager getEventManager() {
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

  /** Update the stats on the UI. */
  private void updateStatsEvent() {
    eventBus.publish(
        new GameStatsUpdatedEvent(
            daySeconds, day, hoursRecreational, hoursStudied, mealsEaten, hoursSlept));
  }

  public float getPlayerScore(){
    //  if target range for each thing rec, study, meals and sleep, then X points
    float rawScore = 0;
    float possible_score = 4;

    ArrayMap<String, int[]> successRanges = new ArrayMap<String, int[]>();

    successRanges.put("rec",new int[] {7,10});
    successRanges.put("study",new int[] {10,21});
    successRanges.put("meals",new int[] {10,26});
    successRanges.put("sleep",new int[] {30,55});

    if ((hoursRecreational > successRanges.get("rec")[0]) &&
            (hoursRecreational < successRanges.get("rec")[1])){
      rawScore += 1;
    }
    if (hoursStudied > successRanges.get("study")[0] &&
            hoursStudied <successRanges.get("study")[1]){
      rawScore += 1;
    }
    if (hoursStudied > successRanges.get("meals")[0] &&
            hoursStudied <successRanges.get("meals")[1]){
      rawScore += 1;
    }
    if (hoursStudied > successRanges.get("sleep")[0] &&
            hoursStudied <successRanges.get("sleep")[1]){
      rawScore += 1;
    }


    /**
     * Multiply by 1.3x
     */
    if (game.allNighter.getAchieved()){
      rawScore *= 1.2f;
    }
    if (game.bookWorm.getAchieved()){
      rawScore *= 1.2f;
    }
    if (game.eatStreak.getAchieved()){
      rawScore *= 1.2f;
    }
    if (game.funStreak.getAchieved()){
      rawScore *= 1.2f;
    }
    if (game.studyStreak.getAchieved()){
      rawScore *= 1.2f;
    }


    float ratio_score = rawScore/possible_score;
    if (ratio_score > 1){
      ratio_score = 1;
    }
    return ratio_score * 100;
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

  public HustleGame getGame() {
    return game;
  }
}
