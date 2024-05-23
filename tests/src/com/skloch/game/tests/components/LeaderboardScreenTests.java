package com.skloch.game.tests.components;

import com.skloch.game.HustleGame;
import com.skloch.game.LeaderboardScreen;
import com.skloch.game.interfaces.LeaderboardScreenInterface;
import com.skloch.game.mocks.MockedClasses;
import com.skloch.game.tests.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class LeaderboardScreenTests {
    HustleGame game;
    LeaderboardScreen leaderboardScreen;

    @Before
    public void setup(){
        HustleGame game= MockedClasses.mockHustleGame();
        LeaderboardScreen leaderboardScreen = MockedClasses.mockLeaderboardScreen();
    }
    @Test
    public void testInitialScore(){
        float score = leaderboardScreen.getPlayerScore();
        assertEquals("Initial score should be 70%.",score,0.7f,0.01);


    }

}
