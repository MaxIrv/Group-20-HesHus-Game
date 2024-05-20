package com.skloch.game.events;


import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;

public class FadeBlackScreenEvent {
    private final Action action;
    private final RunnableAction runnableAction;

    // Constructor without RunnableAction
    public FadeBlackScreenEvent(Action action) {
        this(action, null);
    }

    // Constructor with RunnableAction
    public FadeBlackScreenEvent(Action action, RunnableAction runnableAction) {
        this.action = action;
        this.runnableAction = runnableAction;
    }

    public Action getAction() {
        return action;
    }

    public RunnableAction getRunnableAction() {
        return runnableAction;
    }
}
