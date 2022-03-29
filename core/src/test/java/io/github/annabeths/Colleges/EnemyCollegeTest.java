package io.github.annabeths.Colleges;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.GenericBoat;
import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.GeneralControl.DebugUtils;
import io.github.annabeths.GeneralControl.ResourceManager;
import io.github.annabeths.Level.GameMap;
import io.github.annabeths.Projectiles.Projectile;
import io.github.annabeths.Projectiles.ProjectileData;

/**
 * Tests methods within the {@link EnemyCollege} class. Not all methods can be
 * tested as libGDX headless doesn't support fonts.
 */
public class EnemyCollegeTest {

	public static GameController gc;
	public EnemyCollege col;

	@BeforeAll
	public static void init() {
		ResourceManager.font = mock(BitmapFont.class);
	}

	@BeforeEach
	public void setup() {
		gc = mock(GameController.class);
		gc.map = mock(GameMap.class);
		gc.colleges = new ArrayList<College>();
		gc.physicsObjects = new ArrayList<PhysicsObject>();
		gc.playerBoat = new PlayerBoat(gc, new Vector2(0, 0));
		doCallRealMethod().when(gc).CollegeDestroyed(any(EnemyCollege.class));
		doCallRealMethod().when(gc).NewPhysicsObject(any(PhysicsObject.class));

		col = new EnemyCollege(new Vector2(0, 0), "img/world/castle/castle_dead.png",
				"img/world/castle/castle_dead.png", gc, ProjectileData.ENEMY, 100);
		gc.NewPhysicsObject(col);
	}

	@Test
	public void testShootAtDebugSkip() {
		DebugUtils.ENEMY_COLLEGE_FIRE = false; // ensure debug is enabled
		int physCount = gc.physicsObjects.size();
		col.ShootAt(gc.playerBoat.getCenter());
		assertEquals(physCount, gc.physicsObjects.size());
	}

	@Test
	public void testShootAt() {
		DebugUtils.ENEMY_COLLEGE_FIRE = true; // ensure debug is disabled
		int physCount = gc.physicsObjects.size();
		col.ShootAt(gc.playerBoat.getCenter());
		assertNotEquals(physCount, gc.physicsObjects.size());
	}

	@Test
	public void testDraw() {
		assertDoesNotThrow(() -> col.Draw(mock(SpriteBatch.class)));
		col.HP = -10;
		assertDoesNotThrow(() -> col.Draw(mock(SpriteBatch.class)));
	}

	@Test
	public void testUpdateChangesFire() {
		float temp = col.timeSinceLastShot;
		col.Update(1);
		assertTrue(col.timeSinceLastShot > temp);
	}

	@Test
	public void testUpdateShoot() {
		int physCount = gc.physicsObjects.size();
		col.Update(10);
		assertNotEquals(physCount, gc.physicsObjects.size());
	}

	@Test
	public void testUpdateDead() {
		col.HP = -1; // kill the college
		assertDoesNotThrow(() -> col.Update(1));
	}

	/** Place boat outside of range then set the college to try to shoot. */
	@Test
	public void testUpdateBoatNotInRange() {
		gc.playerBoat.setCenter(new Vector2(600, 0));
		long physCount = gc.physicsObjects.stream().filter(p -> p instanceof Projectile).count();
		col.Update(10);
		// Should not have spawned a projectile
		assertEquals(physCount,
				gc.physicsObjects.stream().filter(p -> p instanceof Projectile).count());
	}

	@Test
	public void testHPString() {
		col.maxHP = 100;
		col.HP = 100;
		assertEquals("100/100", col.getHPString());
		col.maxHP = 13.37f;
		col.HP = 13.37f;
		assertEquals("13/13", col.getHPString());
	}

	@Test
	public void testOnCollisionProjectileAliveNpj() {
		Projectile p = new Projectile(new Vector2(0, 0), 0, ProjectileData.STOCK, false);
		assertDoesNotThrow(() -> col.OnCollision(p));
	}

	@Test
	public void testOnCollisionProjectileAlivePj() {
		Projectile p = new Projectile(new Vector2(0, 0), 0, ProjectileData.STOCK, true);
		try { // try-catch because of GlyphLayout null pointer error
			col.OnCollision(p);
		} catch (NullPointerException e) {
		}
		// Should remove projectile if collided with college
		assertTrue(p.removeOnNextTick());
	}

//	@Test
//	public void testOnCollisionProjectileAlivePjKill() {
//		Projectile p = new Projectile(new Vector2(0, 0), 0, ProjectileData.STOCK, true, 100, 1);
//		try { // try-catch because of GlyphLayout null pointer error
//			col.OnCollision(p);
//		} catch (NullPointerException e) {
//		}
//	}

	@Test
	public void testOnCollisionProjectileDead() {
		Projectile p = new Projectile(new Vector2(0, 0), 0, ProjectileData.STOCK, true);
		col.HP = -1; // kill college
		assertDoesNotThrow(() -> col.OnCollision(p));
	}

	@Test
	public void testOnCollisionNonProjectile() {
		GenericBoat b = new GenericBoat(new Vector2(0, 0));
		assertDoesNotThrow(() -> col.OnCollision(b));
	}

	@Test
	public void testBecomeVulnerable() {
		col.setInvulnerable(true); // ensure college is invulnerable
		assertTrue(col.isInvulnerable());
		try { // try-catch because of GlyphLayout null pointer error
			col.becomeVulnerable();
		} catch (NullPointerException e) {
		}
		assertFalse(col.isInvulnerable());
	}

}
