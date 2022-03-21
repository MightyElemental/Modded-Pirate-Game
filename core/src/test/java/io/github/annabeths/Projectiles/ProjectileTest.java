package io.github.annabeths.Projectiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;

public class ProjectileTest {

	@BeforeAll
	public static void init() {
		System.out.println("Setting up mock GL instance");
		Gdx.gl = mock(GL20.class);
	}

	ProjectileData projTestData = ProjectileData.STOCK;

	@Test
	@DisplayName("Test projectile sets despawn flag after max travel distance")
	public void testProjectileDespawn() {
		Projectile p = new Projectile(Vector2.Zero, 0f, projTestData, true);

		// ensure despawn flag is not set
		assertFalse(p.killOnNextTick);

		// update projectile to move it
		for (int i = 0; i <= 10; i++) {
			p.Update(1f);
		}

		// ensure despawn flag is set
		assertTrue(p.killOnNextTick);
	}

	@Test
	@DisplayName("Test speed is the same after being converted to velocity (2d.p.)")
	public void testSpeedIsEqual() {
		// various angles to test
		float[] rotations = { 0f, 10f, 17f, 37f, 180f, 61.54f };
		for (float r : rotations) {
			Projectile p = new Projectile(Vector2.Zero, r, projTestData, true);
			assertEquals(projTestData.getSpeed(), Math.round(p.getSpeed() * 100) / 100f);
		}
	}

}
