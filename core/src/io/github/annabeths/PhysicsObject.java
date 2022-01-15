package io.github.annabeths;

import com.badlogic.gdx.math.Polygon;

public interface PhysicsObject extends GameObject {
    //in addition to the GameObject, PhysicsObjects must also implement the following

    Polygon collisionPolygon = null;

    boolean CollidesWith(PhysicsObject other);
    void OnCollision(PhysicsObject other);
}
