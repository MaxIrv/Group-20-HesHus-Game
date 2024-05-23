package com.skloch.game.tests;

import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Gdx;
import com.skloch.game.GameMap;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for assets. */
@RunWith(GdxTestRunner.class)
public class AssetTests {

  /** Test that the ship asset exists. */
  @Test
  public void testShipAssetExists() {
    assertTrue("Town map exists", Gdx.files.internal(GameMap.townMapPath).exists());
    assertTrue("Campus map exists", Gdx.files.internal(GameMap.campusMapPath).exists());
  }
}
