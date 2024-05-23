package com.skloch.game.tests.events;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.skloch.game.events.EventBus;
import com.skloch.game.events.TimeUpdatedEvent;
import java.util.function.Consumer;
import org.junit.Before;
import org.junit.Test;

/** Tests for the EventBus class. */
public class EventBusTests {
  private EventBus eventBus;

  @Before
  public void setUp() {
    eventBus = new EventBus();
  }

  @Test
  public void testEventSubscriptionAndPublishing() {
    // Create a mock consumer for TimeUpdatedEvent
    Consumer<TimeUpdatedEvent> listener = mock(Consumer.class);

    // Subscribe the listener to the TimeUpdatedEvent class
    eventBus.subscribe(TimeUpdatedEvent.class, listener);

    // Create a sample TimeUpdatedEvent
    TimeUpdatedEvent event = new TimeUpdatedEvent(100.0f);

    // Publish the event
    eventBus.publish(event);

    // Verify that the listener was invoked with the correct event
    verify(listener, times(1)).accept(event);
  }

  @Test
  public void testNoSubscribers() {
    // Create a sample event
    TimeUpdatedEvent event = new TimeUpdatedEvent(100.0f);

    // Publish the event without any subscribers
    eventBus.publish(event);

    // No exception should be thrown and nothing should happen
  }

  @Test
  public void testMultipleSubscribers() {
    // Create mock consumers for TimeUpdatedEvent
    Consumer<TimeUpdatedEvent> listener1 = mock(Consumer.class);
    Consumer<TimeUpdatedEvent> listener2 = mock(Consumer.class);

    // Subscribe the listeners to the TimeUpdatedEvent class
    eventBus.subscribe(TimeUpdatedEvent.class, listener1);
    eventBus.subscribe(TimeUpdatedEvent.class, listener2);

    // Create a sample TimeUpdatedEvent
    TimeUpdatedEvent event = new TimeUpdatedEvent(100.0f);

    // Publish the event
    eventBus.publish(event);

    // Verify that both listeners were invoked with the correct event
    verify(listener1, times(1)).accept(event);
    verify(listener2, times(1)).accept(event);
  }
}
