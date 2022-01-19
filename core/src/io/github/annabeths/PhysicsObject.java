package io.github.annabeths;

import com.badlogic.gdx.math.Polygon;

public abstract class PhysicsObject implements GameObject {
    //in addition to the GameObject, PhysicsObjects must also implement the following

    Polygon collisionPolygon = null;

    boolean CollidesWith(PhysicsObject other)
    {
        return false;
    }
    void OnCollision(PhysicsObject other)
    {
        
    }
}
