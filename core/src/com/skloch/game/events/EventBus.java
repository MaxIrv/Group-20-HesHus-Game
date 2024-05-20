package com.skloch.game.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A simple event bus that allows subscribing to and publishing events.
 * The EventBus maintains a mapping of event types to sets of listeners.
 */
public class EventBus {
    // A map that associates event types with their respective listeners
    private final Map<Class<?>, Set<Consumer<?>>> listeners = new HashMap<>();

    /**
     * Subscribe to an event type.
     *
     * @param eventType The event type to subscribe to.
     * @param listener  The listener that will be called when the event is published.
     * @param <T>       The type of the event.
     */
    public <T> void subscribe(Class<T> eventType, Consumer<T> listener) {
        // Adds the listener to the set of listeners for the specified event type
        listeners.computeIfAbsent(eventType, k -> new HashSet<>()).add(listener);
    }

    /**
     * Publish an event.
     *
     * @param event The event to publish.
     * @param <T>   The type of the event.
     */
    public <T> void publish(T event) {
        // Get the listeners for the event type
        Set<Consumer<?>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            // Call each listener
            for (Consumer<?> listener : eventListeners) {
                // Cast the listener to the correct type and call it
                // This is safe because we only add listeners of the correct type
                @SuppressWarnings("unchecked")
                Consumer<T> consumer = (Consumer<T>) listener;
                consumer.accept(event);
            }
        }
    }
}