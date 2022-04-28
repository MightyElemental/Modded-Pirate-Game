package io.github.annabeths.Boats;

import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.GeneralControl.MathHelper;
import io.github.annabeths.Projectiles.Projectile;
import io.github.annabeths.Projectiles.ProjectileData;

/**
 * A form of {@link AIBoat} that can attack other ships
 * 
 * @since Assessment 2
 * @author James Burnell
 */
public abstract class AttackBoat extends AIBoat {

	/** How close it needs to be to the player before it attacks */
	public float attackRange = 400;
	/** How close it needs to be to the player before it starts approaching */
	public float approachRange = 650;
	/** The type of projectile to shoot */
	public ProjectileData projectileType = ProjectileData.STOCK;
	/**
	 * The Boat that is being currently targeted. null if there is no target.
	 */
	public Boat target;

	public AttackBoat(GameController controller, Vector2 initialPosition, String texLoc) {
		super(controller, initialPosition, texLoc);

		this.speed = 75;
		this.turnSpeed = 150;
	}

	@Override
	public void Shoot() {

		float damageMul = 1;
		if (this instanceof EnemyBoat) {
			damageMul = controller.getGameDifficulty().getEnemyDmgMul();
		}

		Projectile projLeft = createProjectile(projectileType, -90, damageMul, 1);
		Projectile projRight = createProjectile(projectileType, 90, damageMul, 1);

		// Add the projectile to the GameController's physics objects list so it
		// receives updates
		controller.NewPhysicsObject(projLeft);
		controller.NewPhysicsObject(projRight);
	}

	@Override
	void Destroy() {
		killOnNextTick = true;
	}

	public void updateAIState() {
		target = getNearestTarget();
		if (target == null) {
			state = AIState.IDLE;
		} else {
			float dstToTarget = target.getCenter().dst(getCenter());

			if (dstToTarget < attackRange) {
				state = AIState.ATTACK;
			} else if (dstToTarget < approachRange) {
				// move towards player if possible
				state = AIState.APPROACH;
			} else {
				state = AIState.IDLE;
			}
		}
	}

	@Override
	public void Update(float delta) {
		if (isDead()) Destroy();
		timeSinceLastShot += delta;

		updateAIState();

		switch (state) {
		case APPROACH:
			approach(delta);
			break;
		case ATTACK:
			attack(delta);
			break;
		case IDLE:
			idle(delta);
			break;
		default:
			break;
		}

		if (destination != null) MoveToDestination(delta);
	}

	public void approach(float delta) {
		if (isDestValid(target.getCenter())) {
			destination = target.getCenter();
		} else {
			idle(delta);
		}
	}

	public void attack(float delta) {
		float angToTarget = getCenter().sub(target.getCenter()).angleDeg();

		// Move at a 90 degree angle to the play to align the boat to shoot
		destination = null;
		float adjustedAng = angToTarget - rotation;
		adjustedAng = MathHelper.normalizeAngle(adjustedAng);
		float desiredAng = (angToTarget + (adjustedAng > 180 ? 90 : -90));
		desiredAng = MathHelper.normalizeAngle(desiredAng);
		moveTowardsDesiredAngle(desiredAng, delta);

		// Fire the cannons if delay is sufficient
		if (shotDelay <= timeSinceLastShot) {
			adjustedAng = angToTarget - rotation + 180;
			adjustedAng = MathHelper.normalizeAngle(adjustedAng);
			adjustedAng -= adjustedAng > 270 ? 180 : 0;

			// If the angle to the player is within acceptable angle range,
			// shoot
			if (adjustedAng > 80 && adjustedAng < 100) {
				Shoot();
				timeSinceLastShot = 0;
			}
		}
	}

	/**
	 * Locates the nearest target the boat should follow/attack
	 * 
	 * @return The boat to target
	 */
	public abstract Boat getNearestTarget();

}
