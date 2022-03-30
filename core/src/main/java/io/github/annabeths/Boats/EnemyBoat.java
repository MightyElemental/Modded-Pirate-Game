package io.github.annabeths.Boats;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Collectables.Powerup;
import io.github.annabeths.Collectables.PowerupType;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Projectiles.Projectile;
import io.github.annabeths.Projectiles.ProjectileData;

/**
 * A boat that attacks the player
 * 
 * @author James Burnell
 */
public class EnemyBoat extends AIBoat {

	/** The current state of the AI */
	public AiState state;
	/** How close it needs to be to the player before it attacks */
	public float attackRange = 400;
	/** How close it needs to be to the player before it starts approaching */
	public float approachRange = 650;

	public EnemyBoat(GameController controller, Vector2 position) {
		super(controller, position, "img/entity/boat2.png");

		xpValue = 150;
		plunderValue = 100;

		this.HP = 100;
		this.maxHP = 100;
		this.speed = 75;
		this.turnSpeed = 150;
		// uncomment for fun
		// this.shotDelay = 0.01f;
	}

	public enum AiState {
		ATTACK, APPROACH, IDLE
	}

	@Override
	public void Update(float delta) {
		if (HP <= 0) Destroy();
		timeSinceLastShot += delta;

		updateAiState();

		// if (state != AiState.IDLE) System.out.println(state);

		switch (state) {
		case APPROACH:
			if (isDestValid(controller.playerBoat.getCenter())) {
				destination = controller.playerBoat.getCenter();
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
		float angToPlayer = getCenter().sub(controller.playerBoat.getCenter()).angleDeg();

		// Move at a 90 degree angle to the play to align the boat to shoot
		destination = null;
		float adjustedAng = angToPlayer - rotation;
		adjustedAng += adjustedAng < 0 ? 360 : 0;
		float desiredAng = (angToPlayer + (adjustedAng > 180 ? 90 : -90)) % 360;
		desiredAng += desiredAng < 0 ? 360 : 0;
		moveTowardsDesiredAngle(desiredAng, delta);

		// Fire the cannons if delay is sufficient
		if (shotDelay <= timeSinceLastShot) {
			adjustedAng = angToPlayer - rotation + 180;
			adjustedAng = adjustedAng > 270 ? adjustedAng - 180 : adjustedAng;

			// System.out.println(adjustedAng);

			// If the angle to the player is within acceptable angle range, shoot
			if (adjustedAng > 80 && adjustedAng < 100) {
				Shoot();
				timeSinceLastShot = 0;
			}
		}
	}

	private Boat getNearestTarget() {

		if (controller.playerBoat.getCenter().dst(getCenter()) < approachRange) {
			return controller.playerBoat;
		}

		FriendlyBoat nearestTarget = null;
		float shortestDistance = 1000;
		for (PhysicsObject obj : controller.physicsObjects) {
			if (obj instanceof FriendlyBoat) {
				float dst = obj.getCenter().dst(getCenter());
				if (dst < shortestDistance) {
					nearestTarget = (FriendlyBoat) obj;
					shortestDistance = dst;
				}
			}
		}
		return nearestTarget;
	}

	private void updateAiState() {
		Boat target = getNearestTarget();
		if (target != null) {
			float dstToTarget = target.getCenter().dst(getCenter());

			if (dstToTarget < attackRange) {
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
		if (MathUtils.randomBoolean(0.8f)) {
			controller.NewPhysicsObject(new Powerup(PowerupType.randomPower(), getCenter()));
		}
	}

	@Override
	public void OnCollision(PhysicsObject other) {
		float dmgToInflict = 0;
		// whether or not the object belongs to the player
		boolean objWasPlayer = false;

		if (other instanceof Projectile) {
			Projectile p = (Projectile) other;
			if (p.isPlayerProjectile()) {
				objWasPlayer = true;
				other.kill();
				dmgToInflict = p.getDamage();
			}
		}

		if (objWasPlayer) controller.xp += (dmgToInflict / maxHP) * xpValue;
		damage(dmgToInflict);
	}

}
