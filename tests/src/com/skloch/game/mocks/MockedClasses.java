package com.skloch.game.mocks;

import static org.mockito.Mockito.mock;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.skloch.game.HustleGame;
import com.skloch.game.LeaderboardScreen;
import com.skloch.game.events.EventBus;
import com.skloch.game.interfaces.*;

/** A class to mock classes for testing. */
public class MockedClasses {

  public static HustleGame mockHustleGame() {
    //        game.map = mock(GameMap.class);
    return mock(HustleGame.class);
  }

  public static GameScreenProvider mockGameScreenProvider() {
    return mock(GameScreenProvider.class);
  }

  public static EventBus mockEventBus() {
    return mock(EventBus.class);
  }

  public static PlayerInterface mockPlayer() {
    return mock(PlayerInterface.class);
  }

  public static SpriteBatch mockSpriteBatch() {
    return mock(SpriteBatch.class);
  }

  public static Stage mockStage() {
    return mock(Stage.class);
  }

  public static SoundManagerInterface mockSoundManager() {
    return mock(SoundManagerInterface.class);
  }

  public static GameLogicInterface mockGameLogic() {
    return mock(GameLogicInterface.class);
  }

  public static LeaderboardScreenInterface mockLeaderboardScreen(){
    return mock(LeaderboardScreenInterface.class);
  }
}
