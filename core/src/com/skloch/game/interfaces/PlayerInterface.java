package com.skloch.game.interfaces;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.skloch.game.GameObject;

/** Interface for the player. */
public interface PlayerInterface {
  void move(float delta);

  void updateAnimation();

  boolean nearObject();

  GameObject getClosestObject();

  boolean isMoving();

  void setMoving(boolean moving);

  TextureRegion getCurrentFrame();

  void setCollidables(Array<GameObject> collidables);

  void addCollidable(GameObject object);

  void clearCollidables();

  float getX();

  float getY();

  float getCentreX();

  float getCentreY();

  Vector3 getPosAsVec3();

  void setX(float x);

  void setY(float y);

  void setPos(float x, float y);

  void setBounds(Rectangle bounds);

  void setFrozen(boolean freeze);

  boolean isFrozen();

  float getSpriteX();

  float getSpriteY();

  float getSpriteWidth();

  float getSpriteHeight();

  float getStateTime();
}
