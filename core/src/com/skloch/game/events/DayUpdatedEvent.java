package com.skloch.game.events;

public class DayUpdatedEvent {
    private final int day;

    public DayUpdatedEvent(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }
}
