package io.github.annabeths.Projectiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.NeutralBoat;
import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.GeneralControl.TestHelper;
import io.github.annabeths.GeneralControl.eng1game;
import io.github.annabeths.Level.GameMap;

public class ProjectileRayTest {

	public List<PhysicsObject> objects;
	/** Array of objects where the order does not change */
	public PhysicsObject[] sortedObjects;
	public ProjectileData pd = ProjectileData.STOCK;
	public static GameController gc;

	@BeforeAll
	public static void init() {
		TestHelper.setupEnv();
	}

	@BeforeEach
	public void setupWorld() {
		gc = mock(GameController.class, withSettings().useConstructor(mock(eng1game.class))
				.defaultAnswer(CALLS_REAL_METHODS));
		gc.map = mock(GameMap.class);

		objects = new ArrayList<PhysicsObject>();

		// line of boats
		for (int i = 0; i < 5; i++) {
			objects.add(new NeutralBoat(gc, new Vector2(100 + i * 150, 0)));
		}

		// boat at different y
		objects.add(new NeutralBoat(gc, new Vector2(100, 100)));

		// store order
		sortedObjects = objects.toArray(PhysicsObject[]::new);

		// randomize order with seeded random
		Collections.shuffle(objects, new Random(2022_03_17));
	}

	@Test
	public void testIntersection() {
		// default ray facing to the right
		ProjectileRay ray = new ProjectileRay(new Vector2(0, 25), 0, pd, true);
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
		ProjectileRay ray = new ProjectileRay(new Vector2(0, 25), 0, pd, true);
		List<PhysicsObject> objs = ray.getSortedIntersectingObjects(objects);

		// each element should match the sorted order
		for (int i = 0; i < objs.size(); i++) {
			assertEquals(sortedObjects[i], objs.get(i));
		}
	}

	@Test
	public void testLimitIntersection() {
		ProjectileRay ray = new ProjectileRay(new Vector2(0, 25), 0, pd, true);
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

	@Test
	public void testFireMethod() {
		NeutralBoat gb = (NeutralBoat) sortedObjects[0];

		ProjectileRay ray = new ProjectileRay(new Vector2(0, 25), 0, pd, true);
		ray.fireRay(objects, 1);

		assertEquals(gb.getMaxHealth() - pd.damage, gb.getHealth());

		// ensure the rest of the objects are unaffected
		for (int i = 1; i < sortedObjects.length; i++) {
			gb = (NeutralBoat) sortedObjects[i];
			assertEquals(gb.getMaxHealth(), gb.getHealth());
		}
	}

	@Test
	public void testEmptyFireMethod() {
		ProjectileRay ray = new ProjectileRay(new Vector2(0, 25), 0, pd, true);
		assertDoesNotThrow(() -> ray.fireRay(new ArrayList<PhysicsObject>(), 1));
	}

	@Test
	public void testRemoveAfterShown() {
		ProjectileRay ray = new ProjectileRay(new Vector2(0, 25), 0, pd, true);
		float showTime = ray.getShowTime();

		assertFalse(ray.removeOnNextTick());
		assertEquals(ray.getShowTime(), ray.getRemainShowTime());
		ray.Update(showTime); // skip to end of show period
		assertTrue(ray.removeOnNextTick());
		assertEquals(ray.getRemainShowTime(), 0);
	}

	@Test
	public void testGetCenter() {
		ProjectileRay ray = new ProjectileRay(new Vector2(0, 25), 0, pd, true);
		Vector2 start = ray.getOrigin().cpy();
		Vector2 end = ray.getEndPoint().cpy();
		Vector2 center = start.add(end).scl(0.5f);

		assertEquals(center, ray.getCenter());
		assertEquals(center.x, ray.getCenterX());
		assertEquals(center.y, ray.getCenterY());
	}

	@Test
	public void testFarthestHitPointOneObj() {
		NeutralBoat gb = (NeutralBoat) sortedObjects[0];
		Vector2 expectedHit = new Vector2(0, 0);
		ProjectileRay ray = new ProjectileRay(new Vector2(0, 25), 0, pd, true);

		// Test case when there is only one object
		ray.fireRay(objects, 1);
		Intersector.nearestSegmentPoint(ray.getOrigin(), ray.getEndPoint(), gb.getCenter(),
				expectedHit);
		Vector2 hit = ray.getFarthestHitPoint();
		assertEquals(expectedHit, hit);
	}

	@Test
	public void testFarthestHitPointTwoObj() {
		NeutralBoat gb = (NeutralBoat) sortedObjects[1];
		Vector2 expectedHit = new Vector2(0, 0);
		ProjectileRay ray = new ProjectileRay(new Vector2(0, 25), 0, pd, true);

		// Test case when there is more than one object
		ray.fireRay(objects, 2);
		Intersector.nearestSegmentPoint(ray.getOrigin(), ray.getEndPoint(), gb.getCenter(),
				expectedHit);
		Vector2 hit = ray.getFarthestHitPoint();
		assertEquals(expectedHit, hit);
	}

	/**
	 * Test how the {@link ProjectileRay#getIntersectingObjects(List)} method
	 * handles playerboats when the ray does/doesn't belong to the player
	 */
	@Test
	public void testGetIntersectingObjsPlayer() {
		ProjectileRay ray = new ProjectileRay(new Vector2(0, 25), 0, pd, true);

		List<PhysicsObject> objects = new ArrayList<PhysicsObject>();
		PlayerBoat pb = new PlayerBoat(gc, new Vector2(50, 0));
		objects.add(pb);
		NeutralBoat nb = new NeutralBoat(gc, new Vector2(250, 0));
		objects.add(nb);

		List<PhysicsObject> inter = ray.getIntersectingObjects(objects);
		assertTrue(inter.contains(nb));
		assertFalse(inter.contains(pb));

		ray = new ProjectileRay(new Vector2(0, 25), 0, pd, false);
		inter = ray.getIntersectingObjects(objects);
		assertTrue(inter.contains(nb));
		assertTrue(inter.contains(pb));
	}

}
