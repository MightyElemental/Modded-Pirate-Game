package io.github.annabeths.Boats;

import java.util.Random;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Colleges.College;

import com.badlogic.gdx.math.Intersector;

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

		if (Math.abs(targetAngle - rotation) > angleThreshold) {
			turningMultiplier = (targetAngle > rotation) ? 0.5f : -0.5f;
		} else {
			turningMultiplier = 0;
		}
		Turn(delta, turningMultiplier);

		// Boat is near destination, set a new one
		if (position.dst(destination) <= destinationThreshold) {
			boolean newDestinationSet = false;
			// Keep going until we find a valid destination
			while (newDestinationSet == false) {
				Random r = new Random();
				newDestinationSet = SetDestination(new Vector2(r.nextInt((int) mapSize.x + 1),
						r.nextInt((int) mapSize.y + 1)));
			}
		}
	}

	/**
	 * Attempts to set the boat's destination to the target passed, fails if that
	 * would intersect a college to get to it
	 * 
	 * @param target the destination you want the ship to move to
	 * @return {@code true} if the destination was valid, {@code false} otherwise
	 */
	boolean SetDestination(Vector2 target) {
		// We want to check if there is any college between the boat and its destination
		for (College college : controller.colleges) {
			if (Intersector.intersectLinePolygon(position, target, college.collisionPolygon)) {
				// the line has hit a college, return false and set a new destination
				return false;
			}
		}
		initialPosition = position.cpy();
		this.destination = target;
		return true;
	}

	Vector2 GetDestination() {
		return destination;
	}
}
