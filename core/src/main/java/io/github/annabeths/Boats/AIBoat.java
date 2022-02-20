package io.github.annabeths.Boats;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Colleges.College;

public abstract class AIBoat extends Boat {
	Vector2 initialPosition;
	Vector2 destination;
	float plunderValue;
	float xpValue;

	/** How close should the boat be to its destination before setting a new one */
	float destinationThreshold = 50f;
	/**
	 * If the boat's rotation is greater than the target angle by this much, start
	 * rotating
	 */
	float angleThreshold = 0.25f;

	/**
	 * Moves the boat towards its current destination
	 * 
	 * @param delta time since last frame
	 */
	public void MoveToDestination(float delta) {
		Move(delta, 1);

		// Figure out the angle between the boat and the destination
		float targetAngle = (float) Math
				.toDegrees(Math.atan2(destination.y - position.y, destination.x - position.x));
		float turningMultiplier = 0.5f;
		// TODO: Fix rotation direction calculation
		if (Math.abs(targetAngle - rotation) > angleThreshold) {
			turningMultiplier = (targetAngle > rotation) ? 0.5f : -0.5f;
		} else {
			turningMultiplier = 0;
		}
		Turn(delta, turningMultiplier);

		// Boat is near destination, set a new one
		if (getCenter().dst(destination) <= destinationThreshold) {
			Vector2 target = getNewRandomValidTarget();
			SetDestination(target);
			// System.out.println("New Target! " + target);
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
		Vector2 target = new Vector2(MathUtils.random.nextInt((int) mapSize.x + 1),
				MathUtils.random.nextInt((int) mapSize.y + 1));

		// Keep going until we find a valid destination
		while (!isDestValid(target)) { // TODO: Write more efficient algorithm
			target = new Vector2(MathUtils.random(100, (int) mapSize.x + 1),
					MathUtils.random((int) mapSize.y + 1));
		}
		return target;
	}

	/**
	 * A target destination is valid if the path does not intersect with a college.
	 * 
	 * @return {@code true} if the destination was valid, {@code false} otherwise
	 * @author James Burnell
	 */
	public boolean isDestValid(Vector2 target) {
		// We want to check if there is any college between the boat and its destination
		for (College college : controller.colleges) {
			if (Intersector.intersectLinePolygon(getCenter(), target, college.collisionPolygon)) {
				// the line has hit a college, return false and set a new destination
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
}
