package com.skloch.game;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.utils.Array;
import com.skloch.game.events.EventBus;
import com.skloch.game.events.FadeBlackScreenEvent;
import com.skloch.game.events.dialoguebox.DialogueSetOptions;
import com.skloch.game.events.dialoguebox.DialogueSetText;
import com.skloch.game.events.dialoguebox.DialogueUpdateState;
import com.skloch.game.interfaces.EventManagerInterface;
import com.skloch.game.interfaces.GameLogicInterface;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A class that maps Object's event strings to actual Java functions.
 */
public class EventManager implements EventManagerInterface {
  private final GameLogicInterface gameLogic;
  private final EventBus eventBus;
  private final HashMap<String, Integer> activityEnergies;
  private final HashMap<String, String> objectInteractions;
  private final Array<String> talkTopics;

  private boolean studied = false;
  private boolean studiedTwice = false;
  private boolean hadFun = false;
  private boolean ate = false;
  private boolean noSleep = false;

  /**
   * A class that maps Object's event strings to actual Java functions. To run a function call
   * event(eventString), to add arguments add dashes. E.g. a call to the Piazza function with an arg
   * of 1 would be: "piazza-1" Which the function interprets as "study at the piazza for 1 hour".
   * Object's event strings can be set in the Tiled map editor with a property called "event"
   *
   * @param gameLogic The game logic object
   * @param eventBus  The event bus object
   */
  public EventManager(GameLogicInterface gameLogic, EventBus eventBus) {
    this.gameLogic = gameLogic;
    this.eventBus = eventBus;

    // How much energy an hour of each activity should take
    activityEnergies = new HashMap<String, Integer>();
    activityEnergies.put("studying", 10);
    activityEnergies.put("meet_friends", 10);
    activityEnergies.put("pub", 10);
    activityEnergies.put("eating", 10);

    // Define what to say when interacting with an object who's text won't change
    objectInteractions = new HashMap<String, String>();
    objectInteractions.put("chest", "Open the chest?");
    objectInteractions.put("comp_sci", "Study in the Computer Science building?");
    objectInteractions.put("piazza", "Meet your friends at the Piazza?");
    objectInteractions.put("bus_stop", "Get the bus?");
    objectInteractions.put("houses", "Open the door?");
    objectInteractions.put("pub", "Go for a drink with some friends?");
    objectInteractions.put("accomodation",
        "Go to sleep for the night?\nYour alarm is set for 8am.");
    objectInteractions.put("rch", null); // Changes, dynamically returned in getObjectInteraction
    objectInteractions.put("tree", "Speak to the tree?");

    // Some random topics that can be chatted about
    String[] topics = {
        "Dogs",
        "Cats",
        "Exams",
        "Celebrities",
        "Flatmates",
        "Video games",
        "Sports",
        "Food",
        "Fashion"
    };
    talkTopics = new Array<String>(topics);
  }

  /**
   * Calls the appropriate function based on the event key.
   *
   * @param eventKey The key of the event to be called
   */
  @Override
  public void event(String eventKey) {
    String[] args = eventKey.split("-");

    // Important functions, most likely called after displaying text
    switch (args[0]) {
      case "fadefromblack":
        fadeFromBlack();
        break;
      case "fadetoblack":
        fadeToBlack();
        break;
      default:
        break;
    }

    // Events related to objects
    switch (args[0]) {
      case "pub":
        pubEvent(args);
        break;
      case "restaurant":
        restaurantEvent();
        break;
      case "tree":
        treeEvent();
        break;
      case "houses":
        housesEvent();
        break;
      case "chest":
        chestEvent();
        break;
      case "piazza":
        piazzaEvent(args);
        break;
      case "comp_sci":
        compSciEvent(args);
        break;
      case "rch":
        ronCookeEvent(args);
        break;
      case "accomodation":
        accomEvent(args);
        break;
      case "bus_stop":
        busStopEvent();
        break;
      case "exit":
        // Should do nothing and just close the dialogue menu
        eventBus.publish(new DialogueUpdateState(DialogueUpdateState.State.HIDE));
        //                gameScreen.dialogueBox.hide();
        break;
      default:
        objectEvent(eventKey);
        break;
    }
  }

  /**
   * Gets the interaction text associated with each object via a key.
   *
   * @param key The key of the object
   * @return The object interaction text
   */
  @Override
  public String getObjectInteraction(String key) {
    if (key.equals("rch")) {
      return String.format("Eat %s at the Ron Cooke Hub?", gameLogic.getMeal());
    } else {
      return objectInteractions.get(key);
    }
  }

  /**
   * Checks if the object has some custom text to display that isn't just "This is an x!".
   *
   * @return True if the object has some custom text to display that isn't just "This is an x!"
   */
  @Override
  public boolean hasCustomObjectInteraction(String key) {
    return objectInteractions.containsKey(key);
  }

