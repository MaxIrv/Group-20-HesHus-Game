package com.skloch.game.tests.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.skloch.game.*;
import com.skloch.game.events.*;
import com.skloch.game.mocks.MockedClasses;
import com.skloch.game.tests.GdxTestRunner;
import com.skloch.game.interfaces.GameScreenProvider;
import com.skloch.game.interfaces.IPlayer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class GameLogicTests {

    private HustleGame game;
    private GameScreenProvider gameScreenProvider;
    private EventBus eventBus;
    private IPlayer player;
    private GameLogic gameLogic;
    private SpriteBatch spriteBatch;
    private Stage stage;

    @Before
    public void setup() {
        game = MockedClasses.mockHustleGame();
        gameScreenProvider = MockedClasses.mockGameScreenProvider();
        eventBus = MockedClasses.mockEventBus();
        player = MockedClasses.mockPlayer();
        spriteBatch = MockedClasses.mockSpriteBatch();
        stage = MockedClasses.mockStage();
        game.soundManager = MockedClasses.mockSoundManager();

        // Mock the initialization of classes that use SpriteBatch and Stage
        when(game.getBatch()).thenReturn(spriteBatch);
        when(game.getStage()).thenReturn(stage);

        gameLogic = new GameLogic(game, gameScreenProvider, 1, eventBus);
    }

    @Test
    public void testInitialGameState() {
        assertEquals("Initial energy should be 100", 100, gameLogic.getEnergy());
        assertEquals("Initial day should be 1", 1, gameLogic.getDay());
        assertEquals("Initial map should be campus", "campus", gameLogic.getCurrentMap());
    }

    @Test
    public void testEnergyManagement() {
        gameLogic.decreaseEnergy(10);
        assertEquals("Energy should decrease by 10", 90, gameLogic.getEnergy());

        gameLogic.setEnergy(120);
        assertEquals("Energy should not exceed 100", 100, gameLogic.getEnergy());

        gameLogic.setEnergy(-10);
        assertEquals("Energy should not be negative", 0, gameLogic.getEnergy());
    }

    @Test
    public void testTimeManagement() {
        float initialSeconds = gameLogic.getSeconds();
        gameLogic.passTime(120);
        assertEquals("Seconds should increase by 120", initialSeconds + 120, gameLogic.getSeconds(), 0.01);
    }

    @Test
    public void testDayTransition() {
        gameLogic.passTime(1440); // Pass a full day
        assertEquals("Day should increase by 1", 2, gameLogic.getDay());
        assertTrue("Seconds should reset after a full day", gameLogic.getSeconds() < 1440);
    }



}
