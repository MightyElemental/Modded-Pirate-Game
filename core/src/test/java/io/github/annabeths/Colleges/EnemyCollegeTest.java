package io.github.annabeths.Colleges;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.GeneralControl.DebugUtils;
import io.github.annabeths.GeneralControl.ResourceManager;
import io.github.annabeths.Level.GameMap;
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
		doCallRealMethod().when(gc).NewPhysicsObject(any(PhysicsObject.class));
		col = new EnemyCollege(new Vector2(0, 0), "img/world/castle/castle_dead.png",
				"img/world/castle/castle_dead.png", gc, ProjectileData.ENEMY, 100);
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

	/** Place boat outside of range then set the college to try to shoot. */
	@Test
	public void testUpdateBoatNotInRange() {
		gc.playerBoat.setCenter(new Vector2(600, 0));
		int physCount = gc.physicsObjects.size();
		col.Update(10);
		// Should not have spawned a projectile
		assertEquals(physCount, gc.physicsObjects.size());
	}

}
