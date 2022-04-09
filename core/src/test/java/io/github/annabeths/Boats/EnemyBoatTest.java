package io.github.annabeths.Boats;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Collectables.Powerup;
import io.github.annabeths.Colleges.College;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Level.GameMap;
import io.github.annabeths.Projectiles.Projectile;
import io.github.annabeths.Projectiles.ProjectileData;
import io.github.annabeths.Projectiles.ProjectileRay;

public class EnemyBoatTest {

	public GameController gc;
	public EnemyBoat b;

	@BeforeEach
	public void setup() {
		gc = mock(GameController.class);
		gc.map = mock(GameMap.class);
		gc.colleges = new ArrayList<College>();
		gc.physicsObjects = new ArrayList<PhysicsObject>();
		gc.rays = new ArrayList<ProjectileRay>();
		doCallRealMethod().when(gc).NewPhysicsObject(any(PhysicsObject.class));
		gc.playerBoat = new PlayerBoat(gc, new Vector2(0, 0));

		b = mock(EnemyBoat.class, withSettings().useConstructor(gc, new Vector2(0, 0))
				.defaultAnswer(CALLS_REAL_METHODS));
	}

	@Test
	public void testDestroy() {
		for (int i = 0; i < 1000; i++) {
			b.Destroy();
		}
		// test boat should be removed
		assertTrue(b.removeOnNextTick());
		// test that destroy spawns a powerup
		verify(gc, atLeast(1)).NewPhysicsObject(any(Powerup.class));
	}

	@Test
	public void testOnCollisionPlayer() {
		float xp = gc.xp;
		float plunder = gc.plunder;
		b.OnCollision(gc.playerBoat);
		verify(b, times(1)).Destroy();
		assertTrue(gc.xp > xp);
		assertTrue(gc.plunder > plunder);
	}

	@Test
	public void testOnCollisionProjectilePlayer() {
		Projectile p = new Projectile(new Vector2(0, 0), 0, ProjectileData.STOCK, true);
		float health = b.getHealth();
		float xp = gc.xp;
		b.OnCollision(p);
		assertTrue(p.removeOnNextTick());
		assertTrue(b.getHealth() < health);
		assertTrue(gc.xp > xp);
	}

	@Test
	public void testOnCollisionProjectileNonPlayer() {
		Projectile p = new Projectile(new Vector2(0, 0), 0, ProjectileData.STOCK, false);
		float health = b.getHealth();
		b.OnCollision(p);
		assertFalse(p.removeOnNextTick());
		assertEquals(health, b.getHealth());
	}

	@Test
	public void testGetNearestTargetPlayerNear() {
		for (int i = 10; i < 200; i += 50) {
			gc.NewPhysicsObject(new FriendlyBoat(gc, new Vector2(i, 0)));
		}
		assertEquals(gc.playerBoat, b.getNearestTarget());
	}

	@Test
	public void testGetNearestTargetNothing() {
		gc.playerBoat = new PlayerBoat(gc, new Vector2(1000, 0));
		assertNull(b.getNearestTarget());
	}

	@Test
	public void testGetNearestTargetFriendlyBoat() {
		gc.playerBoat = new PlayerBoat(gc, new Vector2(1000, 0));
		for (int i = 10; i < 200; i += 50) {
			gc.NewPhysicsObject(new FriendlyBoat(gc, new Vector2(i, 0)));
		}
		for (Iterator<PhysicsObject> iterator = gc.physicsObjects.iterator(); iterator.hasNext();) {
			FriendlyBoat eb = (FriendlyBoat) iterator.next();
			assertEquals(eb, b.getNearestTarget());
			iterator.remove();
		}
	}

}
