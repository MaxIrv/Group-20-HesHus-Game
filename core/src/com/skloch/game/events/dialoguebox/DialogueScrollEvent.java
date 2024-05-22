package com.skloch.game.events.dialoguebox;

/** Communicate from GameLogic to GameUI a dialogue scroll event. */
public class DialogueScrollEvent {
  private final float scrollSpeed;

  /**
   * Constructor for a DialogueScrollEvent.
   *
   * @param scrollSpeed the speed at which the dialogue should scroll
   */
  public DialogueScrollEvent(float scrollSpeed) {
    this.scrollSpeed = scrollSpeed;
  }

  /**
   * Get the speed at which the dialogue should scroll.
   *
   * @return the speed at which the dialogue should scroll
   */
  public float getScrollSpeed() {
    return scrollSpeed;
  }
}
