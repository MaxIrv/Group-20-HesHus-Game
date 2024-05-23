package com.skloch.game.tests.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.skloch.game.LeaderboardManager;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class LeaderboardManagerTests {
  private LeaderboardManager leaderboardManager;

  @Before
  public void setup() {
    leaderboardManager = new LeaderboardManager();

    Gdx.files = Mockito.mock(Files.class);
    when(Gdx.files.local(leaderboardManager.leaderboardFilePath)).thenReturn(
        FileHandle.tempFile("scores.csv"));
  }

  @Test
  public void testSetAndGetPlayerScore() {
    leaderboardManager.setPlayerScore(100.0f);
    assertEquals(100.0f, leaderboardManager.getPlayerScore(), 0.01);
  }

  @Test
  public void testSetAndGetPlayerName() {
    leaderboardManager.setPlayerName("Player1");
    assertEquals("Player1", leaderboardManager.getPlayerName());
  }

  @Test
  public void testFetchLeaderboardEntries() {
    List<String[]> entries = leaderboardManager.fetchLeaderboardEntries();
    assertNotNull(entries);
  }

  @Test
  public void testStoreLeaderboardEntry() {
    leaderboardManager.storeLeaderboardEntry(new String[] {"TestPlayer", "99.9"});
    List<String[]> entries = leaderboardManager.fetchLeaderboardEntries();
    assertTrue(entries.stream().anyMatch(entry -> "TestPlayer".equals(entry[0])));
  }

  @Test
  public void testGetSortedEntries() {
    leaderboardManager.storeLeaderboardEntry(new String[] {"PlayerA", "50.0"});
    leaderboardManager.storeLeaderboardEntry(new String[] {"PlayerB", "75.0"});
    leaderboardManager.storeLeaderboardEntry(new String[] {"PlayerC", "25.0"});

    List<String[]> entries = leaderboardManager.fetchLeaderboardEntries();
    List<String[]> sortedEntries = leaderboardManager.getSortedEntries(entries);

    assertEquals("PlayerC", sortedEntries.get(0)[0]);
    assertEquals("PlayerA", sortedEntries.get(1)[0]);
    assertEquals("PlayerB", sortedEntries.get(2)[0]);
  }
}
