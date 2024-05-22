package com.skloch.game.events;

/**
 * The GameStatsUpdatedEvent class represents an event that is triggered when the game statistics
 * are updated. This event carries the updated game statistics including the day seconds, day, hours
 * of recreational activities, hours studied, meals eaten, and hours slept.
 */
public class GameStatsUpdatedEvent {
  // The updated time in seconds of the day
  private final float daySeconds;
  // The updated day
  private final int day;
  // The updated hours of recreational activities
  private final int hoursRecreational;
  // The updated hours studied
  private final int hoursStudied;
  // The updated number of meals eaten
  private final int mealsEaten;
  // The updated hours slept
  private final int hoursSlept;

  /**
   * Constructs a new GameStatsUpdatedEvent with the specified updated game statistics.
   *
   * @param daySeconds The updated time in seconds of the day.
   * @param day The updated day.
   * @param hoursRecreational The updated hours of recreational activities.
   * @param hoursStudied The updated hours studied.
   * @param mealsEaten The updated number of meals eaten.
   * @param hoursSlept The updated hours slept.
   */
  public GameStatsUpdatedEvent(
      float daySeconds,
      int day,
      int hoursRecreational,
      int hoursStudied,
      int mealsEaten,
      int hoursSlept) {
    this.daySeconds = daySeconds;
    this.day = day;
    this.hoursRecreational = hoursRecreational;
    this.hoursStudied = hoursStudied;
    this.mealsEaten = mealsEaten;
    this.hoursSlept = hoursSlept;
  }

  /**
   * Returns the updated time in seconds of the day.
   *
   * @return The updated time in seconds of the day.
   */
  public float getDaySeconds() {
    return daySeconds;
  }

  /**
   * Returns the updated day.
   *
   * @return The updated day.
   */
  public int getDay() {
    return day;
  }

  /**
   * Returns the updated hours of recreational activities.
   *
   * @return The updated hours of recreational activities.
   */
  public int getHoursRecreational() {
    return hoursRecreational;
  }

  /**
   * Returns the updated hours studied.
   *
   * @return The updated hours studied.
   */
  public int getHoursStudied() {
    return hoursStudied;
  }

  /**
   * Returns the updated number of meals eaten.
   *
   * @return The updated number of meals eaten.
   */
  public int getMealsEaten() {
    return mealsEaten;
  }

  /**
   * Returns the updated hours slept.
   *
   * @return The updated hours slept.
   */
  public int getHoursSlept() {
    return hoursSlept;
  }
}
