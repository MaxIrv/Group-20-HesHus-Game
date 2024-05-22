package com.skloch.game.interfaces;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.skloch.game.DialogueBox;

/** Interface for the game UI. */
public interface GameUiInterface {

  void create_ui(float worldWidth, float worldHeight);

  void render_ui(float delta);

  void resize_ui(int width, int height);

  Stage getUiStage();

  boolean isEscapeMenuVisible();

  void setEscapeMenuVisible(boolean visible);

  DialogueBox getDialogueBox();

  void dispose();
}
