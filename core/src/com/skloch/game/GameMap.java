package com.skloch.game;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Represents the game map.
 */
public class GameMap {
  public TiledMap map;
  public String currentMapPath;
  public int[] backgroundLayers;
  public int[] foregroundLayers;
  public int[] objectLayers;
  public int mapSquareSize;
  public float mapScale = 70f;
  public static final String CAMPUS_MAP_PATH = "East Campus/east_campus.tmx";
  public static final String TOWN_MAP_PATH = "Town/town.tmx";
  public MapProperties mapProperties;

  /**
   * Constructor for a GameMap.
   */
  public GameMap() {
    this.currentMapPath = CAMPUS_MAP_PATH;
    this.map = new TmxMapLoader().load(currentMapPath);
    this.mapProperties = map.getProperties();
    backgroundLayers = new int[] {0, 1, 2, 3, 4, 5, 6};
    foregroundLayers = new int[] {7};
    objectLayers = new int[] {8};
    mapSquareSize = mapProperties.get("tilewidth", Integer.class);
  }

  /**
   * Switch the map to the given map name.
   *
   * @param mapName the name of the map to switch to
   */
  public void switchMap(String mapName) {
    if (mapName.equals("town")) {
      currentMapPath = TOWN_MAP_PATH;
    } else {
      currentMapPath = CAMPUS_MAP_PATH;
    }
    TiledMap oldMap = map;
    this.map = new TmxMapLoader().load(currentMapPath);
    oldMap.dispose();
    this.mapProperties = map.getProperties();
    this.mapSquareSize = mapProperties.get("tilewidth", Integer.class);
  }

  public MapLayers getLayers() {
    return map.getLayers();
  }

  public void dispose() {
    map.dispose();
  }

}
