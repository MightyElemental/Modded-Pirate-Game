package io.github.annabeths.Obstacles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Projectiles.Projectile;
import io.github.annabeths.Projectiles.ProjectileData;

/**
 * An abstract class that represents obstacles, e.g the Kraken, mines or weather.
 * @since Assessment 2
 * @author Hector Woods
 */
public abstract class ObstacleEntity extends PhysicsObject {

	GameController controller;
	String texturePath;

	/**
	 * Constructor for ObstacleEntity
	 * @param controller GameController
	 * @param position position of the ObstacleEntity
	 * @param texLoc location of the ObstacleEntity's texture
	 * @param size size of the ObstacleEntity
	 */
	public ObstacleEntity(GameController controller, Vector2 position, String texLoc,
			Vector2 size) {
		this.controller = controller;
		this.position = position.cpy();
		texturePath = texLoc;
		setSprite(texLoc, position, size);
	}

	/**
	 * Called once per frame. update the state of the ObstacleEntity
	 * @param delta time since last frame
	 */
	public abstract void Update(float delta);

	/**
	 * Draw the entity's sprite.
	 * @param batch Spritebatch to draw the GameObject
	 */
	public void Draw(SpriteBatch batch) {
		sprite.draw(batch);
	}

	/**
	 * Creates a new projectile fired from the entity.
	 *
	 * @param type the type of projectile to shoot
	 * @param rotationOffset the angle to fire the projectile relative to the boat
	 * @param dmgMul the damage multiplier
	 * @param spdMul the speed multiplier
	 * @param pos the position to spawn the projectile
	 *
	 * @return A new projectile object
	 */
	protected Projectile createProjectile(ProjectileData type, float rotationOffset, float dmgMul,
			float spdMul, Vector2 pos) {
		return new Projectile(pos, rotation + rotationOffset, type, false, dmgMul, spdMul);
	}

}
