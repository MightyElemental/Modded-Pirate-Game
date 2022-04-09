package io.github.annabeths.Projectiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Used to test {@link Projectile}s and {@link ProjectileData}
 * 
 * @author James Burnell
 */
public class ProjectileTest {

	@BeforeAll
	public static void init() {
		Gdx.gl = mock(GL20.class);
	}

	ProjectileData projTestData = ProjectileData.STOCK;

	@Test
	@DisplayName("Test projectile sets despawn flag after max travel distance")
	public void testProjectileDespawn() {
		Projectile p = new Projectile(new Vector2(0, 0), 0f, projTestData, true);

		// ensure despawn flag is not set
		assertFalse(p.removeOnNextTick());

		// update projectile to move it
		for (int i = 0; i <= 10; i++) {
			p.Update(1f);
		}

		// ensure despawn flag is set
		assertTrue(p.removeOnNextTick());
	}

	@Test
	@DisplayName("Test speed is the same after being converted to velocity (2d.p.)")
	public void testSpeedIsEqual() {
		// various angles to test
		float[] rotations = { 0f, 10f, 17f, 37f, 180f, 61.54f };
		for (float r : rotations) {
			Projectile p = new Projectile(new Vector2(0, 0), r, projTestData, true);
			assertEquals(projTestData.getSpeed(), Math.round(p.getSpeed() * 100) / 100f);
		}
	}

	@Test
	public void testProjectileCollision() {
		// Two projectiles that are both non-player projectiles
		Projectile p1 = new Projectile(new Vector2(0, 0), 0, projTestData, false);
		Projectile p2 = new Projectile(new Vector2(0, 0), 0, projTestData, false);
		p1.OnCollision(p2);
		assertFalse(p1.removeOnNextTick());
		assertFalse(p2.removeOnNextTick());

		// Two projectiles where one is a player, the other is not
		Projectile p3 = new Projectile(new Vector2(0, 0), 0, projTestData, true);
		Projectile p4 = new Projectile(new Vector2(0, 0), 0, projTestData, false);
		p3.OnCollision(p4);
		assertTrue(p3.removeOnNextTick());
		assertTrue(p4.removeOnNextTick());

		// Two projectiles that are both player projectiles
		Projectile p5 = new Projectile(new Vector2(0, 0), 0, projTestData, true);
		Projectile p6 = new Projectile(new Vector2(0, 0), 0, projTestData, true);
		p5.OnCollision(p6);
		assertFalse(p5.removeOnNextTick());
		assertFalse(p6.removeOnNextTick());
	}

	@Test
	public void testDraw() {
		Projectile p1 = new Projectile(new Vector2(0, 0), 0, projTestData, false);
		SpriteBatch b = mock(SpriteBatch.class);
		assertDoesNotThrow(() -> p1.Draw(b)); // ensure draw method doesn't throw an error
	}

	@Test
	public void testGetDamage() {
		Projectile p = new Projectile(new Vector2(0, 0), 0, projTestData, false, 5, 1);
		assertEquals(projTestData.damage * 5, p.getDamage());
	}

	@Test
	public void testProjDataGetTexture() {
		assertEquals(projTestData.texture, projTestData.getTexture());
	}

	@Test
	public void testProjDataGetSize() {
		assertEquals(projTestData.size, projTestData.getSize());
	}

}
