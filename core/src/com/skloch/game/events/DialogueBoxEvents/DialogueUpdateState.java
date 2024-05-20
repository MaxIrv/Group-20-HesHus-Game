package com.skloch.game.events.DialogueBoxEvents;

public class DialogueUpdateState {
    public enum State {
        HIDE,
        HIDE_SELECT_BOX,
        SHOW
    }

    private final State state;

    public DialogueUpdateState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
}
