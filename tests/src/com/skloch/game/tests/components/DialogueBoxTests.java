package com.skloch.game.tests.components;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.skloch.game.DialogueBox;
import com.skloch.game.tests.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(GdxTestRunner.class)
public class DialogueBoxTests {
    DialogueBox dialogueBox;
    @Mock
    Skin skin = Mockito.mock(Skin.class);

    @Before
    public void setup(){
        dialogueBox = new DialogueBox(skin);
    }

    @Test
    public void testDialogueBoxConstructor(){
        Assert.assertEquals(35, dialogueBox.getMaxChars());
        Assert.assertSame(Window.class, dialogueBox.getDialogueWindow());
        Assert.assertSame(skin, dialogueBox.getDialogueWindow().getSkin());

        Assert.assertEquals(800, dialogueBox.getDialogueWindow().getWidth());
        Assert.assertEquals(200, dialogueBox.getDialogueWindow().getHeight());

        Assert.assertSame(Window.class, dialogueBox.getSelectBox());
    }

}
