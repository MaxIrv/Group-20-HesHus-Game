package com.skloch.game.interfaces;

import com.skloch.game.GameMap;

/** Interface for the game renderer. */
public interface GameRendererInterface {
  void initial_render();

  void render(float delta, PlayerInterface player, GameMap gameMap);

  float getWorldWidth();

  float getWorldHeight();

  void resize_viewport(int width, int height);

  void dispose();
}
