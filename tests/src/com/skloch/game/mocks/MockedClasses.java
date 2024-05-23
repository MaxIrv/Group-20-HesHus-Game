package com.skloch.game.mocks;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.skloch.game.Achievement;
import com.skloch.game.HustleGame;
import com.skloch.game.events.EventBus;
import com.skloch.game.interfaces.GameLogicInterface;
import com.skloch.game.interfaces.GameScreenProvider;
import com.skloch.game.interfaces.LeaderboardScreenInterface;
import com.skloch.game.interfaces.PlayerInterface;
import com.skloch.game.interfaces.SoundManagerInterface;

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

  public static LeaderboardScreenInterface mockLeaderboardScreen() {
    return mock(LeaderboardScreenInterface.class);
  }

  /**
   * Mock an achievement with the given achieved status.
   *
   * @param achieved The achieved status of the achievement
   * @return The mocked achievement
   */
  public static Achievement mockAchievement(boolean achieved) {
    Achievement achievement = mock(Achievement.class);
    when(achievement.getAchieved()).thenReturn(achieved);
    return achievement;
  }

  public static FileHandle mockFileHandle() {
    return mock(FileHandle.class);
  }

  public static TextureAtlas mockTextureAtlas() {
    return mock(TextureAtlas.class);
  }

  public static Files mockFiles() {
    return mock(Files.class);
  }
}
