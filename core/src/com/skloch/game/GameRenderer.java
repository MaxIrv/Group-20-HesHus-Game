package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameRenderer {
    private final HustleGame game;
    private final GameScreen gameScreen;
//    private final Stage uiStage;

    public GameRenderer(HustleGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;


    }

    public void initial_render() {
        game.shapeRenderer.setProjectionMatrix(gameScreen.camera.combined);

        // Display a little good morning message
        gameScreen.dialogueBox.show();
        gameScreen.dialogueBox.setText(gameScreen.gameLogic.getWakeUpMessage());
    }

    public void render(float delta) {
        // Clear screen
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameScreen.viewport.apply(); // Update the viewport

        // Update the map's render position
        gameScreen.mapRenderer.setView(gameScreen.camera);
        // Draw the background layer
        gameScreen.mapRenderer.render(game.backgroundLayers);

        // Begin the spritebatch to draw the player on the screen
        game.batch.setProjectionMatrix(gameScreen.camera.combined);
        game.batch.begin();

        // Player, draw and scale
        game.batch.draw(
                gameScreen.player.getCurrentFrame(),
                gameScreen.player.sprite.x, gameScreen.player.sprite.y,
                0, 0,
                gameScreen.player.sprite.width, gameScreen.player.sprite.height,
                1f, 1f, 1
        );

        game.batch.end();

        // Render map foreground layers
        gameScreen.mapRenderer.render(game.foregroundLayers);

        // Focus the camera on the center of the player
        // Make it slide into place too
        // Change to camera.positon.set() to remove cool sliding
        gameScreen.camera.position.slerp(
                new Vector3(
                        gameScreen.player.getCentreX(),
                        gameScreen.player.getCentreY(),
                        0
                ),
                delta*9
        );

        gameScreen.camera.update();
    }


}
