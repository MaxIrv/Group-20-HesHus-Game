package com.skloch.game.tests;

import com.badlogic.gdx.Gdx;
import com.skloch.game.HustleGame;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class AssetTests {
    @Test
    public void testShipAssetExists() {
        assertTrue("The asset for TMX map exists",
                Gdx.files.internal(HustleGame.mapAsset).exists());
    }
}
