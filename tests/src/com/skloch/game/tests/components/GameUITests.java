package com.skloch.game.tests.components;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.skloch.game.*;
import com.skloch.game.events.*;
import com.skloch.game.events.DialogueBoxEvents.DialogueScrollEvent;
import com.skloch.game.events.DialogueBoxEvents.DialogueSetOptions;
import com.skloch.game.events.DialogueBoxEvents.DialogueSetText;
import com.skloch.game.events.DialogueBoxEvents.DialogueUpdateState;
import com.skloch.game.interfaces.IGameLogic;
import com.skloch.game.interfaces.GameScreenProvider;
import com.skloch.game.interfaces.IGameUI;
import com.skloch.game.mocks.MockedClasses;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GameUITests {
    HustleGame game;
    GameScreen gameScreen;
    IGameLogic gameLogic;
    EventBus eventBus;
    GameScreenProvider gameScreenProvider;
    DialogueBox dialogueBox;
    GameUI gameUI;

    @Before
    public void setup() {
        game = MockedClasses.mockHustleGame();
        dialogueBox = MockedClasses.mockDialogueBox();
        when(game.skin).thenReturn(mock(Skin.class));
        when(dialogueBox.getWindow()).thenReturn(mock(Window.class));
        gameUI = new GameUI(game, gameScreen, gameLogic, eventBus, gameScreenProvider);
    }

    @Test
    public void testCreateUI() {
        gameUI.create_ui(800, 600);
        assertNotNull(gameUI.getUIStage());
    }

    @Test
    public void testRenderUI() {
        gameUI.create_ui(800, 600);
        gameUI.render_ui(1.0f);
        verify(gameUI.getUIStage(), times(1)).act(1.0f);
        verify(gameUI.getUIStage(), times(1)).draw();
    }

    @Test
    public void testResizeUI() {
        gameUI.create_ui(800, 600);
        gameUI.resize_ui(1024, 768);
        verify(gameUI.getUIStage().getViewport(), times(1)).update(1024, 768, true);
    }
}