package com.skloch.game.tests.components;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.skloch.game.GameRenderer;
import com.skloch.game.HustleGame;
import com.skloch.game.events.CameraPositionEvent;
import com.skloch.game.events.EventBus;
import com.skloch.game.interfaces.IPlayer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class GameRendererTests {
    private HustleGame mockGame;
    private EventBus mockEventBus;
    private OrthogonalTiledMapRenderer mockMapRenderer;
    private IPlayer mockPlayer;
    private Viewport mockViewport;

    @InjectMocks
    private GameRenderer gameRenderer;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mockGame properties
        mockGame.WIDTH = 800;
        mockGame.HEIGHT = 600;
        mockGame.mapScale = 1.0f;
        mockGame.mapSquareSize = 32;

        // Mock methods
        when(mockGame.map).thenReturn(null); // replace with actual map if needed
        when(mockGame.backgroundLayers).thenReturn(new int[] {0});
        when(mockGame.foregroundLayers).thenReturn(new int[] {1});

        // Injecting dependencies manually since Mockito doesn't handle them automatically
        gameRenderer = new GameRenderer(mockGame, mockEventBus);
    }

    @Test
    public void testInitialRender() {
        // Call the method
        gameRenderer.initial_render();

        // Verify that setProjectionMatrix was called
        verify(mockGame.shapeRenderer).setProjectionMatrix(any());
    }

    @Test
    public void testRender() {
        // Mock player methods
        when(mockPlayer.getCurrentFrame()).thenReturn(null); // replace with actual texture if needed
        when(mockPlayer.getSpriteX()).thenReturn(100f);
        when(mockPlayer.getSpriteY()).thenReturn(100f);
        when(mockPlayer.getSpriteWidth()).thenReturn(50f);
        when(mockPlayer.getSpriteHeight()).thenReturn(50f);
        when(mockPlayer.getCentreX()).thenReturn(400f);
        when(mockPlayer.getCentreY()).thenReturn(300f);

        // Call the method
        gameRenderer.render(1/60f, mockPlayer);

        // Verify methods were called
        verify(mockMapRenderer).setView(any());
        verify(mockMapRenderer, times(2)).render(any(int[].class));
        verify(mockGame.batch).begin();
        verify(mockGame.batch).draw(any(), anyFloat(), anyFloat(), anyFloat(), anyFloat(), anyFloat(), anyFloat(), anyFloat(), anyFloat(), anyFloat());
        verify(mockGame.batch).end();
    }

    @Test
    public void testGetWorldWidth() {
        // Mock viewport method
        when(mockViewport.getWorldWidth()).thenReturn(800f);

        // Call the method and assert
        assertEquals(800f, gameRenderer.getWorldWidth(), 0.001f);
    }

    @Test
    public void testGetWorldHeight() {
        // Mock viewport method
        when(mockViewport.getWorldHeight()).thenReturn(600f);

        // Call the method and assert
        assertEquals(600f, gameRenderer.getWorldHeight(), 0.001f);
    }

    @Test
    public void testResizeViewport() {
        // Call the method
        gameRenderer.resize_viewport(1024, 768);

        // Verify viewport update was called
        verify(mockViewport).update(1024, 768);
    }

    @Test
    public void testDispose() {
        // Call the method
        gameRenderer.dispose();

        // Verify mapRenderer dispose was called
        verify(mockMapRenderer).dispose();
    }

    @Test
    public void testOnCameraPosition() {
        // Create a mock event
        CameraPositionEvent mockEvent = mock(CameraPositionEvent.class);
        when(mockEvent.getPosition()).thenReturn(new Vector3(500, 500, 0));

        // Manually call the private method
        gameRenderer.onCameraPosition(mockEvent);

        // Verify camera position was set and updated
        assertEquals(new Vector3(500, 500, 0), gameRenderer.getCameraPosition());
    }
}