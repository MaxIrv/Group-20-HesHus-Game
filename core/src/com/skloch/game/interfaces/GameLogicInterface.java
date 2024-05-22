package com.skloch.game.interfaces;

import com.skloch.game.GameMap;
import com.skloch.game.GameObject;
import com.skloch.game.HustleGame;

/** Interface for the game logic. */
public interface GameLogicInterface {
  void update(float delta);

  void setupMap(boolean firstLoad, GameMap gameMap);

  void switchMap(String mapName);

  String getCurrentMap();

  void passTime(float delta);

  void setEnergy(int energy);

  int getEnergy();

  void decreaseEnergy(int energy);

  void addStudyHours(int hours);

  void addRecreationalHours(int hours);

  void addMeal();

  String getMeal();

  String getWakeUpMessage();

  void setSleeping(boolean sleeping);

  boolean isSleeping();

  void addSleptHours(int hours);

  float getSeconds();

  PlayerInterface getPlayer();

  void gameOver();

  int getMealsEaten();

  int getHoursStudied();

  int getHoursRecreational();

  int getHoursSlept();

  int getDay();

  float getDaySeconds();

  EventManagerInterface getEventManager();

  boolean isPlayerNearObject();

  GameObject getPlayerClosestObject();

  // Study Streak
  int getStudyStreakCounter();

  void setStudyStreakCounter(int x);

  void addStudyStreakCounter(int x);

  // Bookworm
  int getBookWormCounter();

  void setBookWormCounter(int x);

  void addBookWormCounter(int x);

  // Eat Streak
  int getEatStreakCounter();

  void setEatStreakCounter(int x);

  void addEatStreakCounter(int x);

  // Fun Streak
  int getFunStreakCounter();

  void setFunStreakCounter(int x);

  void addFunStreakCounter(int x);

  // All Nighter
  int getNoSleepCounter();

  void setNoSleepCounter(int x);

  void addNoSleepCounter(int x);

  HustleGame getGame();
}
