package com.skloch.game.tests.components;

import com.skloch.game.InterfaceEventManager;
import com.skloch.game.events.EventBus;
import com.skloch.game.interfaces.GameLogicInterface;
import com.skloch.game.mocks.MockedClasses;
import com.skloch.game.tests.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for the EventManager class. */
@RunWith(GdxTestRunner.class)
public class EventManagerTests {
  private InterfaceEventManager eventManager;
  private GameLogicInterface gameLogic;
  private EventBus eventBus;

  /** Setup the mocked classes for the testing. */
  @Before
  public void setup() {
    gameLogic = MockedClasses.mockGameLogic();
    eventBus = MockedClasses.mockEventBus();

    eventManager = new InterfaceEventManager(gameLogic, eventBus);
  }

  @Test
  public void testEventFadeFromBlack() {}

  @Test
  public void testEventFadeToBlack() {}

  @Test
  public void testGetObjectInteraction_Rch() {}

  @Test
  public void testGetObjectInteraction_Chest() {}

  @Test
  public void testHasCustomObjectInteraction_Chest() {}

  @Test
  public void testTreeEvent() {}

  @Test
  public void testChestEvent() {}

  @Test
  public void testPiazzaEvent_TooEarly() {}

  @Test
  public void testPiazzaEvent_NotEnoughEnergy() {}

  @Test
  public void testPiazzaEvent_Chats() {}

  @Test
  public void testPiazzaEvent_ChatsTopic() {}
}
