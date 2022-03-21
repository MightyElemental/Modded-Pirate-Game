package io.github.annabeths.Projectiles;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.GameGenerics.GameObject;
import io.github.annabeths.GameGenerics.PhysicsObject;

/**
 * A type of projectile that travels instantly and inflicts damage on objects in
 * its path
 * 
 * @author James Burnell
 */
public class ProjectileRay extends GameObject {

	// TODO: Damage objects that intersect

	private Vector2 endPoint;

	private ProjectileData pd;
	private float dmgMul;

	/**
	 * Create a projectile ray with a default distance of 500 units.
	 * 
	 * @param origin the starting point of the ray
	 * @param data the type of projectile
	 * @param angle the angle the ray is facing
	 * @param isPlayerProjectile if the ray belongs to the player
	 */
	public ProjectileRay(Vector2 origin, float angle, ProjectileData data,
			boolean isPlayerProjectile) {
		this(origin, angle, data, isPlayerProjectile, 500f, 1f);
	}

	/**
	 * @param origin the starting point of the ray
	 * @param data the type of projectile
	 * @param angle the angle the ray is facing
	 * @param isPlayerProjectile if the ray belongs to the player
	 * @param maxDistance the maximum distance the projectile can move
	 * @param damageMultiplier how much to multiply the damage by
	 */
	public ProjectileRay(Vector2 origin, float angle, ProjectileData data,
			boolean isPlayerProjectile, float maxDistance, float damageMultiplier) {
		this.position = origin.cpy();
		this.rotation = angle;
		this.pd = data;
		this.dmgMul = damageMultiplier;

		Vector2 dirVec = new Vector2(maxDistance, 0).setAngleDeg(angle);
		this.endPoint = origin.cpy().add(dirVec);
	}

	/**
	 * Returns a list of {@link PhysicsObject}s that intersect with the ray
	 * 
	 * @param physObjs the list of objects in the world
	 * @return A list of intersecting objects
	 */
	public List<PhysicsObject> getIntersectingObjects(List<PhysicsObject> physObjs) {
		List<PhysicsObject> result = new LinkedList<>();

		// Add all intersecting objects to result list
		physObjs.forEach(p -> {
			if (Intersector.intersectSegmentPolygon(position, endPoint, p.collisionPolygon)) {
				result.add(p);
			}
		});

		return result;
	}

	/**
	 * Returns a list of {@link PhysicsObject}s that intersect with the ray sorted
	 * from closest to farthest.
	 * 
	 * @param physObjs the list of objects in the world
	 * @return A sorted list of intersecting objects
	 */
	public List<PhysicsObject> getSortedIntersectingObjects(List<PhysicsObject> physObjs) {
		List<PhysicsObject> result = getIntersectingObjects(physObjs);

		result.sort(Comparator.comparingDouble(e -> position.dst2(e.getCenter())));

		return result;
	}

	/**
	 * Returns the first {@code n} {@link PhysicsObject}s that intersect with the
	 * ray sorted from closest to farthest.
	 * 
	 * @param physObjs the list of objects in the world
	 * @param count the max number of objects to return
	 * @return A sorted list of intersecting objects
	 */
	public List<PhysicsObject> getNClosestIntersectingObjects(List<PhysicsObject> physObjs,
			int count) {
		return getSortedIntersectingObjects(physObjs).stream().limit(count)
				.collect(Collectors.toList());
	}

	/**
	 * @return the origin
	 */
	public Vector2 getOrigin() {
		return position;
	}

	/**
	 * @return the endPoint
	 */
	public Vector2 getEndPoint() {
		return endPoint;
	}

	public float getDamage() {
		return pd.damage * dmgMul;
	}

}
