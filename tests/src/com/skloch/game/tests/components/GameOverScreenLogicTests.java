package com.skloch.game.tests.components;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.skloch.game.Achievement;
import com.skloch.game.GameOverScreenLogic;
import com.skloch.game.HustleGame;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the GameOverScreenLogic class.
 */
public class GameOverScreenLogicTests {

  private HustleGame game;

  /**
   * Initialise the HustleGame and mock the achievements.
   */
  @Before
  public void setup() {
    game = mock(HustleGame.class);
    game.studyStreak = mock(Achievement.class);
    game.bookWorm = mock(Achievement.class);
    game.eatStreak = mock(Achievement.class);
    game.funStreak = mock(Achievement.class);
    game.allNighter = mock(Achievement.class);
  }

  @Test
  public void testGetScoresWithAllAchievements() {
    when(game.studyStreak.getAchieved()).thenReturn(true);
    when(game.bookWorm.getAchieved()).thenReturn(true);
    when(game.eatStreak.getAchieved()).thenReturn(true);
    when(game.funStreak.getAchieved()).thenReturn(true);
    when(game.allNighter.getAchieved()).thenReturn(true);

    when(game.studyStreak.getTitle()).thenReturn("Study Streak Title");
    when(game.bookWorm.getTitle()).thenReturn("Book Worm Title");
    when(game.eatStreak.getTitle()).thenReturn("Eat Streak Title");
    when(game.funStreak.getTitle()).thenReturn("Fun Streak Title");
    when(game.allNighter.getTitle()).thenReturn("All Nighter Title");

    GameOverScreenLogic gameOverScreenLogic = new GameOverScreenLogic(game, 10, 5, 8, 6, 75.0f);

    String[][] expectedScores = {
        {"Hours Studied", "10"},
        {"Recreational hours", "5"},
        {"Hours Slept", "8"},
        {"Meals Eaten", "6"},
        {"Achievement", "Study Streak Title"},
        {"Achievement", "Book Worm Title"},
        {"Achievement", "Eat Streak Title"},
        {"Achievement", "Fun Streak Title"},
        {"Achievement", "All Nighter Title"}
    };

    String[][] scores = gameOverScreenLogic.getScores();
    assertArrayEquals(expectedScores, scores);
  }

  @Test
  public void testGetScoresWithNoAchievements() {
    when(game.studyStreak.getAchieved()).thenReturn(false);
    when(game.bookWorm.getAchieved()).thenReturn(false);
    when(game.eatStreak.getAchieved()).thenReturn(false);
    when(game.funStreak.getAchieved()).thenReturn(false);
    when(game.allNighter.getAchieved()).thenReturn(false);

    GameOverScreenLogic gameOverScreenLogic = new GameOverScreenLogic(game, 10, 5, 8, 6, 75.0f);

    String[][] expectedScores = {
        {"Hours Studied", "10"},
        {"Recreational hours", "5"},
        {"Hours Slept", "8"},
        {"Meals Eaten", "6"}
    };

    String[][] scores = gameOverScreenLogic.getScores();
    assertArrayEquals(expectedScores, scores);
  }

  @Test
  public void testGetScoresWithSomeAchievements() {
    when(game.studyStreak.getAchieved()).thenReturn(true);
    when(game.bookWorm.getAchieved()).thenReturn(false);
    when(game.eatStreak.getAchieved()).thenReturn(true);
    when(game.funStreak.getAchieved()).thenReturn(false);
    when(game.allNighter.getAchieved()).thenReturn(true);

    when(game.studyStreak.getTitle()).thenReturn("Study Streak Title");
    when(game.eatStreak.getTitle()).thenReturn("Eat Streak Title");
    when(game.allNighter.getTitle()).thenReturn("All Nighter Title");

    GameOverScreenLogic gameOverScreenLogic = new GameOverScreenLogic(game, 10, 5, 8, 6, 75.0f);

    String[][] expectedScores = {
        {"Hours Studied", "10"},
        {"Recreational hours", "5"},
        {"Hours Slept", "8"},
        {"Meals Eaten", "6"},
        {"Achievement", "Study Streak Title"},
        {"Achievement", "Eat Streak Title"},
        {"Achievement", "All Nighter Title"}
    };

    String[][] scores = gameOverScreenLogic.getScores();
    assertArrayEquals(expectedScores, scores);
  }
}
