package com.skloch.game.interfaces;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.skloch.game.DialogueBox;

/** Interface for the game UI. */
public interface GameUiInterface {

  void createUi(float worldWidth, float worldHeight);

  void renderUi(float delta);

  void resizeUi(int width, int height);

  Stage getUiStage();

  boolean isEscapeMenuVisible();

  void setEscapeMenuVisible(boolean visible);

  DialogueBox getDialogueBox();

  void dispose();
}
