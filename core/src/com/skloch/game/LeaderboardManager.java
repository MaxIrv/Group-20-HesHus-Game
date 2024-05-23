package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaderboardManager {
  private String playerName;
  private float playerScore;
  public final String leaderboardFilePath = "scores.csv";

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

  public void storeLeaderboardEntry(String[] entry) {
    FileHandle file = Gdx.files.local(leaderboardFilePath);
    String writeString = String.join(",", entry);
    writeString += "\n";
    file.writeString(writeString, true);
  }

  public List<String[]> getSortedEntries(List<String[]> entries) {
    Comparator<String[]> cmpr = Comparator.comparing(arr -> Float.parseFloat(arr[1]));
    entries.sort(cmpr);
    return entries;
  }
}
