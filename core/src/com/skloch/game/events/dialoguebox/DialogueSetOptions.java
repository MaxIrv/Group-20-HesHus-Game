package com.skloch.game.events.dialoguebox;

/**
 * The DialogueSetOptions class represents a set of dialogue options and their corresponding events.
 * This class is used to handle the options presented to the player in a dialogue box and the events
 * triggered by selecting those options.
 */
public class DialogueSetOptions {
  private final String[] options;
  private final String[] events;

  /**
   * Constructs a new DialogueSetOptions object with the specified options and events.
   *
   * @param options the array of dialogue options
   * @param events the array of events corresponding to each dialogue option
   */
  public DialogueSetOptions(String[] options, String[] events) {
    this.options = options;
    this.events = events;
  }

  /**
   * Returns the array of dialogue options.
   *
   * @return the array of dialogue options
   */
  public String[] getOptions() {
    return options;
  }

  /**
   * Returns the array of events corresponding to the dialogue options.
   *
   * @return the array of events
   */
  public String[] getEvents() {
    return events;
  }
}
