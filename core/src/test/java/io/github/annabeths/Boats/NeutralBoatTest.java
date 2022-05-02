package io.github.annabeths.Boats;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.GeneralControl.TestHelper;
import io.github.annabeths.GeneralControl.eng1game;
import io.github.annabeths.Level.GameMap;
import io.github.annabeths.Projectiles.Projectile;
import io.github.annabeths.Projectiles.ProjectileData;

public class NeutralBoatTest {

	private static GameController gc;
	private NeutralBoat nb;

	@BeforeAll
	public static void init() {
		TestHelper.setupEnv();
	}

	@BeforeEach
	public void setup() {
		gc = mock(GameController.class, withSettings().useConstructor(mock(eng1game.class))
				.defaultAnswer(CALLS_REAL_METHODS));
		gc.map = mock(GameMap.class);

		nb = new NeutralBoat(gc, new Vector2(0, 0));
	}

	@Test
	public void testDestroyKills() {
		assertFalse(nb.removeOnNextTick());
		nb.Destroy();
		assertTrue(nb.removeOnNextTick());
	}

	@Test
	public void testDestroyDropsPowerupRandomly() {
		// The number of powerups should be less than the number of destroy
		// calls
		int runs = 10000;
		for (int i = 0; i < runs; i++) {
			nb.Destroy();
		}
		assertTrue(gc.physicsObjects.size() < runs);
	}

	@Test
	public void testOnCollisionPlayerBoat() {
		PlayerBoat pb = new PlayerBoat(gc, new Vector2(0, 0));
		float xp = gc.getXp();
		int gold = gc.getPlunder();
		nb.OnCollision(pb);
		assertTrue(gc.getXp() > xp, "XP should increase after collision");
		assertTrue(gc.getPlunder() > gold, "Plunder should increase after collision");
		assertTrue(nb.removeOnNextTick());
	}

	@Test
	public void testOnCollisionProjectileKill() {
		Projectile p = new Projectile(new Vector2(), 0, ProjectileData.STOCK, true);
		nb.OnCollision(p);
		assertTrue(p.removeOnNextTick());
	}

//	@Test
//	public void testOnCollisionProjectilePlayer() {
//		Projectile p = new Projectile(new Vector2(), 0, ProjectileData.STOCK, true);
//		float xp = gc.getXp();
//		nb.OnCollision(p);
//		assertTrue(gc.getXp() > xp, "XP should increase after collision");
//		assertEquals(nb.getMaxHealth() - p.getDamage(), nb.getHealth());
//	}

	@Test
	public void testOnCollisionProjectileNotPlayer() {
		Projectile p = new Projectile(new Vector2(), 0, ProjectileData.STOCK, false);
		float xp = gc.getXp();
		nb.OnCollision(p);
		// no xp for non player projectiles
		assertEquals(xp, gc.getXp(), "XP should be the same after collision");
		assertEquals(nb.getMaxHealth() - p.getDamage(), nb.getHealth());
	}

	@Test
	public void testOnCollisionOther() {
		float hp = nb.getHealth();
		assertDoesNotThrow(() -> nb.OnCollision(mock(NeutralBoat.class)));
		assertEquals(hp, nb.getHealth()); // no damage
	}

}
