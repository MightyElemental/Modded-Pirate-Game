package io.github.annabeths.Boats;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Colleges.College;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Level.GameMap;

/**
 * @since Assessment 2
 * @author James Burnell
 * Abstract class that represents Boats with AI-behaviour.
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
	 * Constructor for AIBoat
	 * @param controller an instance of GameController that this boat belongs to
	 * @param initialPosition the initial position for the boat
	 * @param texLoc file location of the boat's texture
	 */
	public AIBoat(GameController controller, Vector2 initialPosition, String texLoc) {
		super(controller, initialPosition, texLoc);

		this.initialPosition = initialPosition.cpy();
		this.state = AIState.IDLE;
	}

	/**
	 * Enum describing state of the AI
	 */
	public enum AIState {
		ATTACK, APPROACH, IDLE
	}

	/**
	 * The steps to take when in {@link AIState#IDLE idle} state
	 */
	public void idle() {
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
	 * @param delta the time since last update
	 */
	public void attack(float delta) {
	}

	/**
	 * Moves the boat towards its current destination
	 * @param delta time since last frame
	 */
	public void MoveToDestination(float delta) {
		moveTowardsDesiredAngle(getAngleToDest(), delta);
	}

	/**
	 * Figure out the angle between the boat and the destination
	 * @return The angle to the destination
	 */
	public float getAngleToDest() {
		return destination == null ? -1 : destination.cpy().sub(getCenter()).angleDeg();
	}


	/**
	 * Called once per-frame, updates state of the boat.
	 * @param delta time since last frame
	 */
	@Override
	public void Update(float delta) {
		MoveToDestination(delta);
		if (HP <= 0) Destroy();
		// Boat is near destination, set a new one
		updateDestination();
	}

	/**
	 * If the AIBoat has reached its target, choose a new random destination.
	 */
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

	/**
	 * Getter method for the AIBoat's destination
	 * @return the AIBoat's destination - Vector2.
	 */
	public Vector2 GetDestination() {
		return destination;
	}

	/**
	 * @return the destination threshold
	 */
	public float getDestinationThreshold() {
		return destinationThreshold;
	}

	/**
	 * abstract method for when the AIBoat shoots a projectile.
	 */
	abstract void Shoot();
}
