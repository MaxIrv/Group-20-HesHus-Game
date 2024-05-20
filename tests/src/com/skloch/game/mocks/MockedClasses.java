package com.skloch.game.mocks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.skloch.game.HustleGame;
import com.skloch.game.events.EventBus;
import com.skloch.game.interfaces.GameScreenProvider;
import com.skloch.game.interfaces.IPlayer;
import com.skloch.game.interfaces.ISoundManager;
import com.skloch.game.interfaces.IGameLogic;

import static org.mockito.Mockito.mock;

public class MockedClasses {

    public static HustleGame mockHustleGame() {
        HustleGame game = mock(HustleGame.class);
        game.map = mock(TiledMap.class);
        return game;
    }

    public static GameScreenProvider mockGameScreenProvider() {
        return mock(GameScreenProvider.class);
    }

    public static EventBus mockEventBus() {
        return mock(EventBus.class);
    }

    public static IPlayer mockPlayer() {
        return mock(IPlayer.class);
    }

    public static SpriteBatch mockSpriteBatch() {
        return mock(SpriteBatch.class);
    }

    public static Stage mockStage() {
        return mock(Stage.class);
    }

    public static ISoundManager mockSoundManager() {
        return mock(ISoundManager.class);
    }

    public static IGameLogic mockGameLogic(){
        return mock(IGameLogic.class);
    }
}
