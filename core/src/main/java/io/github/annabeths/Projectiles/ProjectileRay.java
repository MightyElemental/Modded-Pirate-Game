package io.github.annabeths.Projectiles;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.GameGenerics.PhysicsObject;

/**
 * A type of projectile that travels instantly and inflicts damage on objects in
 * its path
 * 
 * @author James Burnell
 */
public class ProjectileRay extends Projectile {

	private Vector2 endPoint;

	private ProjectileData pd;
	private float dmgMul;

	/** How long the ray should be drawn on screen in seconds */
	private float showTime = 1f;
	/** How much time is remaining to show the ray in seconds */
	private float remainShowTime = showTime;

	private Vector2 farthestHitPoint;

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
		super(origin, angle, data, isPlayerProjectile);
		this.position = origin.cpy();
		this.rotation = angle;
		this.pd = data;
		this.dmgMul = damageMultiplier;

		Vector2 dirVec = new Vector2(maxDistance, 0).setAngleDeg(angle);
		this.endPoint = origin.cpy().add(dirVec);
		this.farthestHitPoint = endPoint.cpy();
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
			// skip if belongs to player
			if (isPlayerProjectile() && p instanceof PlayerBoat) return;
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

	public void fireRay(List<PhysicsObject> physObjs, int passThroughLimit) {
		List<PhysicsObject> intersection = getNClosestIntersectingObjects(physObjs,
				passThroughLimit);
		intersection.forEach(e -> OnCollision(e));

		// set the point of the ray that is closest to the farthest hit object
		if (!intersection.isEmpty()) {
			PhysicsObject p = intersection.get(intersection.size() - 1);
			Intersector.nearestSegmentPoint(getOrigin(), getEndPoint(), p.getCenter(),
					farthestHitPoint);
		}
	}

	public void OnCollision(PhysicsObject obj) {
		obj.OnCollision(this);
	}

	public void Update(float delta) {
		remainShowTime -= delta;
	}

	public boolean removeOnNextTick() {
		return remainShowTime <= 0;
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

	/**
	 * @return the showTime
	 */
	public float getShowTime() {
		return showTime;
	}

	/**
	 * @return the remainShowTime
	 */
	public float getRemainShowTime() {
		return remainShowTime;
	}

	@Override
	public Vector2 getCenter() {
		return getOrigin().cpy().lerp(endPoint, 0.5f);
	}

	@Override
	public float getCenterX() {
		return getCenter().x;
	}

	@Override
	public float getCenterY() {
		return getCenter().y;
	}

	/**
	 * @return the farthestHitPoint
	 */
	public Vector2 getFarthestHitPoint() {
		return farthestHitPoint;
	}

}