  /**
   * Sets the text when talking to a tree.
   */
  private void treeEvent() {
    //        eventBus.publish(new DialogueUpdateState(DialogueUpdateState.State.HIDE_SELECT_BOX));
    eventBus.publish(new DialogueUpdateState(DialogueUpdateState.State.HIDE_SELECT_BOX));
    eventBus.publish(new DialogueSetText("The tree doesn't say anything back."));
  }

  /**
   * A simple event to handle interaction with houses (other than your own).
   */
  public void housesEvent() {
    eventBus.publish(new DialogueUpdateState(DialogueUpdateState.State.HIDE_SELECT_BOX));
    eventBus.publish(new DialogueSetText("That's not your house silly."));
  }

  /**
   * Sets the text when opening a chest.
   */
  private void chestEvent() {
    eventBus.publish(new DialogueUpdateState(DialogueUpdateState.State.HIDE_SELECT_BOX));
    eventBus.publish(
        new DialogueSetText(
            "Wow! This chest is full of so many magical items! "
                + "I wonder how they will help you out on your journey! "
                + "Boy, this is an awfully long piece of text, "
                + "I wonder if someone is testing something?\n...\n...\n...\nHow cool!"));
  }

  /**
   * Sets the text when talking to an object without a dedicated function.
   *
   * @param object The object to be published in the dialogue box
   */
  private void objectEvent(String object) {
    eventBus.publish(new DialogueUpdateState(DialogueUpdateState.State.HIDE_SELECT_BOX));
    eventBus.publish(new DialogueSetText("This is a " + object + "!"));
  }

  /**
   * Lets the player study at the piazza for x num of hours, decreases the player's energy and
   * increments the game time.
   *
   * @param args Arguments to be passed, should contain the hours the player wants to study. E.g.
   *             ["piazza", "1"]
   */
  private void piazzaEvent(String[] args) {
    if (gameLogic.getSeconds() > 8 * 60) {
      int energyCost = activityEnergies.get("meet_friends");
      // If the player is too tired to meet friends
      if (gameLogic.getEnergy() < energyCost) {
        eventBus.publish(new DialogueSetText("You are too tired to meet your friends right now!"));

      } else if (args.length == 1) {
        // Ask the player to chat about something (makes no difference)
        String[] topics = randomTopics();
        eventBus.publish(new DialogueSetText("What do you want to chat about?"));
        eventBus.publish(
            new DialogueSetOptions(
                topics,
                new String[] {
                    "piazza-" + topics[0], "piazza-" + topics[1], "piazza-" + topics[2]
                }));
        //                gameScreen.dialogueBox.getSelectBox().setOptions(topics, new
        // String[]{"piazza-"+topics[0], "piazza-"+topics[1], "piazza-"+topics[2]});
      } else {
        // Say that the player chatted about this topic for 1-3 hours
        // RNG factor adds a slight difficulty (may consume too much energy to study)
        int hours = ThreadLocalRandom.current().nextInt(1, 4);
        eventBus.publish(
            new DialogueSetText(
                String.format("You talked about %s for %d hours!", args[1].toLowerCase(), hours)));
        gameLogic.decreaseEnergy(energyCost * hours);
        gameLogic.passTime(hours * 60); // in seconds
        gameLogic.addRecreationalHours(hours);
        hadFun = true;
      }
    } else {
      eventBus.publish(
          new DialogueSetText("It's too early in the morning to meet your friends, go to bed!"));
    }
  }

  /**
   * Generates 3 random topics for the player to chat about.
   *
   * @return An array of 3 random topics the player can chat about
   */
  private String[] randomTopics() {
    // Returns an array of 3 random topics
    Array<String> topics = new Array<String>(3);

    for (int i = 0; i < 3; i++) {
      String choice = talkTopics.random();
      // If statement to ensure topic hasn't already been selected
      if (!topics.contains(choice, false)) {
        topics.add(choice);
      } else {
        i -= 1;
      }
    }

    return topics.toArray(String.class);
  }

