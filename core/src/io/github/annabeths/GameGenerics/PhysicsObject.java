package io.github.annabeths.GameGenerics;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.math.Intersector;

public abstract class PhysicsObject extends GameObject {
    //in addition to the GameObject, PhysicsObjects must also implement the following

    public Polygon collisionPolygon = null;

    public boolean CheckCollisionWith(PhysicsObject other)
    {
        return Intersector.intersectPolygons(new FloatArray(collisionPolygon.getTransformedVertices()), new FloatArray(other.collisionPolygon.getTransformedVertices()));
    }
    public abstract void OnCollision(PhysicsObject other);
}