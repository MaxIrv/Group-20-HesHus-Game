package com.skloch.game.events.DialogueBoxEvents;

/**
 * The DialogueSetText class represents an event that is triggered when the text of a dialogue box is set in the game.
 * This event carries the new text and an optional event key associated with the text.
 */
public class DialogueSetText {
    // The new text to be displayed in the dialogue box
    private final String text;
    // An optional event key associated with the text
    private final String eventKey;

    /**
     * Constructs a new DialogueSetText event with the specified text.
     *
     * @param text The new text to be displayed in the dialogue box.
     */
    public DialogueSetText(String text) {
        this.text = text;
        this.eventKey = null;
    }

    /**
     * Constructs a new DialogueSetText event with the specified text and event key.
     *
     * @param text The new text to be displayed in the dialogue box.
     * @param eventKey An optional event key associated with the text.
     */
    public DialogueSetText(String text, String eventKey) {
        this.text = text;
        this.eventKey = eventKey;
    }

    /**
     * Returns the new text to be displayed in the dialogue box.
     *
     * @return The new text to be displayed in the dialogue box.
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the event key associated with the text.
     *
     * @return The event key associated with the text, or null if none was specified.
     */
    public String getEventKey() {
        return eventKey;
    }
}