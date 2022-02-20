package io.github.annabeths.GameGenerics;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.FloatArray;

public abstract class PhysicsObject extends GameObject {
	/*
	 * In addition to the GameObject, PhysicsObjects must also implement the
	 * following
	 */

	public Polygon collisionPolygon = null;

	/**
	 * Test for collision with other {@link PhysicsObject}
	 * 
	 * @param other the physics object to check collision with
	 * @return {@code true} if the physics objects collide, {@code false} otherwise
	 */
	public boolean CheckCollisionWith(PhysicsObject other) {
		return Intersector.intersectPolygons(
				new FloatArray(collisionPolygon.getTransformedVertices()),
				new FloatArray(other.collisionPolygon.getTransformedVertices()));
	}

	/**
	 * Called when object collides with another {@link PhysicsObject}
	 * 
	 * @param other the object collided with
	 */
	public abstract void OnCollision(PhysicsObject other);
}