package com.skloch.game;

import java.util.function.Predicate;

public class Achievement {
    public String title;
    public String description;
    public Predicate<Integer> conditions;
    public boolean achieved;

    public Achievement(String title, String description, Predicate<Integer> conditions) {
        this.title = title;
        this.description = description;
        this.conditions = conditions;
        this.achieved = false;
    }

    public void checkCondition(Integer x) {
        if(this.conditions.test(x)) {
            this.achieved = true;
        }
    }
}
