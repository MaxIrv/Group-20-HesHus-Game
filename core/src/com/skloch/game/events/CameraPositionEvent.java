package com.skloch.game.events;

import com.badlogic.gdx.math.Vector3;

/**
 * The CameraPositionEvent class represents an event that is triggered when the camera position is updated in the game.
 * This event carries the updated camera position.
 */
public class CameraPositionEvent {
    // The updated camera position
    private final Vector3 position;

    /**
     * Constructs a new CameraPositionEvent with the specified updated camera position.
     *
     * @param position The updated camera position.
     */
    public CameraPositionEvent(Vector3 position) {
        this.position = position;
    }

    /**
     * Returns the updated camera position.
     *
     * @return The updated camera position.
     */
    public Vector3 getPosition() {
        return position;
    }
}