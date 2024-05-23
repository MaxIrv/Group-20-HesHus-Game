package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.skloch.game.events.CameraPositionEvent;
import com.skloch.game.events.EventBus;
import com.skloch.game.events.MapSwitchEvent;
import com.skloch.game.interfaces.GameRendererInterface;
import com.skloch.game.interfaces.PlayerInterface;

/**
 * A class to render the game, including the player, map and UI elements. This class is called by
 * the GameScreen class to render the game.
 */
public class GameRenderer implements GameRendererInterface {
  private final HustleGame game;
  private OrthogonalTiledMapRenderer mapRenderer;
  private final OrthographicCamera camera;
  private final Viewport viewport;

  /**
   * A class to render the game, including the player, map and UI elements. This class is called by
   * the GameScreen class to render the game.
   *
   * @param game The game object
   * @param eventBus The event bus
   */
  public GameRenderer(HustleGame game, EventBus eventBus, GameMap gameMap) {
    this.game = game;

    // Camera and viewport settings
    camera = new OrthographicCamera();
    viewport = new FitViewport(game.width, game.height, camera);
    camera.setToOrtho(false, game.width, game.height);
    game.shapeRenderer.setProjectionMatrix(camera.combined);

    // Setup map
    float unitScale = gameMap.mapScale / gameMap.mapSquareSize;
    mapRenderer = new OrthogonalTiledMapRenderer(gameMap.map, unitScale);

    // Subscribe to camera position events
    eventBus.subscribe(CameraPositionEvent.class, this::onCameraPosition);
    eventBus.subscribe(MapSwitchEvent.class, this::onGameMapSwitch);
  }

  /** Initial render of the game, including the player, map and UI elements. */
  @Override
  public void initial_render() {
    game.shapeRenderer.setProjectionMatrix(camera.combined);
  }

  /**
   * Render the game, including the player and map.
   *
   * @param delta The time between the last frame and this frame
   */
  @Override
  public void render(float delta, PlayerInterface player, GameMap gameMap) {
    Gdx.app.log("GameRenderer", "Rendering game map " + gameMap.currentMap);
    // Clear screen
    ScreenUtils.clear(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    viewport.apply(); // Update the viewport

    // Update the map's render position
    mapRenderer.setView(camera);
    // Draw the background layer
    mapRenderer.render(gameMap.backgroundLayers);

    // Begin the spritebatch to draw the player on the screen
    game.batch.setProjectionMatrix(camera.combined);
    game.batch.begin();

    // Player, draw and scale
    game.batch.draw(
        player.getCurrentFrame(),
        player.getSpriteX(),
        player.getSpriteY(),
        0,
        0,
        player.getSpriteWidth(),
        player.getSpriteHeight(),
        1f,
        1f,
        1);

    game.batch.end();

    // Render map foreground layers
    mapRenderer.render(gameMap.foregroundLayers);

    // Focus the camera in the center of the player
    // Make it slide into place too
    // Change to camera.positon.set() to remove cool sliding
    camera.position.slerp(new Vector3(player.getCentreX(), player.getCentreY(), 0), delta * 9);

    camera.update();
  }

  public float getWorldWidth() {
    return viewport.getWorldWidth();
  }

  public float getWorldHeight() {
    return viewport.getWorldHeight();
  }

  public void resize_viewport(int width, int height) {
    viewport.update(width, height);
  }

  private void onCameraPosition(CameraPositionEvent event) {
    camera.position.set(event.getPosition());
    camera.update();
  }

  private void onGameMapSwitch(MapSwitchEvent event) {
    OrthogonalTiledMapRenderer oldMapRenderer = mapRenderer;
    float unitScale = event.getNewGameMap().mapScale / event.getNewGameMap().mapSquareSize;
    mapRenderer = new OrthogonalTiledMapRenderer(event.getNewGameMap().map, unitScale);
    oldMapRenderer.dispose();
  }

  /** Dispose of the map renderer. */
  @Override
  public void dispose() {
    mapRenderer.dispose();
  }
}
