package com.skloch.game.interfaces;

/** Interface for the sound manager. */
public interface SoundManagerInterface {
  void dispose();

  void setMusicVolume(float volume);

  void setSfxVolume(float volume);

  void playPauseSound();

  void playDialogueOpen();

  void playDialogueOption();

  void playButton();

  void playOverworldMusic();

  void stopOverworldMusic();

  void playMenuMusic();

  void stopMenuMusic();

  void pauseOverworldMusic();

  float getMusicVolume();

  float getSfxVolume();

  void processTimers(float delta);

  void playFootstep();

  void stopFootstep();
}
