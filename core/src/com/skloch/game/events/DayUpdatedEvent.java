package com.skloch.game.events;

/**
 * The DayUpdatedEvent class represents an event that is triggered when the day is updated in the
 * game. This event carries the updated day.
 */
public class DayUpdatedEvent {
  // The updated day
  private final int day;

  /**
   * Constructs a new DayUpdatedEvent with the specified updated day.
   *
   * @param day The updated day.
   */
  public DayUpdatedEvent(int day) {
    this.day = day;
  }

  /**
   * Returns the updated day.
   *
   * @return The updated day.
   */
  public int getDay() {
    return day;
  }
}
