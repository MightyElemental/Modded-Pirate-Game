package io.github.annabeths;

import com.badlogic.gdx.math.Polygon;

public interface PhysicsObject extends GameObject {

    Polygon collisionPolygon = null;

    boolean CollidesWith(PhysicsObject other);
    void OnCollision(PhysicsObject other);
}
