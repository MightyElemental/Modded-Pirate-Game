package io.github.annabeths;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.math.Intersector;

public abstract class PhysicsObject extends GameObject {
    //in addition to the GameObject, PhysicsObjects must also implement the following

    Polygon collisionPolygon = null;

    public boolean CheckCollisionWith(PhysicsObject other)
    {
        return Intersector.intersectPolygons(collisionPolygon, other.collisionPolygon, null) ||
        Intersector.intersectPolygons(other.collisionPolygon, collisionPolygon, null);
    }
    public abstract void OnCollision(PhysicsObject other);
}