package io.github.annabeths.Projectiles;

import com.badlogic.gdx.graphics.g2d.Sprite;
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
 */
public class Projectile extends PhysicsObject {

	private Vector2 velocity;
	public boolean isPlayerProjectile;
	public float damage;

	/** How far the projectile can travel before dying */
	public float lifeDist;
	/**
	 * lifeDist squared. Used to speed up distance calculations.
	 * 
	 * @see #lifeDist
	 */
	private float lifeDist2;
	private Vector2 startingPos;

	/**
	 * @param origin where it should start
	 * @param originRot the rotation of the projectile
	 * @param data the data to use
	 * @param isPlayerProjectile true if the projectile is shot by the player
	 * @see #Projectile(Vector2, float, ProjectileData, boolean, float, float)
	 */
	public Projectile(Vector2 origin, float originRot, ProjectileData data,
			boolean isPlayerProjectile) {
		this(origin, originRot, data, isPlayerProjectile, 1, 1);
	}

	/**
	 * @param origin where it should start
	 * @param originRot the rotation of the projectile
	 * @param data the data to use
	 * @param isPlayerProjectile true if the projectile is shot by the player
	 * @param damageMultiplier how much to multiply the damage by
	 * @param speedMultiplier how much to multiple the speed by
	 */
	public Projectile(Vector2 origin, float originRot, ProjectileData data,
			boolean isPlayerProjectile, float damageMultiplier, float speedMultiplier) {
		this.startingPos = origin.cpy();
		position = origin;
		damage = data.damage * damageMultiplier;
		this.isPlayerProjectile = isPlayerProjectile;

		// speed is only needed to initialize the velocity
		float speed = data.speed * speedMultiplier;

		// Calculate the projectile's velocity in the game space
		velocity = new Vector2(speed, 0).setAngleDeg(originRot);

		sprite = new Sprite(data.texture);
		sprite.setSize(data.size.x, data.size.y);
		sprite.setOrigin(data.size.x / 2, data.size.y / 2);
		sprite.setRotation(originRot);

		collisionPolygon = new Polygon(new float[] { data.size.x / 2, 0, data.size.x,
				data.size.y / 2, data.size.x / 2, data.size.y, 0, data.size.y / 2 });
		collisionPolygon.setOrigin(data.size.x / 2, data.size.y / 2);

		lifeDist = MathUtils.random(500 - 50, 500 + 50);
		lifeDist2 = lifeDist * lifeDist;
	}

	@Override
	public void Update(float delta) {
		position.x += velocity.x * delta;
		position.y += velocity.y * delta;
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
				other.killOnNextTick = true;
				killOnNextTick = true;
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
}
