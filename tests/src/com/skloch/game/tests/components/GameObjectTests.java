package com.skloch.game.tests.components;

import com.badlogic.gdx.maps.MapProperties;
import com.skloch.game.GameObject;
import com.skloch.game.tests.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

@RunWith(GdxTestRunner.class)
public class GameObjectTests {
    GameObject gameObject1;
    GameObject gameObject2;
    @Mock
    MapProperties mapProperties = Mockito.mock(MapProperties.class);

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
        Assert.assertEquals(1.0f, gameObject1.getX());
        // y
        Assert.assertEquals(1.0f, gameObject1.getY());
        // width
        Assert.assertEquals(1.0f, gameObject1.getWidth());
        // height
        Assert.assertEquals(1.0f, gameObject1.getHeight());
        // centreX
        Assert.assertEquals(1.5f, gameObject1.centreX);
        // centreY
        Assert.assertEquals(1.5f, gameObject1.centreY);

    }

    @Test
    public void testPredefinedInstanceProperties() {
        Assert.assertNotNull(gameObject2.properties);
        // x
        Assert.assertEquals(2.0f, gameObject2.getX());
        // y
        Assert.assertEquals(2.0f, gameObject2.getY());
        // width
        Assert.assertEquals(2.0f, gameObject2.getWidth());
        // height
        Assert.assertEquals(2.0f, gameObject2.getHeight());
        // centreX
        Assert.assertEquals(3.0f, gameObject2.centreX);
        // centreY
        Assert.assertEquals(3.0f, gameObject2.centreY);
    }

    @Test
    public void testGet() {
        Assert.assertEquals(1.0f, gameObject1.get("x"));
        Assert.assertEquals(1.0f, gameObject1.get("y"));
        Assert.assertEquals(1.0f, gameObject1.get("width"));
        Assert.assertEquals(1.0f, gameObject1.get("height"));
    }
}
