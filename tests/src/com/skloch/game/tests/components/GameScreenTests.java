package com.skloch.game.tests.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.InputMultiplexer;
import com.skloch.game.GameMap;
import com.skloch.game.GameObject;
import com.skloch.game.GameOverScreen;
import com.skloch.game.GameScreen;
import com.skloch.game.HustleGame;
import com.skloch.game.interfaces.GameLogicInterface;
import com.skloch.game.interfaces.GameRendererInterface;
import com.skloch.game.interfaces.GameUiInterface;
import com.skloch.game.interfaces.PlayerInterface;
import com.skloch.game.tests.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(GdxTestRunner.class)
public class GameScreenTests {

  @Mock
  private HustleGame mockGame;

  @Mock
  private GameLogicInterface mockGameLogic;

  @Mock
  private GameRendererInterface mockGameRenderer;

  @Mock
  private GameUiInterface mockGameUI;

  @Mock
  private GameMap mockGameMap;

  @Mock
  private PlayerInterface mockPlayer;

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
    verify(mockGameRenderer, times(1)).render(delta, mockPlayer, mockGameMap);
    verify(mockGameUI, times(1)).renderUi(delta);
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

    verify(mockGameUI, times(1)).resizeUi(width, height);
    verify(mockGameRenderer, times(1)).resizeViewport(width, height);
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
    gameScreen.gameOver();

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