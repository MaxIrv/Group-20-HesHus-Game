package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A class to manage the leaderboard, storing and fetching entries from a file.
 */
public class LeaderboardManager {
  private String playerName;
  private float playerScore;
  public final String leaderboardFilePath = "scores.csv";

  /**
   * Constructor for LeaderboardManager.
   */
  public LeaderboardManager() {

  }

  public void setPlayerScore(float score) {
    this.playerScore = score;
  }

  public float getPlayerScore() {
    return playerScore;
  }

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String name) {
    this.playerName = name;
  }

  /**
   * Fetches the leaderboard entries from the file.
   *
   * @return A list of String arrays, each containing a name and score
   */
  public List<String[]> fetchLeaderboardEntries() {
    FileHandle file = Gdx.files.local(leaderboardFilePath);
    List<String[]> entries = new ArrayList<>();
    if (!file.exists()) {
      System.out.println("WARNING: Couldn't load file " + leaderboardFilePath);
    } else {
      String text = file.readString();
      String[] lines = text.split("\\r?\\n");
      for (String line : lines) {
        entries.add(line.split(","));
      }
    }
    return entries;
  }

  /**
   * Stores a new entry in the leaderboard file.
   *
   * @param entry A string array containing a name and score
   */
  public void storeLeaderboardEntry(String[] entry) {
    FileHandle file = Gdx.files.local(leaderboardFilePath);
    String writeString = String.join(",", entry);
    writeString += "\n";
    file.writeString(writeString, true);
  }

  /**
   * Sorts the entries by score.
   *
   * @param entries A list of String arrays, each containing a name and score
   * @return The sorted list of entries
   */
  public List<String[]> getSortedEntries(List<String[]> entries) {
    Comparator<String[]> cmpr = Comparator.comparing(arr -> Float.parseFloat(arr[1]));
    entries.sort(cmpr);
    return entries;
  }
}
