package com.skloch.game.events;

public class GameStatsUpdatedEvent {
    private final float daySeconds;
    private final int day;
    private final int hoursRecreational;
    private final int hoursStudied;
    private final int mealsEaten;
    private final int hoursSlept;

    public GameStatsUpdatedEvent(float daySeconds, int day, int hoursRecreational, int hoursStudied, int mealsEaten, int hoursSlept) {
        this.daySeconds = daySeconds;
        this.day = day;
        this.hoursRecreational = hoursRecreational;
        this.hoursStudied = hoursStudied;
        this.mealsEaten = mealsEaten;
        this.hoursSlept = hoursSlept;
    }

    public float getDaySeconds() {
        return daySeconds;
    }

    public int getDay() {
        return day;
    }

    public int getHoursRecreational() {
        return hoursRecreational;
    }

    public int getHoursStudied() {
        return hoursStudied;
    }

    public int getMealsEaten() {
        return mealsEaten;
    }

    public int getHoursSlept() {
        return hoursSlept;
    }
}

