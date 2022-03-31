package io.github.annabeths.Boats;

import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Projectiles.Projectile;
import io.github.annabeths.Projectiles.ProjectileData;

/**
 * @author Hector Woods, based largely on EnemyBoat.java
 */
public class FriendlyBoat extends AIBoat {
	/** The current state of the AI */
	public AiState state;
	/** How close it needs to be to the enemy ship before it attacks */
	public float attackRange = 400;
	/** How close it needs to be to the enemy ship before it starts approaching */
	public float approachRange = 650;

	/**
	 * The EnemyBoat this boat is currently targeting. null if there is no target.
	 */
	public EnemyBoat target;

	public FriendlyBoat(GameController controller, Vector2 position) {
		super(controller, position, "img/entity/boat1.png");

		xpValue = 0; // friendly ships should not reward xp or plunder
		plunderValue = 0;

		this.HP = 75; // slightly weaker than enemy ships
		this.maxHP = 75;
		this.speed = 65;
		this.turnSpeed = 150;
		this.target = null;
		state = AiState.IDLE;
	}

	public enum AiState {
		ATTACK, APPROACH, IDLE
	}

	@Override
	public void Update(float delta) {
		if (HP <= 0) Destroy();
		timeSinceLastShot += delta;

		updateAiState();

		switch (state) {
		case APPROACH:
			if (isDestValid(target.getCenter())) {
				destination = target.getCenter();
			} else {
				idle(delta);
			}
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

	private void idle(float delta) {
		if (destination == null) {
			SetDestination(getNewRandomValidTarget());
		} else {
			updateDestination();
		}
	}

	private void attack(float delta) {
		float angToTarget = getCenter().sub(target.getCenter()).angleDeg();

		// Move at a 90 degree angle to the play to align the boat to shoot
		destination = null;
		float adjustedAng = angToTarget - rotation;
		adjustedAng += adjustedAng < 0 ? 360 : 0;
		moveTowardsDesiredAngle(angToTarget + (adjustedAng > 180 ? 90 : -90), delta);

		// Fire the cannons if delay is sufficient
		if (shotDelay <= timeSinceLastShot) {
			adjustedAng = angToTarget - rotation + 180;
			adjustedAng = adjustedAng > 270 ? adjustedAng - 180 : adjustedAng;

			// System.out.println(adjustedAng);

			// If the angle to the player is within acceptable angle range, shoot
			if (adjustedAng > 80 && adjustedAng < 100) {
				Shoot();
				timeSinceLastShot = 0;
			}
		}
	}

	private EnemyBoat getNearestTarget() {
		EnemyBoat nearestTarget = null;
		float shortestDistance = 1000;
		for (PhysicsObject obj : controller.physicsObjects) {
			if (obj instanceof EnemyBoat) {
				float dst = obj.getCenter().dst(getCenter());
				if (dst < shortestDistance) {
					nearestTarget = (EnemyBoat) obj;
					shortestDistance = dst;
				}
			}
		}
		return nearestTarget;
	}

	private void updateAiState() {
		target = getNearestTarget();

		if (target != null) {
			float dstToTarget = target.getCenter().dst(getCenter());
			if (dstToTarget < attackRange) {
				// once close enough, rotate to attack
				state = AiState.ATTACK;
			} else if (dstToTarget < approachRange) {
				// move towards player if possible
				state = AiState.APPROACH;
			} else {
				state = AiState.IDLE;
			}
		} else {
			state = AiState.IDLE;
		}
	}

	@Override
	void Shoot() {
		// the projectile type to shoot
		ProjectileData pd = ProjectileData.STOCK;

		Projectile projLeft = createProjectile(pd, -90, 1, 1);
		Projectile projRight = createProjectile(pd, 90, 1, 1);

		// Add the projectile to the GameController's physics objects list so it
		// receives updates
		controller.NewPhysicsObject(projLeft);
		controller.NewPhysicsObject(projRight);
	}

	@Override
	void Destroy() {
		killOnNextTick = true;
	}

	@Override
	public void OnCollision(PhysicsObject other) {
		float dmgToInflict = 0;

		if (other instanceof Projectile) {
			Projectile p = (Projectile) other;
			if (!p.isPlayerProjectile()) {
				other.kill();
				dmgToInflict = p.getDamage();
			}
		}
		damage(dmgToInflict);
	}
}
