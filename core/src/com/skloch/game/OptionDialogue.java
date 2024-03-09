package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Align;

public class OptionDialogue {
    private HustleGame game;
    private Window window;
    public Label questionLabel;
    private Label leftArrow;
    private Label rightArrow;
    boolean visible = false;
    boolean choice = false;
    public OptionDialogue (String question, int width, HustleGame game) {
        // Create an option dialogue to prompt the player for an input
        // Attempted to make it as adaptable as possible
        // Width can be changed and text wraps
        // It will however go wrong up to a certain point

        this.game = game;
        window = new Window("", game.skin);
        window.setModal(true);

        Table dialogueTable = new Table();

        // dialogueTable.setDebug(true);
        // window.setDebug(true);

        questionLabel = new Label("Interact", game.skin, "interaction");
        questionLabel.setWrap(true);
        leftArrow = new Label(">", game.skin, "interaction");
        rightArrow = new Label(">", game.skin, "interaction");
        Label option1Label = new Label("Yes", game.skin, "interaction");
        Label option2Label = new Label("No", game.skin, "interaction");

        questionLabel.setAlignment(Align.center);

        dialogueTable.add(questionLabel).top().colspan(4).fillX();
        dialogueTable.row().pad(0, 0, 0, 0);
        dialogueTable.add(leftArrow).right().padLeft(width-290);
        dialogueTable.add(option1Label).left().padLeft(10).padRight(30);
        dialogueTable.add(rightArrow).right();
        dialogueTable.add(option2Label).left().padLeft(10).padRight(width-290);

        window.add(dialogueTable).fillX().left();

        window.pack();

        window.setX(((float) Gdx.graphics.getWidth() / 2) - (window.getWidth() / 2));
        window.setY(((float) Gdx.graphics.getHeight() / 2) - (window.getHeight() / 2) - 150);

        this.updateArrow();
        this.setVisible(false);

        this.setWidth(width);
    }

    public Window getWindow () {
        return this.window;
    }

    public void setQuestionText (String text) {
        questionLabel.setText(text);
    }

    public void setWidth (int x) {
        window.setWidth(x);
        window.setX(((float) Gdx.graphics.getWidth() / 2) - (window.getWidth() / 2));
        window.setY(((float) Gdx.graphics.getHeight() / 2) - (window.getHeight() / 2) - 150);
    }

    public void setVisible (boolean visible) {
        this.visible = visible;
        window.setVisible(visible);
    }

    public boolean isVisible () {
        return visible;
    }

    public boolean select() {
        // Returns the result of the choice of the player
        return choice;
    }

    public boolean getChoice() {
        return choice;
    }


    public void act(int keycode) {
        // Reacts to keypress to change which option is selected
        if (choice == true && (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT)) {
            choice = false;
        } else if (choice == false && (keycode == Input.Keys.A || keycode == Input.Keys.LEFT)) {
            choice = true;
        }

        this.updateArrow();
    }

    private void updateArrow () {
        // Updates which arrow is pointed at
        if (choice == true) {
            rightArrow.setVisible(false);
            leftArrow.setVisible(true);
        } else {
            rightArrow.setVisible(true);
            leftArrow.setVisible(false);
        }
    }

    public void setChoice (Boolean choice) {
        this.choice = choice;
        updateArrow();
    }


}