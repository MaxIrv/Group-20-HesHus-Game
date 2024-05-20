package com.skloch.game.tests.components;

import com.skloch.game.*;
import com.skloch.game.events.*;
import com.skloch.game.mocks.MockedClasses;
import com.skloch.game.tests.GdxTestRunner;
import com.skloch.game.interfaces.GameScreenProvider;
import com.skloch.game.interfaces.IPlayer;
import com.skloch.game.interfaces.IGameLogic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class EventManagerTests {
    private EventManager eventManager;
    private IGameLogic gameLogic;
    private EventBus eventBus;
    @Before
    public void setup() {
        gameLogic = MockedClasses.mockGameLogic();
        eventBus = MockedClasses.mockEventBus();

        eventManager = new EventManager(gameLogic, eventBus);
    }

    @Test
    public void testEventFadeFromBlack() {

    }

    @Test
    public void testEventFadeToBlack() {

    }

    @Test
    public void testGetObjectInteraction_Rch() {

    }

    @Test
    public void testGetObjectInteraction_Chest() {

    }

    @Test
    public void testHasCustomObjectInteraction_Chest() {

    }

    @Test
    public void testTreeEvent() {

    }

    @Test
    public void testChestEvent() {

    }

    @Test
    public void testPiazzaEvent_TooEarly() {

    }

    @Test
    public void testPiazzaEvent_NotEnoughEnergy() {

    }

    @Test
    public void testPiazzaEvent_Chats() {

    }

    @Test
    public void testPiazzaEvent_ChatsTopic() {

    }
}