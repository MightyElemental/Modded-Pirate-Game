package io.github.annabeths.Projectiles;

import com.badlogic.gdx.math.Vector2;

/** Used to define each type of projectile found in the game */
public enum ProjectileData {

	/** Rays damage entities directly without traveling */
	RAY(0, 20, new Vector2(0, 0), null),
	STOCK(250, 20, new Vector2(20, 20), "img/entity/cannonball.png"),
	BOSS(300, 20, new Vector2(20, 20), "img/entity/cannonball.png"),
	ENEMY(250, 20, new Vector2(20, 20), "img/entity/cannonball.png");

	/** The scalar speed of the object, not the velocity */
	public float speed;
	public float damage;
	public Vector2 size;
	public String texture;

	private ProjectileData(float speed, float damage, Vector2 size, String texture) {
		this.speed = speed;
		this.damage = damage;
		this.size = size;
		this.texture = texture;
	}

	/**
	 * @return the speed
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * @return the damage
	 */
	public float getDamage() {
		return damage;
	}

	/**
	 * @return the size
	 */
	public Vector2 getSize() {
		return size;
	}

	/**
	 * @return the width
	 */
	public float getWidth() {
		return size.x;
	}

	/**
	 * @return the height
	 */
	public float getHeight() {
		return size.y;
	}

	/**
	 * @return the texture
	 */
	public String getTexture() {
		return texture;
	}
}
