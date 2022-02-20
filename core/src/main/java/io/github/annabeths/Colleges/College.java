package io.github.annabeths.Colleges;

import com.badlogic.gdx.graphics.g2d.Sprite;

import io.github.annabeths.Boats.Boat;
import io.github.annabeths.GameGenerics.PhysicsObject;

import java.lang.Math;

public abstract class College extends PhysicsObject {
	int range;
	int HP;
	int damage;
	int fireRate;
	Sprite aliveSprite;
	Sprite deadSprite;
	Sprite islandSprite;

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
