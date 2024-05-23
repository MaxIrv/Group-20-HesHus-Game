package com.skloch.game.tests.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.skloch.game.HustleGame;
import com.skloch.game.SettingsScreen;
import com.skloch.game.interfaces.SoundManagerInterface;
import com.skloch.game.mocks.MockedClasses;
import com.skloch.game.tests.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

@RunWith(GdxTestRunner.class)
public class SettingsScreenTests {
  private HustleGame game;
  private Screen screen;
  private SettingsScreen settingsScreen;
  private SoundManagerInterface soundManager;
  private Viewport viewport;
  private Stage stage;

  @Before
  public void setup() {
    game = MockedClasses.mockHustleGame();
    screen = mock(Screen.class);
    settingsScreen = new SettingsScreen(game, screen);
    soundManager = MockedClasses.mockSoundManager();
    viewport = MockedClasses.mockViewport();
    stage = MockedClasses.mockStage();
  }

  @Test
  public void testConstructor() {
    assertNotNull(settingsScreen.musicSlider);
    assertNotNull(settingsScreen.sfxSlider);
    assertEquals(settingsScreen.previousScreen, screen);
  }

  @Test
  public void testRender() {
    // Test if render sets the correct volumes
    settingsScreen.musicSlider.setValue(50);
    settingsScreen.sfxSlider.setValue(30);

    settingsScreen.render(1.0f);

    verify(soundManager).setMusicVolume(0.5f);
    verify(soundManager).setSfxVolume(0.3f);
  }

  @Test
  public void testExitButtonListener() {
    // Capture the exit button listener
    ArgumentCaptor<ChangeListener> captor = ArgumentCaptor.forClass(ChangeListener.class);
    TextButton exitButton = settingsScreen.getOptionMenu().findActor("exitButton");
    verify(exitButton).addListener(captor.capture());

    // Trigger the listener and verify interactions
    ChangeListener.ChangeEvent event = new ChangeListener.ChangeEvent();
    captor.getValue().changed(event, exitButton);

    verify(soundManager).playButton();
    verify(game).setScreen(screen);
    verify(screen).resume();
  }

  @Test
  public void testResize() {
    settingsScreen.resize(1024, 768);
    verify(viewport).update(1024, 768);
    verify(stage.getViewport()).update(1024, 768);
  }

  @Test
  public void testDispose() {
    settingsScreen.dispose();
    verify(settingsScreen).dispose();
  }
}
