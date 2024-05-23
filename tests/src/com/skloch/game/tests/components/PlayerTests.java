package com.skloch.game.tests.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.skloch.game.GameObject;
import com.skloch.game.Player;
import com.skloch.game.mocks.MockedClasses;
import com.skloch.game.tests.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for the Player class.
 */
@RunWith(GdxTestRunner.class)
public class PlayerTests {
  private Player player;
  private FileHandle fileHandle;
  private Files files;

  /**
   * Setup the mocked classes for the testing.
   */
  @Before
  public void setup() {
    TextureAtlas atlas = MockedClasses.mockTextureAtlas();
    files = MockedClasses.mockFiles();
    when(files.internal(anyString())).thenReturn(MockedClasses.mockFileHandle());

    player = new Player("avatar1");
  }

  @Test
  public void testPlayerInitialization() {
    assertNotNull(player.sprite);
    assertNotNull(player.feet);
    assertNotNull(player.eventHitbox);
    assertNotNull(player.walkingAnimation);
    assertNotNull(player.idleAnimation);
  }

  @Test
  public void testPlayerMovement() {
    // Set up Gdx input mocks
    Gdx.input = mock(Input.class);
    when(Gdx.input.isKeyPressed(Input.Keys.LEFT)).thenReturn(true);

    float initialX = player.getX();
    player.move(1.0f);
    assertTrue(player.getX() < initialX);
  }

  @Test
  public void testCollisionDetection() {
    // Add a collidable object
    GameObject object = mock(GameObject.class);
    when(object.overlaps(any(Rectangle.class))).thenReturn(true);
    player.addCollidable(object);

    player.setX(0.0f);
    player.setY(0.0f);
    object.setX(0.0f);
    object.setY(0.0f);
    assertEquals(object.getX(), player.getX(), 0.0f);
    assertEquals(object.getY(), player.getY(), 0.0f);
    assertTrue(object.overlaps(player.eventHitbox));
  }

  @Test
  public void testAnimationUpdate() {
    float stateTimeBefore = player.getStateTime();
    player.updateAnimation();
    assertTrue(player.getStateTime() > stateTimeBefore);
  }

  @Test
  public void testNearObject() {
    GameObject object = mock(GameObject.class);
    when(object.get(anyString())).thenReturn("event");
    when(object.overlaps(any(Rectangle.class))).thenReturn(true);

    player.addCollidable(object);
    player.move(1.0f);
    assertTrue(player.nearObject());
  }

  @Test
  public void testGetCurrentFrame() {
    player.updateAnimation();
    TextureRegion frame = player.getCurrentFrame();
    assertNotNull(frame);
  }

  @Test
  public void testSetBounds() {
    Rectangle bounds = new Rectangle(0, 0, 100, 100);
    player.setBounds(bounds);

    player.setPos(150, 150);
    player.move(1.0f);

    assertTrue(player.getX() <= 100);
    assertTrue(player.getY() <= 100);
  }

  @Test
  public void testFrozenPlayer() {
    player.setFrozen(true);
    assertTrue(player.isFrozen());

    float initialX = player.getX();
    player.move(1.0f);
    assertEquals(initialX, player.getX(), 0.0f);
  }
}