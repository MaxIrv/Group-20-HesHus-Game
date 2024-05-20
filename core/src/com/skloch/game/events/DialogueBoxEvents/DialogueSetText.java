package com.skloch.game.events.DialogueBoxEvents;

public class DialogueSetText {
    private final String text;
    private final String eventKey;

    public DialogueSetText(String text) {
        this.text = text;
        this.eventKey = null;
    }

    public DialogueSetText(String text, String eventKey) {
        this.text = text;
        this.eventKey = eventKey;
    }

    public String getText() {
        return text;
    }

    public String getEventKey() {
        return eventKey;
    }
}
