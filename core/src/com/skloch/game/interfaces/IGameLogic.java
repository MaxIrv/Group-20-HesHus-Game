package com.skloch.game.interfaces;

import com.skloch.game.GameObject;

public interface IGameLogic {
    void update(float delta);
    void setupMap(boolean firstLoad);
    void switchMap(String mapName);
    String getCurrentMap();
    void passTime(float delta);
    String formatTime(int seconds);
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
    IPlayer getPlayer();
    void GameOver();
    int getMealsEaten();
    int getHoursStudied();
    int getHoursRecreational();
    int getHoursSlept();
    int getDay();
    float getDaySeconds();
    IEventManager getEventManager();
    boolean isPlayerNearObject();
    GameObject getPlayerClosestObject();
}
