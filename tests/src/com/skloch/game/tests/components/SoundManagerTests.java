package com.skloch.game.tests.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.skloch.game.SoundManager;
import com.skloch.game.tests.GdxTestRunner;
import com.skloch.game.mocks.MockedClasses;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(GdxTestRunner.class)
public class SoundManagerTests {
    private SoundManager soundManager;

    @Before
    public void setUp() {
        soundManager = new SoundManager();
    }

    @After
    public void tearDown() {
        soundManager.dispose();
    }

    @Test
    public void testSetMusicVolume() {
        soundManager.setMusicVolume(0.5f);
        assertEquals(0.5f, soundManager.getMusicVolume());
        verify(soundManager.overworldMusic).setVolume(0.8f);
        verify(soundManager.menuMusic).setVolume(0.8f);
    }

    @Test
    public void testSetSfxVolume() {
        soundManager.setSfxVolume(0.8f);
        assertEquals(0.5f, soundManager.getSfxVolume(), 0.01);
        soundManager.setSfxVolume(0.8f);
    }

    @Test
    public void testPlayPauseSound() {
        soundManager.playPauseSound();
        verify(soundManager.getPauseSound()).play(0.8f);
    }

    @Test
    public void testPlayDialogueOpen() {
        soundManager.playDialogueOpen();
        verify(soundManager.getDialogueOpenSound()).play(0.8f);
    }

    @Test
    public void testPlayDialogueOption() {
        soundManager.playDialogueOption();
        verify(soundManager.getDialogueOptionSound()).play(0.8f);
    }

    @Test
    public void testPlayButton() {
        soundManager.playButton();
        verify(soundManager.getButtonSound()).play(0.8f);
    }

    @Test
    public void testPlayOverworldMusic() {
        soundManager.playOverworldMusic();
        verify(soundManager.overworldMusic).play();
    }

    @Test
    public void testStopOverworldMusic() {
        soundManager.stopOverworldMusic();
        verify(soundManager.overworldMusic).stop();
    }

    @Test
    public void testPlayMenuMusic() {
        soundManager.playMenuMusic();
        verify(soundManager.menuMusic).play();
    }

    @Test
    public void testStopMenuMusic() {
        soundManager.stopMenuMusic();
        verify(soundManager.menuMusic).stop();
    }

    @Test
    public void testPauseOverworldMusic() {
        soundManager.pauseOverworldMusic();
        verify(soundManager.overworldMusic).pause();
    }

    @Test
    public void testProcessTimers() {
        soundManager.processTimers(1.0f);
        assertEquals(0.0f, soundManager.getFootstepTimer(), 0.01);
    }

    @Test
    public void testPlayFootstep() {
        soundManager.setFootstepTimer(0);
        soundManager.footstepBool = false;
        soundManager.playFootstep();
        verify(soundManager.getFootstep1()).play(0.8f);
        assertTrue(soundManager.footstepBool);

        soundManager.playFootstep();
        verify(soundManager.getFootstep2()).play(0.8f);
        assertFalse(soundManager.footstepBool);
    }

    @Test
    public void testStopFootstep() {
        soundManager.stopFootstep();
        assertFalse(soundManager.footstepBool);
    }
}