package io.github.annabeths.Boats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.GameGenerics.IHealth;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Projectiles.Projectile;
import io.github.annabeths.Projectiles.ProjectileData;

public abstract class Boat extends PhysicsObject implements IHealth {
	GameController controller;

	// Boat stats
	public float HP;
	public float maxHP;
	protected float speed;
	protected float turnSpeed;

	protected float shotDelay = 0.5f;
	protected float timeSinceLastShot = 0f;

	@Deprecated
	public Boat() {
		sprite = new Sprite(new Texture(Gdx.files.internal("img/boat1.png")));
		sprite.setSize(100, 50);
		sprite.setOrigin(50, 25);
		sprite.setCenter(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

		collisionPolygon = new Polygon(new float[] { 0, 0, 0, 68, 25, 100, 50, 68, 50, 0 });

		position = new Vector2();
	}

	public Boat(GameController controller, Vector2 position, String texLoc) {
		this.controller = controller;
		this.position = position.cpy();

		collisionPolygon = new Polygon(new float[] { 0, 0, 0, 68, 25, 100, 50, 68, 50, 0 });

		setSprite(texLoc, position, new Vector2(100, 50));

		collisionPolygon.setPosition(position.x + getLocalCenterX() / 2,
				position.y - getLocalCenterY() / 2 - 10);
		collisionPolygon.setOrigin(25, 50);
		collisionPolygon.setRotation(rotation - 90);
	}

	public abstract void Update(float delta);

	/**
	 * Generic move method for boats to move forward by their set speed, and a
	 * multiplier
	 * 
	 * @param delta time since last frame
	 * @param multiplier multiplier to set forward or reverse motion (1 or -1)
	 */
	void Move(float delta, int multiplier) {
		// Convention: 0 degrees means the object is pointing right, positive angles are
		// counter clockwise
		Vector2 oldPos = position.cpy();
		position.x += Math.cos(Math.toRadians(rotation)) * speed * delta * multiplier;
		position.y += Math.sin(Math.toRadians(rotation)) * speed * delta * multiplier;

		sprite.setPosition(position.x, position.y);
		collisionPolygon.setPosition(position.x + getLocalCenterX() / 2,
				position.y - getLocalCenterY() / 2 - 10);
		collisionPolygon.setOrigin(25, 50);

		if (!controller.map.isPointInBounds(getCenter())) {
			position = oldPos.cpy();
		}
	}

	/**
	 * Turns the boat by its turn speed, in the direction specified by multiplier.
	 * 
	 * Turn the boat, a positive multiplier will turn it anti-clockwise, negative
	 * clockwise
	 * 
	 * @param delta time since last frame
	 * @param multiplier turn anti-clockwise if +ve, clockwise if -ve
	 */
	void Turn(float delta, float multiplier) {
		rotation = (rotation + turnSpeed * delta * multiplier) % 360;
		sprite.setRotation(rotation);
		collisionPolygon.setRotation(rotation - 90);
	}

	/**
	 * Turns the boat towards a desired angle using the shortest angular distance.
	 * Moves the boat forwards at the same time.
	 * 
	 * @param desiredAngle the angle the boat should end up at
	 * @param delta the time since the last update
	 * @author James Burnell
	 * @author Hector Woods
	 */
	public void moveTowardsDesiredAngle(float desiredAngle, float delta) {

		// Manipulate angle to compensate for [0-360] limitations
		if (rotation <= 90 && desiredAngle >= 270) desiredAngle -= 360;
		if (rotation >= 270 && desiredAngle <= 90) desiredAngle += 360;
		if (rotation > 180 && desiredAngle < 90) desiredAngle += 360;

		if (Math.abs(rotation - desiredAngle) > 0.5f) {
			Turn(delta, rotation < desiredAngle ? 1 : -1);
		}

		Move(delta, 1);
	}

	abstract void Shoot();

	abstract void Destroy();

	/**
	 * Place the boat somewhere in global space, use this when spawning boats
	 * 
	 * @param x the x position
	 * @param y the y position
	 */
	void SetPosition(float x, float y) {
		position.x = x;
		position.y = y;
		sprite.setPosition(x, y);
	}

	@Override
	public void Draw(SpriteBatch batch) {
		sprite.draw(batch);
	}

	/**
	 * Creates a new projectile fired from the boat.
	 * 
	 * @param type the type of projectile to shoot
	 * @param rotationOffset the angle to fire the projectile relative to the boat
	 * @param dmgMul the damage multiplier
	 * @param spdMul the speed multiplier
	 * 
	 * @return A new projectile object
	 */
	protected Projectile createProjectile(ProjectileData type, float rotationOffset, float dmgMul,
			float spdMul) {
		boolean isPlayer = this instanceof PlayerBoat;
		return new Projectile(getCenter(), rotation + rotationOffset, type, isPlayer, dmgMul,
				spdMul);
	}

	@Override
	public float getHealth() {
		return HP;
	}

	@Override
	public float getMaxHealth() {
		return maxHP;
	}

	@Override
	public void damage(float dmg) {
		HP = MathUtils.clamp(HP - dmg, 0, maxHP);
	}

	@Override
	public boolean isDead() {
		return killOnNextTick || IHealth.super.isDead();
	}
}
