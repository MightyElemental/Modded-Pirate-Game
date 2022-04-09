package io.github.annabeths.Boats;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Colleges.College;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Level.GameMap;
import io.github.annabeths.Projectiles.Projectile;
import io.github.annabeths.Projectiles.ProjectileData;
import io.github.annabeths.Projectiles.ProjectileRay;

public class FriendlyBoatTest {

	public GameController gc;
	public FriendlyBoat b;

	@BeforeEach
	public void setup() {
		gc = mock(GameController.class);
		gc.map = mock(GameMap.class);
		gc.colleges = new ArrayList<College>();
		gc.physicsObjects = new ArrayList<PhysicsObject>();
		gc.rays = new ArrayList<ProjectileRay>();
		doCallRealMethod().when(gc).NewPhysicsObject(any(PhysicsObject.class));
		gc.playerBoat = new PlayerBoat(gc, new Vector2(0, 0));

		b = mock(FriendlyBoat.class, withSettings().useConstructor(gc, new Vector2(0, 0))
				.defaultAnswer(CALLS_REAL_METHODS));
		b.maxHP = 100;
		b.HP = b.maxHP;
	}

	@Test
	public void testGetNearestTarget() {
		for (int i = 10; i < 200; i += 50) {
			gc.NewPhysicsObject(new EnemyBoat(gc, new Vector2(i, 0)));
		}
		for (Iterator<PhysicsObject> iterator = gc.physicsObjects.iterator(); iterator.hasNext();) {
			EnemyBoat eb = (EnemyBoat) iterator.next();
			assertEquals(eb, b.getNearestTarget());
			iterator.remove();
		}
	}

	@Test
	public void testGetNearestTargetNoEnemyBoats() {
		assertNull(b.getNearestTarget());
	}

	@Test
	public void testGetNearestTargetPlayer() {
		// should not target player
		gc.NewPhysicsObject(new PlayerBoat(gc, new Vector2(0, 0)));
		assertNull(b.getNearestTarget());
	}

	@Test
	public void testOnCollisionProjectileNotPlayer() {
		Projectile p = mock(Projectile.class,
				withSettings().useConstructor(new Vector2(0, 0), 0f, ProjectileData.STOCK, false)
						.defaultAnswer(CALLS_REAL_METHODS));
		float health = b.getHealth();
		b.OnCollision(p);
		verify(p, times(1)).kill();
		assertTrue(b.getHealth() < health);
	}

	@Test
	public void testOnCollisionProjectilePlayer() {
		Projectile p = mock(Projectile.class,
				withSettings().useConstructor(new Vector2(0, 0), 0f, ProjectileData.STOCK, true)
						.defaultAnswer(CALLS_REAL_METHODS));
		float health = b.getHealth();
		b.OnCollision(p);
		verify(p, never()).kill();
		assertEquals(health, b.getHealth());
	}

	@Test
	public void testOnCollisionNonProjectile() {
		EnemyBoat eb = mock(EnemyBoat.class);
		float health = b.getHealth();
		b.OnCollision(eb);
		assertEquals(health, b.getHealth());
	}

}
