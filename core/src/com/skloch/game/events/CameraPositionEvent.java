package com.skloch.game.events;

import com.badlogic.gdx.math.Vector3;

public class CameraPositionEvent {
    private final Vector3 position;

    public CameraPositionEvent(Vector3 position) {
        this.position = position;
    }

    public Vector3 getPosition() {
        return position;
    }
}
