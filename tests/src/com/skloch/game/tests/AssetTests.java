package com.skloch.game.tests;

import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Gdx;
import com.skloch.game.GameMap;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class AssetTests {
    @Test
    public void testShipAssetExists() {
        for (String mapName : GameMap.mapPaths.values()) {
            assertTrue("The asset for TMX map exists",
                    Gdx.files.internal(mapName).exists());
        }
    }
}
