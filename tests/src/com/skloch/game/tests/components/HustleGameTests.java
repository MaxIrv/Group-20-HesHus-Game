package com.skloch.game.tests.components;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.skloch.game.HustleGame;
import com.skloch.game.SoundManager;
import com.skloch.game.mocks.MockedClasses;
import com.skloch.game.tests.GdxTestRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class HustleGameTests {
    private HustleGame game;
    private TmxMapLoader mapLoader;
    private TiledMap mockMap;
    private Skin mockSkin;
    private SpriteBatch mockBatch;
    private Stage mockStage;
    private SoundManager mockSoundManager;
    private MapProperties mockMapProperties;
    private FileHandle mockFileHandle;
    @Before
    public void setup() {
        // Mock dependencies
        mapLoader = mock(TmxMapLoader.class);
        mockMap = mock(TiledMap.class);
        mockSkin = mock(Skin.class);
        mockBatch = mock(SpriteBatch.class);
        mockStage = mock(Stage.class);
        mockSoundManager = mock(SoundManager.class);
        mockMapProperties = mock(MapProperties.class);
        mockFileHandle = mock(FileHandle.class);

        // Set up Gdx.files and Gdx.files.internal to return the mock FileHandle
        Gdx.files = mock(Files.class);
        when(Gdx.files.internal(anyString())).thenReturn(mockFileHandle);

        // Set up TmxMapLoader to return the mock TiledMap
        when(mapLoader.load(anyString())).thenReturn(mockMap);

        // Set up mock MapProperties
        when(mockMap.getProperties()).thenReturn(mockMapProperties);
        when(mockMapProperties.get("tilewidth", Integer.class)).thenReturn(32);

        // Create game instance
        game = new HustleGame(800, 600);

        // Inject mocks into game
        game.batch = mockBatch;
        game.skin = mockSkin;
        game.map = mockMap;
        game.mapProperties = mockMapProperties;
        game.soundManager = mockSoundManager;
        game.blueBackground = mockStage;
    }

    @Test
    public void testCreate() {
        game.create();

        assertNotNull(game.batch);
        assertNotNull(game.skin);
        assertNotNull(game.map);
        assertNotNull(game.shapeRenderer);
        assertNotNull(game.soundManager);
        assertNotNull(game.blueBackground);
        assertNotNull(game.credits);
        assertNotNull(game.tutorialText);
        assertNotNull(game.studyStreak);
        assertNotNull(game.bookWorm);
        assertNotNull(game.eatStreak);
        assertNotNull(game.funStreak);
        assertNotNull(game.allNighter);
    }

    @Test
    public void testSwitchMap() {
        String newMapPath = "East Campus/new_map.tmx";
        game.switch_map(newMapPath);

        verify(mapLoader).load(newMapPath);
        assertSame(mockMap, game.map);
        assertSame(mockMapProperties, game.mapProperties);
        verify(mockMap).dispose();
    }

    @Test
    public void testDispose() {
        game.dispose();

        verify(mockBatch).dispose();
        verify(mockStage).dispose();
        verify(mockSkin).dispose();
        verify(mockMap).dispose();
        verify(mockSoundManager).dispose();
    }

    @Test
    public void testReadTextFile() {
        String testFilePath = "Text/test_file.txt";
        String expectedContent = "This is a test file content.";

        when(mockFileHandle.exists()).thenReturn(true);
        when(mockFileHandle.readString()).thenReturn(expectedContent);

        String actualContent = game.readTextFile(testFilePath);
        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testReadTextFile_FileNotFound() {
        String testFilePath = "Text/non_existent_file.txt";

        when(mockFileHandle.exists()).thenReturn(false);

        String actualContent = game.readTextFile(testFilePath);
        assertTrue(actualContent.contains("Couldn't load " + testFilePath));
    }
}
