package io.github.annabeths.GameGenerics;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
	// every GameObject needs to have these values, and define these methods

	public Vector2 position;

	public float rotation = 0;
	public Sprite sprite = null;
	public boolean killOnNextTick = false;

	/**
	 * Update Method, define on inheritance
	 * 
	 * @param delta time since last frame
	 */
	public void Update(float delta) {
	}

	public void Draw(SpriteBatch batch) {
	}

	/**
	 * Get the center point of the object
	 * 
	 * @return The center point
	 * @author James Burnell
	 */
	public Vector2 getCenter() {
		return new Vector2(getCenterX(), getCenterY());
	}

	/** @return The center x position */
	public float getCenterX() {
		return getLocalCenterX() + position.x;
	}

	/** @return The center y position */
	public float getCenterY() {
		return getLocalCenterY() + position.y;
	}

	/**
	 * Get the center x local to the object
	 * 
	 * @return The local center x, or {@code NaN} if {@link #sprite} is {@code null}
	 */
	public float getLocalCenterX() {
		return sprite == null ? Float.NaN : sprite.getWidth() / 2;
	}

	/**
	 * Get the center y local to the object
	 * 
	 * @return The local center y, or {@code NaN} if {@link #sprite} is {@code null}
	 */
	public float getLocalCenterY() {
		return sprite == null ? Float.NaN : sprite.getHeight() / 2;
	}
}