  /**
   * The event to be run when interacting with the computer science building Gives the player the
   * option to study for 2, 3 or 4 hours.
   *
   * @param args The arguments passed to the function, should contain the hours the player wants to
   *             study
   */
  private void compSciEvent(String[] args) {
    if (gameLogic.getSeconds() > 8 * 60) {
      int energyCost = activityEnergies.get("studying");
      // If the player is too tired for any studying:
      if (gameLogic.getEnergy() < energyCost) {
        eventBus.publish(new DialogueUpdateState(DialogueUpdateState.State.HIDE_SELECT_BOX));
        eventBus.publish(new DialogueSetText("You are too tired to study right now!"));
        //                eventBus.publish(new DialogueSetText("You are too tired to study right
        // now!");
      } else if (args.length == 1) {
        // If the player has not yet chosen how many hours, ask
        eventBus.publish(new DialogueSetText("Study for how long?"));
        eventBus.publish(
            new DialogueSetOptions(
                new String[] {"2 Hours (20)", "3 Hours (30)", "4 Hours (40)"},
                new String[] {"comp_sci-2", "comp_sci-3", "comp_sci-4"}));
        //                gameScreen.dialogueBox.getSelectBox().setOptions(new String[]{"2 Hours
        // (20)", "3 Hours (30)", "4 Hours (40)"}, new String[]{"comp_sci-2", "comp_sci-3",
        // "comp_sci-4"});
      } else {
        int hours = Integer.parseInt(args[1]);
        // If the player does not have enough energy for the selected hours
        if (gameLogic.getEnergy() < hours * energyCost) {
          eventBus.publish(
              new DialogueSetText("You don't have the energy to study for this long!"));
        } else {
          // If they do have the energy to study
          eventBus.publish(
              new DialogueSetText(
                  String.format(
                      "You studied for %s hours!\nYou lost %d energy",
                      args[1], hours * energyCost)));
          gameLogic.decreaseEnergy(energyCost * hours);
          gameLogic.addStudyHours(hours);
          gameLogic.passTime(hours * 60); // in seconds
          if (studied) {
            studiedTwice = true;
          } else {
            studied = true;
          }
        }
      }
    } else {
      eventBus.publish(new DialogueSetText("It's too early in the morning to study, go to bed!"));
    }
  }

  /**
   * The event for the player to interact with a pub.
   *
   * @param args arguments, not used currently but will use for future expansion.
   */
  public void pubEvent(String[] args) {
    if (gameLogic.getSeconds() > 8 * 60) {
      int energyCost = activityEnergies.get("pub");
      if (gameLogic.getEnergy() < energyCost) {
        eventBus.publish(new DialogueUpdateState(DialogueUpdateState.State.HIDE_SELECT_BOX));
        eventBus.publish(new DialogueSetText("You are too tired to go to the pub right now!"));
      } else if (args.length == 1) {
        // If the player has not yet chosen how many hours, ask
        eventBus.publish(new DialogueSetText("How long do you want to stay?"));
        eventBus.publish(
            new DialogueSetOptions(
                new String[] {"1 Hours (10)", "2 Hours (20)", "3 Hours (30)"},
                new String[] {"pub-1", "pub-2", "pub-3"}));
      } else {
        int hours = Integer.parseInt(args[1]);
        // If the player does not have enough energy to stay for that long
        if (gameLogic.getEnergy() < hours * energyCost) {
          eventBus.publish(new DialogueSetText("You don't have enough energy to stay that long!"));
        } else {
          eventBus.publish(new DialogueSetText("You had a drink with some friends."));
          gameLogic.decreaseEnergy(energyCost * hours);
          gameLogic.addRecreationalHours(hours);
          gameLogic.passTime(hours * 60); // in seconds
        }
      }
    }
  }

  /**
   * The event to be run when the player interacts with the ron cooke hub Gives the player the
   * choice to eat breakfast, lunch or dinner depending on the time of day.
   *
   * @param args Should contain the meal the player wants to eat
   */
  public void ronCookeEvent(String[] args) {
    eatingEvent("the Ron Cooke Hub");
  }

  /**
   * The event to handle interaction with restaurants Gives the player the choice to eat breakfast,
   * lunch or dinner depending on the time of day.
   */
  public void restaurantEvent() {
    eatingEvent("a restaurant");
  }

  /**
   * To handle events that involve eating.
   *
   * @param placeName the name of the place the player will be eating
   */
  private void eatingEvent(String placeName) {
    if (gameLogic.getSeconds() > 8 * 60) {
      int energyCost = activityEnergies.get("eating");
      if (gameLogic.getEnergy() < energyCost) {
        eventBus.publish(new DialogueSetText("You are too tired to eat right now!"));
      } else {

        eventBus.publish(
            new DialogueSetText(
                String.format(
                    "You took an hour to eat %s at %s!\nYou lost %d energy!",
                    gameLogic.getMeal(), placeName, energyCost)));

        gameLogic.addMeal();
        gameLogic.decreaseEnergy(energyCost);
        gameLogic.passTime(60); // in seconds
        ate = true;
      }
    } else {
      eventBus.publish(
          new DialogueSetText("It's too early in the morning to eat food, go to bed!"));
    }
  }

