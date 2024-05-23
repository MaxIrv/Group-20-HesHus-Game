package com.skloch.game.tests.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.InputMultiplexer;
import com.skloch.game.GameObject;
import com.skloch.game.GameOverScreen;
import com.skloch.game.GameScreen;
import com.skloch.game.HustleGame;
import com.skloch.game.events.EventBus;
import com.skloch.game.interfaces.*;

import com.skloch.game.tests.GdxTestRunner;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class GameScreenTests {

    @Mock
    private HustleGame mockGame;

    @Mock
    private IGameLogic mockGameLogic;

    @Mock
    private IGameRenderer mockGameRenderer;

    @Mock
    private IGameUI mockGameUI;

    @Mock
    private EventBus mockEventBus;

    @Mock
    private IPlayer mockPlayer;

    private GameScreen gameScreen;

    @Before
    public void setup() {

        when(mockGameLogic.getPlayer()).thenReturn(mockPlayer);

        gameScreen = new GameScreen(mockGame, 0);
    }

    @Test
    public void testShow() {
        // Test the show method if needed. Currently, it does nothing.
        gameScreen.show();
        // Add assertions or verifications if the show method is expanded.
    }

    @Test
    public void testRender() {
        // Mocking dependencies that the render method will call
        float delta = 0.016667f;

        gameScreen.render(delta);

        verify(mockGameLogic, times(1)).update(delta);
        verify(mockGameRenderer, times(1)).render(delta, mockPlayer);
        verify(mockGameUI, times(1)).render_ui(delta);
    }

    @Test
    public void testIsDialogueBoxVisible() {
        when(mockGameUI.getDialogueBox().isVisible()).thenReturn(true);

        assertTrue(gameScreen.isDialogueBoxVisible());

        when(mockGameUI.getDialogueBox().isVisible()).thenReturn(false);

        assertFalse(gameScreen.isDialogueBoxVisible());
    }

    @Test
    public void testIsEscapeMenuVisible() {
        when(mockGameUI.isEscapeMenuVisible()).thenReturn(true);

        assertTrue(gameScreen.isEscapeMenuVisible());

        when(mockGameUI.isEscapeMenuVisible()).thenReturn(false);

        assertFalse(gameScreen.isEscapeMenuVisible());
    }

    @Test
    public void testIsPlayerNearObject() {
        when(mockGameLogic.isPlayerNearObject()).thenReturn(true);

        assertTrue(gameScreen.isPlayerNearObject());

        when(mockGameLogic.isPlayerNearObject()).thenReturn(false);

        assertFalse(gameScreen.isPlayerNearObject());
    }

    @Test
    public void testIsPlayerSleeping() {
        when(mockGameLogic.isSleeping()).thenReturn(true);

        assertTrue(gameScreen.isPlayerSleeping());

        when(mockGameLogic.isSleeping()).thenReturn(false);

        assertFalse(gameScreen.isPlayerSleeping());
    }

    @Test
    public void testGetPlayerClosestObject() {
        GameObject mockGameObject = mock(GameObject.class);
        when(mockGameLogic.getPlayerClosestObject()).thenReturn(mockGameObject);

        assertEquals(mockGameObject, gameScreen.getPlayerClosestObject());
    }

    @Test
    public void testGetPlayer() {
        assertEquals(mockPlayer, gameScreen.getPlayer());
    }

    @Test
    public void testResize() {
        int width = 800;
        int height = 600;

        gameScreen.resize(width, height);

        verify(mockGameUI, times(1)).resize_ui(width, height);
        verify(mockGameRenderer, times(1)).resize_viewport(width, height);
    }

    @Test
    public void testResume() {
        InputMultiplexer inputMultiplexer = mock(InputMultiplexer.class);

        gameScreen.resume();

        verify(inputMultiplexer, times(1)).addProcessor(any());
        // Further verifications if needed.
    }

    @Test
    public void testDispose() {
        gameScreen.dispose();

        verify(mockGameUI, times(1)).dispose();
        verify(mockGameRenderer, times(1)).dispose();
    }

    @Test
    public void testGameOver() {
        gameScreen.GameOver();

        verify(mockGame, times(1)).setScreen(any(GameOverScreen.class));
    }

    @Test
    public void testAddStudyStreakCounter() {
        gameScreen.setStudyStreakCounter(0);
        gameScreen.addStudyStreakCounter(5);

        assertEquals(5, gameScreen.getStudyStreakCounter());
    }

    @Test
    public void testAddBookWormCounter() {
        gameScreen.setBookWormCounter(0);
        gameScreen.addBookWormCounter(5);

        assertEquals(5, gameScreen.getBookWormCounter());
    }

    @Test
    public void testAddEatStreakCounter() {
        gameScreen.setEatStreakCounter(0);
        gameScreen.addEatStreakCounter(5);

        assertEquals(5, gameScreen.getEatStreakCounter());
    }

    @Test
    public void testAddFunStreakCounter() {
        gameScreen.setFunStreakCounter(0);
        gameScreen.addFunStreakCounter(5);

        assertEquals(5, gameScreen.getFunStreakCounter());
    }

    @Test
    public void testAddNoSleepCounter() {
        gameScreen.setNoSleepCounter(0);
        gameScreen.addNoSleepCounter(5);

        assertEquals(5, gameScreen.getNoSleepCounter());
    }
}