package com.skloch.game.tests.components;

import com.skloch.game.*;
import com.skloch.game.events.*;
import com.skloch.game.events.DialogueBoxEvents.*;
import com.skloch.game.mocks.MockedClasses;
import com.skloch.game.tests.GdxTestRunner;
import com.skloch.game.interfaces.IGameLogic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class EventManagerTests {
    private EventManager eventManager;
    private IGameLogic gameLogic;
    private EventBus eventBus;
    private FadeBlackScreenEvent fadeBlackScreenEvent;

    @Before
    public void setup() {
        gameLogic = MockedClasses.mockGameLogic();
        eventBus = MockedClasses.mockEventBus();
        fadeBlackScreenEvent = MockedClasses.mockFadeBlackScreenEvent();

        eventManager = new EventManager(gameLogic, eventBus);
    }

    @Test
    public void testFadeFromBlackEvent() {
        eventManager.event("fadefromblack");
        verify(eventBus).publish(any(FadeBlackScreenEvent.class));
    }

    @Test
    public void testTreeEvent() {
        eventManager.event("tree");
        verify(eventBus).publish(argThat(event -> event instanceof DialogueUpdateState && ((DialogueUpdateState) event).getState() == DialogueUpdateState.State.HIDE_SELECT_BOX));
        verify(eventBus).publish(argThat(event -> event instanceof DialogueSetText && ((DialogueSetText) event).getText().equals("The tree doesn't say anything back.")));
    }

    @Test
    public void testChestEvent() {
        eventManager.event("chest");
        verify(eventBus).publish(argThat(event -> event instanceof DialogueUpdateState && ((DialogueUpdateState) event).getState() == DialogueUpdateState.State.HIDE_SELECT_BOX));
        verify(eventBus).publish(argThat(event -> event instanceof DialogueSetText && ((DialogueSetText) event).getText().contains("Wow! This chest is full of so many magical items!")));
    }

    @Test
    public void testObjectEvent() {
        when(gameLogic.getCurrentMap()).thenReturn("campus");
        eventManager.event("bus_stop");
        verify(eventBus).publish(argThat(event -> event instanceof DialogueUpdateState && ((DialogueUpdateState) event).getState() == DialogueUpdateState.State.HIDE_SELECT_BOX));
        verify(gameLogic).switchMap(anyString());
    }

    @Test
    public void testGetObjectInteraction() {
        assertEquals("Open the chest?", eventManager.getObjectInteraction("chest"));
        when(eventManager.getObjectInteraction("rch")).thenReturn("lunch");
        assertEquals("Eat lunch at the Ron Cooke Hub?", eventManager.getObjectInteraction("rch")); // assuming getMeal() returns "lunch"
    }

    @Test
    public void testHasCustomObjectInteraction() {
        assertTrue(eventManager.hasCustomObjectInteraction("chest"));
        assertFalse(eventManager.hasCustomObjectInteraction("non_existing_key"));
    }

    @Test
    public void testPiazzaEvent() {
        when(gameLogic.getSeconds()).thenReturn((float) (9 * 60)); // 9 AM
        when(gameLogic.getEnergy()).thenReturn(50);

        eventManager.event("piazza");

        verify(eventBus).publish(argThat(event -> event instanceof DialogueSetText && ((DialogueSetText) event).getText().equals("What do you want to chat about?")));
        verify(eventBus).publish(any(DialogueSetOptions.class));
    }

    @Test
    public void testCompSciEvent() {
        when(gameLogic.getSeconds()).thenReturn((float) (9 * 60)); // 9 AM
        when(gameLogic.getEnergy()).thenReturn(50);

        eventManager.event("comp_sci");

        verify(eventBus).publish(argThat(event -> event instanceof DialogueSetText && ((DialogueSetText) event).getText().equals("Study for how long?")));
        verify(eventBus).publish(any(DialogueSetOptions.class));
    }

    @Test
    public void testAccomEvent() {
        when(gameLogic.getSeconds()).thenReturn((float) (10 * 60)); // 10 AM

        eventManager.event("accomodation");

        verify(eventBus).publish(argThat(event -> event instanceof DialogueUpdateState && ((DialogueUpdateState) event).getState() == DialogueUpdateState.State.HIDE));
        verify(eventBus).publish(any(FadeBlackScreenEvent.class));
    }
}