package io.github.annabeths.Collectables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.GameGenerics.PhysicsObject;

/**
 * Powerups that can be used to provide various buffs to the player.
 * @since Assessment 2
 * @author Ben Faulkner
 */
public class Powerup extends PhysicsObject {

	// id of powerup given
	private final PowerupType powerup;

	public Powerup(PowerupType powerup, Vector2 initialPosition) {

		setSprite(powerup.getTexture(), initialPosition, new Vector2(50, 50));
		setCenter(initialPosition);

		this.powerup = powerup;

		collisionPolygon = new Polygon(new float[] { 0, 25, 25, 50, 50, 25, 25, 0 });
		collisionPolygon.setOrigin(8, 8);
		collisionPolygon.setPosition(position.x, position.y);
	}

	/**
	 * Called when the powerup collides with a physics object.
	 * If it collides with a player, then they are awarded the powerup.
	 * @param other the object collided with
	 */
	@Override
	public void OnCollision(PhysicsObject other) {
		if (other instanceof PlayerBoat) {
			PlayerBoat boat = (PlayerBoat) other;

			// remove if player successfully received powerup
			if (boat.receivePower(powerup)) {
				Gdx.app.log("Powerup", "Collected powerup - " + powerup.getName());
				kill();
			}
		}
	}

	/**
	 * Called once per frame; draws the powerup
	 * @param batch a SpriteBatch that will draw the powerup.
	 */
	@Override
	public void Draw(SpriteBatch batch) {
		sprite.draw(batch);
	}

}
