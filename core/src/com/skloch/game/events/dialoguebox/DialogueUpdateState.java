package com.skloch.game.events.dialoguebox;

/**
 * The DialogueUpdateState class represents an event that is triggered when the state of a dialogue
 * box is updated in the game. This event carries the updated state of the dialogue box.
 */
public class DialogueUpdateState {

  /** The State enum defines the possible states of a dialogue box. */
  public enum State {
    HIDE, // The dialogue box is hidden
    HIDE_SELECT_BOX, // The dialogue box is hidden but the select box is shown
    SHOW // The dialogue box is shown
  }

  // The updated state of the dialogue box
  private final State state;

  /**
   * Constructs a new DialogueUpdateState with the specified updated state.
   *
   * @param state The updated state of the dialogue box.
   */
  public DialogueUpdateState(State state) {
    this.state = state;
  }

  /**
   * Returns the updated state of the dialogue box.
   *
   * @return The updated state of the dialogue box.
   */
  public State getState() {
    return state;
  }
}
