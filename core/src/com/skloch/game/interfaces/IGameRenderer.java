package com.skloch.game.interfaces;

import com.skloch.game.GameMap;

public interface IGameRenderer {
    void initial_render();
    void render(float delta, IPlayer player, GameMap gameMap);
  float getWorldWidth();
    float getWorldHeight();
    void resize_viewport(int width, int height);
    void dispose();
}