  /**
   * Allows player to "get the bus" in effect switching map.
   *
   * @see GameScreen switchMap function
   */
  private void busStopEvent() {
    if (gameLogic.getCurrentMap().equals("town")) {
      gameLogic.switchMap("campus");
    } else {
      gameLogic.switchMap("town");
    }
    eventBus.publish(new DialogueUpdateState(DialogueUpdateState.State.HIDE_SELECT_BOX));
  }

  /**
   * Lets the player go to sleep, fades the screen to black then shows a dialogue about the amount
   * of sleep the player gets Then queues up fadeFromBlack to be called when this dialogue closes.
   *
   * @param args Unused currently
   * @see GameScreen fadeToBlack function
   */
  private void accomEvent(String[] args) {
    gameLogic.setSleeping(true);
    eventBus.publish(new DialogueUpdateState(DialogueUpdateState.State.HIDE));

    // Calculate the hours slept to the nearest hour
    // Wakes the player up at 8am
    float secondsSlept;
    if (gameLogic.getSeconds() < 60 * 8) {
      secondsSlept = (60 * 8 - gameLogic.getSeconds());
    } else {
      // Account for the wakeup time being in the next day
      secondsSlept = (((60 * 8) + 1440) - gameLogic.getSeconds());
    }
    int hoursSlept = Math.round(secondsSlept / 60f);

    RunnableAction setTextAction = new RunnableAction();
    setTextAction.setRunnable(
        new Runnable() {
          @Override
          public void run() {
            if (gameLogic.isSleeping()) {
              eventBus.publish(new DialogueUpdateState(DialogueUpdateState.State.SHOW));
              eventBus.publish(
                  new DialogueSetText(
                      String.format(
                          "You slept for %d hours!\nYou recovered %d energy!",
                          hoursSlept, Math.min(100, hoursSlept * 13)),
                      "fadefromblack"));
              // Restore energy and pass time
              gameLogic.setEnergy(hoursSlept * 13);
              gameLogic.passTime(secondsSlept);
              gameLogic.addSleptHours(hoursSlept);
              // Check for any streaks/achievements
              if (studied) {
                gameLogic.addStudyStreakCounter(1);
                gameLogic.getGame().studyStreak.checkCondition(gameLogic.getStudyStreakCounter());
                studied = false;
              } else {
                gameLogic.setStudyStreakCounter(0);
              }

              if (studiedTwice) {
                gameLogic.setBookWormCounter(1);
                gameLogic.getGame().bookWorm.checkCondition(gameLogic.getBookWormCounter());
                studiedTwice = false;
              }

              if (ate) {
                gameLogic.addEatStreakCounter(1);
                gameLogic.getGame().eatStreak.checkCondition(gameLogic.getEatStreakCounter());
                ate = false;
              } else {
                gameLogic.setEatStreakCounter(0);
              }

              if (hadFun) {
                gameLogic.addFunStreakCounter(1);
                gameLogic.getGame().funStreak.checkCondition(gameLogic.getFunStreakCounter());
                hadFun = false;
              } else {
                gameLogic.setFunStreakCounter(0);
              }

              if (noSleep) {
                gameLogic.setNoSleepCounter(1);
                gameLogic.getGame().allNighter.checkCondition(gameLogic.getNoSleepCounter());
                noSleep = false;
              }
            }
          }
        });

    fadeToBlack(setTextAction);
  }

  /**
   * Fades the screen to black.
   */
  private void fadeToBlack() {
    eventBus.publish(new FadeBlackScreenEvent(Actions.fadeIn(3f)));
  }

  /**
   * Fades the screen to black, then runs a runnable after it is done.
   *
   * @param runnable A runnable to execute after fading is finished
   */
  private void fadeToBlack(RunnableAction runnable) {
    eventBus.publish(new FadeBlackScreenEvent(Actions.fadeIn(3f), runnable));
  }

  /**
   * Fades the screen back in from black, displays a good morning message if the player was
   * sleeping.
   */
  private void fadeFromBlack() {
    // If the player is sleeping, queue up a message to be sent
    if (gameLogic.isSleeping()) {
      RunnableAction setTextAction = new RunnableAction();
      setTextAction.setRunnable(
          new Runnable() {
            @Override
            public void run() {
              if (gameLogic.isSleeping()) {
                eventBus.publish(new DialogueUpdateState(DialogueUpdateState.State.SHOW));
                // Show a text displaying how many days they have left in the game
                eventBus.publish(new DialogueSetText(gameLogic.getWakeUpMessage()));
                gameLogic.setSleeping(false);
              }
            }
          });

      // Queue up events
      eventBus.publish(new FadeBlackScreenEvent(Actions.fadeOut(3f), setTextAction));
    } else {
      eventBus.publish(new FadeBlackScreenEvent(Actions.fadeOut(3f)));
    }
  }
}
