package com.skloch.game;

import java.util.function.Predicate;

/** Represents an achievement in the game. */
public class Achievement {
  private String title;
  private String description;
  private Predicate<Integer> conditions;
  private boolean achieved;

  /**
   * Creates an achievement with the given title, description, and conditions.
   *
   * @param title the title of the achievement
   * @param description the description of the achievement
   * @param conditions the conditions that must be met to achieve the achievement
   */
  public Achievement(String title, String description, Predicate<Integer> conditions) {
    this.title = title;
    this.description = description;
    this.conditions = conditions;
    this.achieved = false;
  }

  /**
   * Checks if the conditions for the achievement are met and sets the achievement to achieved if
   * they are.
   *
   * @param x the value to check the conditions against
   */
  public void checkCondition(Integer x) {
    if (this.getConditions().test(x)) {
      this.setAchieved(true);
    }
  }

  // Getters and Setters
  public String getTitle() {
    return this.title;
  }

  public void setTitle(String x) {
    this.title = x;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String x) {
    this.description = x;
  }

  public Predicate<Integer> getConditions() {
    return this.conditions;
  }

  public void setConditions(Predicate<Integer> x) {
    this.conditions = x;
  }

  public boolean getAchieved() {
    return this.achieved;
  }

  public void setAchieved(boolean x) {
    this.achieved = x;
  }
}
