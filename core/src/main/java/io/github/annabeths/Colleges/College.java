package io.github.annabeths.Colleges;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.Boat;
import io.github.annabeths.GameGenerics.PhysicsObject;

public abstract class College extends PhysicsObject {

	public int maxHP;
	public int range;
	public int HP;
	public int damage;

	public float fireRate;

	public boolean invulnerable;

	public Sprite aliveSprite;
	public Sprite deadSprite;
	public Sprite islandSprite;

	public College(Vector2 pos, Texture aliveTexture, Texture islandTexture) {
		position = pos;

		aliveSprite = new Sprite(aliveTexture);
		aliveSprite.setPosition(position.x, position.y);
		aliveSprite.setSize(100, 100);

		islandSprite = new Sprite(islandTexture);
		islandSprite.setCenter(aliveSprite.getX() + 5, aliveSprite.getY() + 5);
		islandSprite.setSize(120, 120);
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
	 */
	public boolean isInRange(Boat other) {
		return range >= Math.sqrt(Math
				.pow((aliveSprite.getX() + aliveSprite.getWidth() / 2)
						- (other.position.x + other.GetCenterX()), 2)
				+ Math.pow((aliveSprite.getY() + aliveSprite.getHeight() / 2)
						- (other.position.y + other.GetCenterY()), 2));
	}
}
