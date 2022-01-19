package io.github.annabeths;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.math.Intersector;

public abstract class PhysicsObject implements GameObject {
    //in addition to the GameObject, PhysicsObjects must also implement the following

    Polygon collisionPolygon = null;

    boolean CollidesWith(PhysicsObject other)
    {
        return Intersector.intersectPolygons(new FloatArray(collisionPolygon.getTransformedVertices()),
                                             new FloatArray(other.collisionPolygon.getTransformedVertices()));
    }
    void OnCollision(PhysicsObject other)
    {

    }
}
