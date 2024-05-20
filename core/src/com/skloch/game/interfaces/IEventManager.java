package com.skloch.game.interfaces;

public interface IEventManager {
    void event(String event);
    String getObjectInteraction(String key);
    boolean hasCustomObjectInteraction(String key);
}
