package io.github.annabeths.Boats;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Colleges.College;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Level.GameMap;

/**
 * @since Assessment 2
 */
public abstract class AIBoat extends Boat {

	/** The current state of the AI */
	public AIState state;

	Vector2 initialPosition;
	Vector2 destination;
	public float plunderValue;
	public float xpValue;

	/** How close should the boat be to its destination before setting a new one */
	float destinationThreshold = 50f;
	/**
	 * If the boat's rotation is greater than the target angle by this much, start
	 * rotating
	 */
	float angleThreshold = 0.25f;

	public AIBoat(GameController controller, Vector2 initialPosition, String texLoc) {
		super(controller, initialPosition, texLoc);

		this.initialPosition = initialPosition.cpy();
		this.state = AIState.IDLE;
	}

	public enum AIState {
		ATTACK, APPROACH, IDLE
	}

	/** Updates the AI state */
	public void updateAIState() {
	}

	/**
	 * The steps to take when in {@link AIState#IDLE idle} state
	 * 
	 * @param delta the time since last update
	 */
	public void idle(float delta) {
		if (destination == null) {
			SetDestination(getNewRandomValidTarget());
		} else {
			updateDestination();
		}
	}

	/**
	 * The steps to take when in {@link AIState#APPROACH approach} state
	 * 
	 * @param delta the time since last update
	 */
	public void approach(float delta) {
	}

	/**
	 * The steps to take when in {@link AIState#ATTACK attack} state
	 * 
	 * @param delta the time since last update
	 */
	public void attack(float delta) {
	}

	/**
	 * Moves the boat towards its current destination
	 * 
	 * @param delta time since last frame
	 */
	public void MoveToDestination(float delta) {
		moveTowardsDesiredAngle(getAngleToDest(), delta);
	}

	/**
	 * Figure out the angle between the boat and the destination
	 * 
	 * @return The angle to the destination
	 */
	public float getAngleToDest() {
		return destination == null ? -1 : destination.cpy().sub(getCenter()).angleDeg();
	}

	@Override
	public void Update(float delta) {
		MoveToDestination(delta);
		if (HP <= 0) Destroy();
		// Boat is near destination, set a new one
		updateDestination();
	}

	public void updateDestination() {
		if (destination == null || getCenter().dst(destination) <= destinationThreshold) {
			Vector2 target = getNewRandomValidTarget();
			SetDestination(target);
		}
	}

	/**
	 * Continually generates a random target until a valid one is found.
	 * 
	 * @return The new target destination
	 * @author James Burnell
	 * @see #isDestValid(Vector2)
	 */
	protected Vector2 getNewRandomValidTarget() {
		Vector2 target = GameMap.getRandomPointInBounds();

		// Keep going until we find a valid destination
		while (!isDestValid(target)) { // TODO: Write more efficient algorithm
			target = GameMap.getRandomPointInBounds();
		}
		return target;
	}

	/**
	 * A target destination is valid if the path does not intersect with a college.
	 * 
	 * @param target the target destination
	 * @return {@code true} if the destination was valid, {@code false} otherwise
	 * @author James Burnell
	 */
	public boolean isDestValid(Vector2 target) {
		// We want to check if there is any college between the boat and its destination
		for (College college : controller.colleges) {
			if (Intersector.intersectSegmentPolygon(getCenter(), target,
					college.collisionPolygon)) {
				// the line has hit a college, return false and set a new destination
//				System.out.println(
//						"hit: " + college.getCenter() + " | " + getCenter() + " <->" + target);
				return false;
			}
		}
		return true;
	}

	/**
	 * Sets the target destination
	 * 
	 * @param target the destination you want the ship to move to
	 */
	void SetDestination(Vector2 target) {
		initialPosition = position.cpy();
		this.destination = target;
	}

	public Vector2 GetDestination() {
		return destination;
	}

	/**
	 * @return the destination threshold
	 */
	public float getDestinationThreshold() {
		return destinationThreshold;
	}

	abstract void Shoot();
}
