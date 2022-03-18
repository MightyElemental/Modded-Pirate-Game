package io.github.annabeths.Projectiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.GenericBoat;
import io.github.annabeths.GameGenerics.PhysicsObject;

public class ProjectileRayTest {

	public List<PhysicsObject> objects;
	/** Array of objects where the order does not change */
	public PhysicsObject[] sortedObjects;

	@BeforeEach
	public void setupWorld() {

		objects = new ArrayList<PhysicsObject>();

		// line of boats
		for (int i = 0; i < 5; i++) {
			objects.add(new GenericBoat(new Vector2(100 + i * 150, 0)));
		}

		// boat at different y
		objects.add(new GenericBoat(new Vector2(100, 100)));

		// store order
		sortedObjects = objects.toArray(PhysicsObject[]::new);

		// randomize order with seeded random
		Collections.shuffle(objects, new Random(2022_03_17));
	}

	@Test
	public void testIntersection() {
		// default ray facing to the right
		ProjectileRay ray = new ProjectileRay(new Vector2(0, 25), 0, true);
		List<PhysicsObject> objs = ray.getIntersectingObjects(objects);

		// should contain the first three objects in the array
		for (int i = 0; i < 3; i++) {
			assertTrue(objs.contains(sortedObjects[i]));
		}

		// should not contain any other object
		for (int i = 3; i < sortedObjects.length; i++) {
			assertFalse(objs.contains(sortedObjects[i]));
		}
	}

	@Test
	public void testIntersectionSorting() {
		ProjectileRay ray = new ProjectileRay(new Vector2(0, 25), 0, true);
		List<PhysicsObject> objs = ray.getSortedIntersectingObjects(objects);

		// each element should match the sorted order
		for (int i = 0; i < objs.size(); i++) {
			assertEquals(sortedObjects[i], objs.get(i));
		}
	}

	@Test
	public void testLimitIntersection() {
		ProjectileRay ray = new ProjectileRay(new Vector2(0, 25), 0, true);
		List<PhysicsObject> objs = ray.getNClosestIntersectingObjects(objects, 2);

		// should contain the first three objects in the array
		for (int i = 0; i < 2; i++) {
			assertTrue(objs.contains(sortedObjects[i]));
		}

		// should not contain any other object
		for (int i = 2; i < sortedObjects.length; i++) {
			assertFalse(objs.contains(sortedObjects[i]));
		}
	}

}
