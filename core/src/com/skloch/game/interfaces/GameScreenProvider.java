package com.skloch.game.interfaces;

import com.skloch.game.GameMap;
import com.skloch.game.GameObject;

public interface GameScreenProvider {
    boolean isDialogueBoxVisible();
    boolean isEscapeMenuVisible();
    boolean isPlayerNearObject();
    boolean isPlayerSleeping();
    GameObject getPlayerClosestObject();
    IPlayer getPlayer();
    GameMap getGameMap();
}
