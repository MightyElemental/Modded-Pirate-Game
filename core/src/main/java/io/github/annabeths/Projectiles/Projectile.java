package io.github.annabeths.Projectiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.GameGenerics.PhysicsObject;

/**
 * Projectiles are fired from other game objects. For example, cannonballs from
 * ships or colleges.
 * 
 * @author Leif Kemp
 * @author Annabeth Singleton
 * @author James Burnell
 * @tt.updated Assessment 2
 */
public class Projectile extends PhysicsObject {

	private final Vector2 velocity;
	private final boolean isPlayerProjectile;
	private boolean isFriendlyProjectile;
	private final float damage;

	/** How far the projectile can travel before dying */
	public float lifeDist;
	/**
	 * lifeDist squared. Used to speed up distance calculations.
	 * 
	 * @see #lifeDist
	 */
	private final float lifeDist2;
	private final Vector2 startingPos;

	/**
	 * @param origin where it should start
	 * @param originRot the angle the projectile is facing
	 * @param data the projectile data type to use
	 * @param isPlayerProjectile true if the projectile is shot by the player
	 * @see #Projectile(Vector2, float, ProjectileData, boolean, float, float)
	 */
	public Projectile(Vector2 origin, float originRot, ProjectileData data,
			boolean isPlayerProjectile) {
		this(origin, originRot, data, isPlayerProjectile, 1, 1);
	}

	public Projectile(Vector2 origin, float originRot, ProjectileData data, boolean isPlayerProjectile, boolean
			isFriendlyProjectile){
		this(origin, originRot, data, isPlayerProjectile, 1, 1);
		this.isFriendlyProjectile = isFriendlyProjectile;
	}

	public Projectile(Vector2 origin, float originRot, ProjectileData data, boolean isPlayerProjectile,
					  boolean isFriendlyProjectile, float damageMultiplier, float speedMultiplier){
		this(origin,originRot,data,isPlayerProjectile,damageMultiplier,speedMultiplier);
		this.isFriendlyProjectile = isFriendlyProjectile;

	}

	/**
	 * @param origin where it should start
	 * @param originRot the angle the projectile is facing
	 * @param data the projectile data type to use
	 * @param isPlayerProjectile true if the projectile is shot by the player
	 * @param damageMultiplier how much to multiply the damage by
	 * @param speedMultiplier how much to multiply the speed by
	 */
	public Projectile(Vector2 origin, float originRot, ProjectileData data,
			boolean isPlayerProjectile, float damageMultiplier, float speedMultiplier) {
		this.startingPos = origin.cpy();
		position = origin;
		damage = data.getDamage() * damageMultiplier;
		this.isPlayerProjectile = isPlayerProjectile;

		// speed is only needed to initialize the velocity
		float speed = data.getSpeed() * speedMultiplier;

		// Calculate the projectile's velocity in the game space
		velocity = new Vector2(speed, 0).setAngleDeg(originRot);
		this.rotation = originRot;

		setSprite(data.texture, position, data.size);
		sprite.setRotation(originRot);

		collisionPolygon = new Polygon(
				new float[] { data.getWidth() / 2, 0, data.getWidth(), data.getHeight() / 2,
						data.getWidth() / 2, data.getHeight(), 0, data.getHeight() / 2 });
		collisionPolygon.setOrigin(data.getWidth() / 2, data.getHeight() / 2);

		lifeDist = MathUtils.random(500 - 50, 500 + 50);
		lifeDist2 = lifeDist * lifeDist;
	}

	@Override
	public void Update(float delta) {
		position.mulAdd(velocity, delta);
		collisionPolygon.setPosition(position.x - getLocalCenterY(),
				position.y - getLocalCenterX());

		// test if projectile has passed the distance limit
		if (startingPos.dst2(position) > lifeDist2) {
			onTravelledMax();
		}
	}

	/** Ran when the projectile has traveled beyond the maximum distance */
	private void onTravelledMax() {
		killOnNextTick = true;
	}

	@Override
	public void Draw(SpriteBatch batch) {
		sprite.setCenter(position.x, position.y);
		sprite.draw(batch);
	}

	@Override
	public void OnCollision(PhysicsObject other) {
		if (other instanceof Projectile) {
			Projectile p = (Projectile) other;
			if (p.isPlayerProjectile != isPlayerProjectile) {
				p.kill();
				this.kill();
			}
		}
	}

	/**
	 * Get the speed of the projectile
	 * 
	 * @return the speed
	 */
	public float getSpeed() {
		return velocity.len();
	}

	public boolean isPlayerProjectile() {
		return isPlayerProjectile;
	}

	public boolean isFriendlyProjectile(){return isFriendlyProjectile;}

	public float getDamage() {
		return damage;
	}
}
