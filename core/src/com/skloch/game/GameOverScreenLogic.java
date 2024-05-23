package com.skloch.game;

/**
 * Logic for the Game Over screen, including achievements.
 */
public class GameOverScreenLogic {
  private final HustleGame game;
  private final int hoursStudied;
  private final int hoursRecreational;
  private final int hoursSlept;
  private final int mealsEaten;

  /**
   * Constructor for the GameOverScreenLogic.
   *
   * @param game              the game object
   * @param hoursStudied      the number of hours studied
   * @param hoursRecreational the number of hours spent on recreational activities
   * @param hoursSlept        the number of hours slept
   * @param mealsEaten        the number of meals eaten
   * @param score             the player's score
   */
  public GameOverScreenLogic(HustleGame game, int hoursStudied, int hoursRecreational,
                             int hoursSlept, int mealsEaten, float score) {
    this.game = game;
    this.hoursStudied = hoursStudied;
    this.hoursRecreational = hoursRecreational;
    this.hoursSlept = hoursSlept;
    this.mealsEaten = mealsEaten;
  }

  /**
   * Get the scores for the game over screen, including achievements.
   *
   * @return a 2D array of strings, where each row is a score and each column is a title and value
   */
  public String[][] getScores() {
    String[][] scores = {
        {"Hours Studied", String.valueOf(hoursStudied)},
        {"Recreational hours", String.valueOf(hoursRecreational)},
        {"Hours Slept", String.valueOf(hoursSlept)},
        {"Meals Eaten", String.valueOf(mealsEaten)}
    };

    if (game.studyStreak.getAchieved()) {
      scores = addAchievement(scores, game.studyStreak.getTitle());
    }
    if (game.bookWorm.getAchieved()) {
      scores = addAchievement(scores, game.bookWorm.getTitle());
    }
    if (game.eatStreak.getAchieved()) {
      scores = addAchievement(scores, game.eatStreak.getTitle());
    }
    if (game.funStreak.getAchieved()) {
      scores = addAchievement(scores, game.funStreak.getTitle());
    }
    if (game.allNighter.getAchieved()) {
      scores = addAchievement(scores, game.allNighter.getTitle());
    }

    return scores;
  }

  private String[][] addAchievement(String[][] scores, String achievement) {
    String[][] newScores = new String[scores.length + 1][2];
    for (int i = 0; i < scores.length; i++) {
      newScores[i] = scores[i];
    }
    newScores[scores.length] = new String[] {"Achievement", achievement};
    return newScores;
  }
}
