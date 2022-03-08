package io.github.annabeths.Boats;

import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Projectiles.Projectile;

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
		super(controller, position, "img/boat2.png");

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
		moveTowardsDesiredAngle(angToPlayer + (adjustedAng > 180 ? 90 : -90), delta);

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

	private void updateAiState() {
		float dstToPlayer = controller.playerBoat.getCenter().dst(getCenter());
		if (dstToPlayer < attackRange) {
			// once close enough, rotate to attack
			state = AiState.ATTACK;
		} else if (dstToPlayer < approachRange) {
			// move towards player if possible
			state = AiState.APPROACH;
		} else {
			state = AiState.IDLE;
		}
	}

	@Override
	void Shoot() {
		Projectile projLeft = new Projectile(getCenter(), rotation - 90,
				controller.projectileHolder.stock, false, 0, 1);
		Projectile projRight = new Projectile(getCenter(), rotation + 90,
				controller.projectileHolder.stock, false, 0, 1);
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
		if (other instanceof Projectile) {
			Projectile p = (Projectile) other;
			if (p.isPlayerProjectile) {
				other.killOnNextTick = true;
				if (p.isPlayerProjectile) controller.xp += (p.damage / maxHP) * xpValue;
				HP -= p.damage;
			}
		}
	}

}
