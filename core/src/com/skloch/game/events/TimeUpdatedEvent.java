package com.skloch.game.events;

/**
 * The TimeUpdatedEvent class represents an event that is triggered when the time is updated in the game.
 * This event carries the updated time in seconds of the day.
 */
public class TimeUpdatedEvent {
    // The updated time in seconds of the day
    private final float daySeconds;

    /**
     * Constructs a new TimeUpdatedEvent with the specified updated time.
     *
     * @param daySeconds The updated time in seconds of the day.
     */
    public TimeUpdatedEvent(float daySeconds) {
        this.daySeconds = daySeconds;
    }

    /**
     * Returns the updated time in seconds of the day.
     *
     * @return The updated time in seconds of the day.
     */
    public float getDaySeconds() {
        return daySeconds;
    }
}