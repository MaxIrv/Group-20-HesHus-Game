package com.skloch.game.tests.components;

import static org.mockito.Mockito.when;

import com.badlogic.gdx.maps.MapProperties;
import com.skloch.game.GameObject;
import com.skloch.game.tests.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * Tests for the GameObject class.
 */
@RunWith(GdxTestRunner.class)
public class GameObjectTests {
  GameObject gameObject1;
  GameObject gameObject2;
  @Mock
  MapProperties mapProperties = Mockito.mock(MapProperties.class);
  private static final float DELTA = 0.0001f;

  /**
   * Setup the mocked classes for the testing.
   */
  @Before
  public void setup() {
    when(mapProperties.get("x")).thenReturn(1.0f);
    when(mapProperties.get("y")).thenReturn(1.0f);
    when(mapProperties.get("width")).thenReturn(1.0f);
    when(mapProperties.get("height")).thenReturn(1.0f);

    gameObject1 = new GameObject(1.0f, 1.0f, 1.0f, 1.0f);
    gameObject2 = new GameObject(mapProperties, 2.0f);
  }

  @Test
  public void testLocationalInstanceProperties() {
    // Check properties was instantiated
    Assert.assertNotNull(gameObject1.properties);
    // x
    Assert.assertEquals(1.0f, gameObject1.getX(), DELTA);
    // y
    Assert.assertEquals(1.0f, gameObject1.getY(), DELTA);
    // width
    Assert.assertEquals(1.0f, gameObject1.getWidth(), DELTA);
    // height
    Assert.assertEquals(1.0f, gameObject1.getHeight(), DELTA);
    // centreX
    Assert.assertEquals(1.5f, gameObject1.centreX, DELTA);
    // centreY
    Assert.assertEquals(1.5f, gameObject1.centreY, DELTA);

  }

  @Test
  public void testPredefinedInstanceProperties() {
    Assert.assertNotNull(gameObject2.properties);
    // x
    Assert.assertEquals(2.0f, gameObject2.getX(), DELTA);
    // y
    Assert.assertEquals(2.0f, gameObject2.getY(), DELTA);
    // width
    Assert.assertEquals(2.0f, gameObject2.getWidth(), DELTA);
    // height
    Assert.assertEquals(2.0f, gameObject2.getHeight(), DELTA);
    // centreX
    Assert.assertEquals(3.0f, gameObject2.centreX, DELTA);
    // centreY
    Assert.assertEquals(3.0f, gameObject2.centreY, DELTA);
  }
}
