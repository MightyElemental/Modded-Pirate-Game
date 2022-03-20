package io.github.annabeths.GameGenerics;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.GeneralControl.ResourceManager;

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
	
	protected void setSprite(String texture, Vector2 position, Vector2 size) {
		sprite = new Sprite(ResourceManager.getTexture(texture));
		sprite.setSize(100, 50);
		sprite.setOrigin(50, 25);

		sprite.setPosition(position.x, position.y);
	}

	/**
	 * Set the position of the object, centered around the given point.
	 * 
	 * @param pos the center point to move the object to
	 * @author James Burnell
	 */
	public void setCenter(Vector2 pos) {
		position = pos.cpy().sub(getLocalCenterX(), getLocalCenterY());
		sprite.setCenter(pos.x, pos.y);
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
