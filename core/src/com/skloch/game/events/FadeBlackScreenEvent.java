package com.skloch.game.events;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;

/**
 * The FadeBlackScreenEvent class represents an event that is triggered when the screen needs to
 * fade to black. This event carries an Action that defines the fade animation and an optional
 * RunnableAction that can be executed after the fade.
 */
public class FadeBlackScreenEvent {
  // The Action that defines the fade animation
  private final Action action;
  // The optional RunnableAction that can be executed after the fade
  private final RunnableAction runnableAction;

  /**
   * Constructs a new FadeBlackScreenEvent with the specified Action.
   *
   * @param action The Action that defines the fade animation.
   */
  public FadeBlackScreenEvent(Action action) {
    this(action, null);
  }

  /**
   * Constructs a new FadeBlackScreenEvent with the specified Action and RunnableAction.
   *
   * @param action The Action that defines the fade animation.
   * @param runnableAction The RunnableAction that can be executed after the fade.
   */
  public FadeBlackScreenEvent(Action action, RunnableAction runnableAction) {
    this.action = action;
    this.runnableAction = runnableAction;
  }

  /**
   * Returns the Action that defines the fade animation.
   *
   * @return The Action that defines the fade animation.
   */
  public Action getAction() {
    return action;
  }

  /**
   * Returns the RunnableAction that can be executed after the fade.
   *
   * @return The RunnableAction that can be executed after the fade.
   */
  public RunnableAction getRunnableAction() {
    return runnableAction;
  }
}
