package com.skloch.game;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import java.util.Map;

/** Represents the game map. */
public class GameMap {
  public TiledMap map;
  public String currentMap = "campus";
  public int[] backgroundLayers;
  public int[] foregroundLayers;
  public int[] objectLayers;
  public int mapSquareSize;
  public float mapScale = 70f;
  public static final Map<String, String> mapPaths = Map.of(
      "campus", "East Campus/east_campus.tmx",
      "town", "Town/town.tmx"
  );
  public MapProperties mapProperties;
  
  /** Constructor for a GameMap. */
  public GameMap() {
    this.map = new TmxMapLoader().load(mapPaths.get(currentMap));
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
  public void switch_map(String mapName) {
    if (!mapPaths.containsKey(mapName)) {
      mapName = "campus";
    }
    currentMap = mapName;
    TiledMap oldMap = map;
    this.map = new TmxMapLoader().load(mapPaths.get(mapName));
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
