package com.skloch.game.tests.components;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.skloch.game.EventManager;
import com.skloch.game.events.EventBus;
import com.skloch.game.events.FadeBlackScreenEvent;
import com.skloch.game.events.dialoguebox.DialogueSetText;
import com.skloch.game.interfaces.GameLogicInterface;
import com.skloch.game.mocks.MockedClasses;
import com.skloch.game.tests.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.when;

/** Tests for the EventManager class. */
@RunWith(GdxTestRunner.class)
public class EventManagerTests {
    private EventManager eventManager;
    private GameLogicInterface gameLogic;
    private EventBus eventBus;

    /**
     * Setup the mocked classes for the testing.
     */
    @Before
    public void setup() {
        gameLogic = MockedClasses.mockGameLogic();
        eventBus = MockedClasses.mockEventBus();

        eventManager = new EventManager(gameLogic, eventBus);
    }

    @Test
    public void testGetObjectInteraction_Rch() {
        when(gameLogic.getMeal()).thenReturn("breakfast");
        Assert.assertEquals(eventManager.getObjectInteraction("rch"), String.format("Eat %s at the Ron Cooke Hub?", gameLogic.getMeal()));
    }

    @Test
    public void testGetObjectInteraction_Chest() {
        Assert.assertEquals(eventManager.getObjectInteraction("chest"), "Open the chest?");
    }

    @Test
    public void testGetObjectInteraction_CompSci() {
        Assert.assertEquals(eventManager.getObjectInteraction("comp_sci"), "Study in the Computer Science building?");
    }

    @Test
    public void testGetObjectInteraction_Piazza() {
        Assert.assertEquals(eventManager.getObjectInteraction("piazza"), "Meet your friends at the Piazza?");
    }

    @Test
    public void testGetObjectInteraction_BusStop() {
        Assert.assertEquals(eventManager.getObjectInteraction("bus_stop"), "Get the bus?");
    }

    @Test
    public void testGetObjectInteraction_Houses() {
        Assert.assertEquals(eventManager.getObjectInteraction("houses"), "Open the door?");
    }

    @Test
    public void testGetObjectInteraction_Pub() {
        Assert.assertEquals(eventManager.getObjectInteraction("pub"), "Go for a drink with some friends?");
    }

    @Test
    public void testGetObjectInteraction_Accomodation() {
        Assert.assertEquals(eventManager.getObjectInteraction("accomodation"), "Go to sleep for the night?\nYour alarm is set for 8am.");
    }

    @Test
    public void testGetObjectInteraction_Tree() {
        Assert.assertEquals(eventManager.getObjectInteraction("tree"), "Speak to the tree?");
    }

    @Test
    public void testPiazzaEvent_TooEarly() {
        when(gameLogic.getEnergy()).thenReturn(100);
        when(gameLogic.getSeconds()).thenReturn(30.0f);
        eventManager.event("piazza");
        Assert.assertTrue(gameLogic.getEnergy() == 100);
    }

    @Test
    public void testPiazzaEvent_NotEnoughEnergy() {
        when(gameLogic.getEnergy()).thenReturn(0);
        eventManager.event("piazza");
        Assert.assertTrue(gameLogic.getEnergy() == 0);

    }
}