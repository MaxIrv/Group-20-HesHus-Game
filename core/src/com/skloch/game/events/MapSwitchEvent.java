package com.skloch.game.events;

import com.skloch.game.GameMap;

/** Communicate from GameLogic to GameRenderer a map switch event. */
public class MapSwitchEvent {
  private final GameMap newGameMap;

  public MapSwitchEvent(GameMap newGameMap) {
    this.newGameMap = newGameMap;
  }

  public GameMap getNewGameMap() {
    return newGameMap;
  }

}
