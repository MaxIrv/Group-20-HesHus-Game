package com.skloch.game.tests.components;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.skloch.game.DialogueBox;
import com.skloch.game.DialogueBox.*;
import com.skloch.game.tests.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for the DialogueBox class.
 */
@RunWith(GdxTestRunner.class)
public class DialogueBoxTests {
    DialogueBox dialogueBox;
    @Mock
    Skin skin = Mockito.mock(Skin.class);
    Window.WindowStyle windowStyle = Mockito.mock(Window.WindowStyle.class);

    @Before
    public void setup(){
        windowStyle.titleFont = mock(BitmapFont.class);

        when(skin.get(Window.WindowStyle.class)).thenReturn(windowStyle);

        dialogueBox = new DialogueBox(skin);
    }

    @Test
    public void testDialogueBoxConstructor(){

        Assert.assertEquals(35, dialogueBox.getMaxChars());
        Assert.assertSame(Window.class, dialogueBox.getDialogueWindow().getClass());
        Assert.assertSame(skin, dialogueBox.getDialogueWindow().getSkin());

        Assert.assertEquals(800, dialogueBox.getDialogueWindow().getWidth(), 0);
        Assert.assertEquals(200, dialogueBox.getDialogueWindow().getHeight(), 0);
    }

}
