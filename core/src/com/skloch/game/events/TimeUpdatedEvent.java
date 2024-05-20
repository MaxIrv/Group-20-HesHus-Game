package com.skloch.game.events;

public class TimeUpdatedEvent {
    private final float daySeconds;

    public TimeUpdatedEvent(float daySeconds) {
        this.daySeconds = daySeconds;
    }

    public float getDaySeconds() {
        return daySeconds;
    }
}
