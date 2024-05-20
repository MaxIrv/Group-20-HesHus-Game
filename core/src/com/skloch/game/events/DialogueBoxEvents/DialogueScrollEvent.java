package com.skloch.game.events.DialogueBoxEvents;

public class DialogueScrollEvent {
    private final float scrollSpeed;

    public DialogueScrollEvent(float scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    public float getScrollSpeed() {
        return scrollSpeed;
    }
}
