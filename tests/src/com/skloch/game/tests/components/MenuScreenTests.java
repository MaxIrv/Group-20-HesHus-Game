package com.skloch.game.tests.components;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.skloch.game.*;
import com.skloch.game.interfaces.ISoundManager;
import com.skloch.game.mocks.MockedClasses;
import com.skloch.game.tests.GdxTestRunner;
import org.junit.Before;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class MenuScreenTests {
    private HustleGame game;
    private MenuScreen menuScreen;
    private ISoundManager soundManager;
    private GL20 gl20;
    @Before
    public void setup() {
        game = MockedClasses.mockHustleGame();
        menuScreen = new MenuScreen(game);

        soundManager = MockedClasses.mockSoundManager();
        gl20 = MockedClasses.mockGL20();
    }

    @After
    public void tearDown() {
        menuScreen.dispose();
    }

    @Test
    public void testConstructor() {
        // Verify initial setup
        assertNotNull(menuScreen);
        verify(soundManager).playMenuMusic();
    }

    @Test
    public void testRender() {
        menuScreen.render(1.0f);
        verify(gl20).glClearColor(0, 0, 0, 1);
        verify(gl20).glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Test
    public void testStartButton() {
        TextButton startButton;
        startButton = menuScreen.getMenuStage().getRoot().findActor("startButton");
        assertNotNull(startButton);

        // Capture and trigger the listener
        ArgumentCaptor<ChangeListener> captor = ArgumentCaptor.forClass(ChangeListener.class);
        verify(startButton).addListener(captor.capture());
        ChangeListener listener = captor.getValue();
        ChangeListener.ChangeEvent event = new ChangeListener.ChangeEvent();
        listener.changed(event, startButton);

        // Verify actions taken when start button is pressed
        verify(soundManager).playButton();
        // More assertions as needed
    }

    @Test
    public void testSettingsButton() {
        TextButton settingsButton = menuScreen.getMenuStage().getRoot().findActor("settingsButton");
        assertNotNull(settingsButton);

        // Capture and trigger the listener
        ArgumentCaptor<ChangeListener> captor = ArgumentCaptor.forClass(ChangeListener.class);
        verify(settingsButton).addListener(captor.capture());
        ChangeListener listener = captor.getValue();
        ChangeListener.ChangeEvent event = new ChangeListener.ChangeEvent();
        listener.changed(event, settingsButton);

        // Verify actions taken when settings button is pressed
        verify(soundManager).playButton();
        verify(game).setScreen(any(SettingsScreen.class));
    }

    @Test
    public void testCreditsButton() {
        TextButton creditsButton = menuScreen.getMenuStage().getRoot().findActor("creditsButton");
        assertNotNull(creditsButton);

        // Capture and trigger the listener
        ArgumentCaptor<ChangeListener> captor = ArgumentCaptor.forClass(ChangeListener.class);
        verify(creditsButton).addListener(captor.capture());
        ChangeListener listener = captor.getValue();
        ChangeListener.ChangeEvent event = new ChangeListener.ChangeEvent();
        listener.changed(event, creditsButton);

        // Verify actions taken when credits button is pressed
        verify(soundManager).playButton();
        verify(game).setScreen(any(CreditScreen.class));
    }

    @Test
    public void testExitButton() {
        TextButton exitButton = menuScreen.getMenuStage().getRoot().findActor("exitButton");
        assertNotNull(exitButton);

        // Capture and trigger the listener
        ArgumentCaptor<ChangeListener> captor = ArgumentCaptor.forClass(ChangeListener.class);
        verify(exitButton).addListener(captor.capture());
        ChangeListener listener = captor.getValue();
        ChangeListener.ChangeEvent event = new ChangeListener.ChangeEvent();
        listener.changed(event, exitButton);

        // Verify actions taken when exit button is pressed
        verify(soundManager).playButton();
        verify(game).dispose();
        verify(menuScreen.getMenuStage()).dispose();
    }

    @Test
    public void testResize() {
        menuScreen.resize(1024, 768);
        verify(menuScreen.getMenuStage()).getViewport().update(1024, 768, true);
    }

    @Test
    public void testDispose() {
        menuScreen.dispose();
        verify(menuScreen.getMenuStage()).dispose();
    }
}
