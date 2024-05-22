package com.skloch.game.interfaces;

import com.skloch.game.GameMap;
import com.skloch.game.GameObject;

/** Interface for providing information about the game screen. */
public interface GameScreenProvider {
  boolean isDialogueBoxVisible();

  boolean isEscapeMenuVisible();

  boolean isPlayerNearObject();

  boolean isPlayerSleeping();

  GameObject getPlayerClosestObject();

  PlayerInterface getPlayer();

  GameMap getGameMap();
}
