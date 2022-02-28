package io.github.annabeths.Colleges;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;

public class PlayerCollege extends College {

	/**
	 * The amount to heal the player's boat
	 * 
	 * @see PlayerBoat#Heal(int, float)
	 */
	public int healAmount;

	public PlayerCollege(Vector2 position, Texture aliveTexture, Texture islandTexture,
			GameController gc) {
		super(position, aliveTexture, islandTexture, gc);
		healAmount = 15;
		range = 400;
		collisionPolygon = new Polygon(new float[] { 0, 0, 100, 0, 100, 100, 0, 100 });
		collisionPolygon.setPosition(position.x, position.y);
	}

	/**
	 * PlayerCollege doesn't need to handle any collisions itself. The case of the
	 * PlayerBoat crashing into it is handled in the PlayerBoat class.
	 * 
	 * @param other n/a
	 * @see PlayerBoat#OnCollision(PhysicsObject)
	 */
	@Override
	public void OnCollision(PhysicsObject other) {
	}

	@Override
	public void Update(float delta) {
		PlayerBoat boat = gc.playerBoat;
		// if the player boat is in range, heal it
		if (isInRange(boat)) {
			boat.Heal(healAmount, delta);
		}
	}

	@Override
	public void Draw(SpriteBatch batch) {
		islandSprite.draw(batch);
		sprite.draw(batch);
	}

}
