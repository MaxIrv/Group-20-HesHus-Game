package com.skloch.game;

import com.badlogic.gdx.utils.Predicate;

public class Achievement {
    public String title;
    public String description;
    public Predicate<Boolean> conditions;
    public boolean achieved;

    public Achievement(String title, String description, Predicate<Boolean> conditions) {
        this.title = title;
        this.description = description;
        this.conditions = conditions;
        this.achieved = false;
    }

    public void checkCondition() {
        if(this.conditions.evaluate(true)) {
            this.achieved = true;
        }
    }
}
