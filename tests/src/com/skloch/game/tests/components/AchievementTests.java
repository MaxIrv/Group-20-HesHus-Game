package com.skloch.game.tests.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.skloch.game.Achievement;
import java.util.function.Predicate;
import org.junit.Test;

public class AchievementTests {
  @Test
  public void testAchievementInitialization() {
    Predicate<Integer> condition = i -> i > 4;
    Achievement achievement =
        new Achievement("Serial Studier", "Study 5 days in a row.", condition);

    assertEquals("Title should be 'Serial Studier'", "Serial Studier", achievement.getTitle());
    assertEquals("Description should be 'Study 5 days in a row.'", "Study 5 days in a row.",
        achievement.getDescription());
    assertFalse("Achieved should be false initially", achievement.getAchieved());
  }

  @Test
  public void testCheckConditionAchieved() {
    Predicate<Integer> condition = i -> i > 4;
    Achievement achievement =
        new Achievement("Serial Studier", "Study 5 days in a row.", condition);

    achievement.checkCondition(5);
    assertTrue("Achievement should be achieved when condition is met", achievement.getAchieved());
  }

  @Test
  public void testCheckConditionNotAchieved() {
    Predicate<Integer> condition = i -> i > 4;
    Achievement achievement =
        new Achievement("Serial Studier", "Study 5 days in a row.", condition);

    achievement.checkCondition(3);
    assertFalse("Achievement should not be achieved when condition is not met",
        achievement.getAchieved());
  }

  @Test
  public void testSettersAndGetters() {
    Predicate<Integer> condition = i -> i > 4;
    Achievement achievement =
        new Achievement("Serial Studier", "Study 5 days in a row.", condition);

    achievement.setTitle("New Title");
    assertEquals("Title should be 'New Title'", "New Title", achievement.getTitle());

    achievement.setDescription("New Description");
    assertEquals("Description should be 'New Description'", "New Description",
        achievement.getDescription());

    Predicate<Integer> newCondition = i -> i == 1;
    achievement.setConditions(newCondition);
    assertEquals("Conditions should be updated to the new condition", newCondition,
        achievement.getConditions());

    achievement.setAchieved(true);
    assertTrue("Achieved should be true after setting it to true", achievement.getAchieved());
  }
}
