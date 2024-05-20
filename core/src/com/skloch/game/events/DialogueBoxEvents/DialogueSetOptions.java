package com.skloch.game.events.DialogueBoxEvents;

public class DialogueSetOptions {
    private final String[] options;
    private final String[] events;

    public DialogueSetOptions(String[] options, String[] events) {
        this.options = options;
        this.events = events;
    }

    public String[] getOptions() {
        return options;
    }

    public String[] getEvents() {
        return events;
    }
}
