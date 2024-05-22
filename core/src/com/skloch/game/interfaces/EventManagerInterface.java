package com.skloch.game.interfaces;

/** Interface for the event manager. */
public interface EventManagerInterface {
  void event(String event);

  String getObjectInteraction(String key);

  boolean hasCustomObjectInteraction(String key);
}
