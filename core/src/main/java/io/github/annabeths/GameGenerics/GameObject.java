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

	/**
	 * Update Method, define on inheritance, required for colleges
	 * 
	 * @param delta time since last frame
	 * @param other the other object
	 */
	public void Update(float delta, PhysicsObject other) {
	}

	public void Draw(SpriteBatch batch) {
	}
}
