package com.skloch.game.events.DialogueBoxEvents;

/**
 * Communicate from GameLogic to GameUI a dialogue scroll event.
 */
public class DialogueScrollEvent {
    private final float scrollSpeed;

    /**
     * @param scrollSpeed the speed at which the dialogue should scroll
     */
    public DialogueScrollEvent(float scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    /**
     * @return the speed at which the dialogue should scroll
     */
    public float getScrollSpeed() {
        return scrollSpeed;
    }
}
