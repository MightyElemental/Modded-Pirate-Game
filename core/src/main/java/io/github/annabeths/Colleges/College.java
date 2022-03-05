package io.github.annabeths.Colleges;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.Boat;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;

public abstract class College extends PhysicsObject {

	public int maxHP;
	public int range;
	public int HP;
	public int damage;

	public float fireRate;

	public boolean invulnerable;

	public Sprite deadSprite;
	public Sprite islandSprite;

	/**
	 * Used to access player boat and to notify the game controller when the college
	 * has been destroyed
	 */
	protected GameController gc;

	public College(Vector2 pos, Texture aliveTexture, Texture islandTexture, GameController gc) {
		this.gc = gc;
		position = pos;

		sprite = new Sprite(aliveTexture);
		sprite.setPosition(position.x, position.y);
		sprite.setSize(100, 100);

		islandSprite = new Sprite(islandTexture);
		islandSprite.setCenter(sprite.getX() + 5, sprite.getY() + 5);
		islandSprite.setSize(120, 120);

		collisionPolygon = new Polygon(new float[] { 0, 0, 100, 0, 100, 100, 0, 100 });
		collisionPolygon.setPosition(position.x, position.y);
	}

	public College() {
	}

	/**
	 * Work out euclidean distance to the other physics object, and then returns
	 * true if the that distance is &lt;= the range of the college.<br>
	 * This will be used to check if the enemy college should attack the player.<br>
	 * This will be used to check if the friendly college should heal the player.
	 * 
	 * @param other the boat to check the range of
	 * @return {@code true} if the boat is in range of the college, {@code false}
	 *         otherwise
	 * @author James Burnell
	 */
	public boolean isInRange(Boat other) {
		return getCenter().dst(other.getCenter()) <= range;
	}
}
