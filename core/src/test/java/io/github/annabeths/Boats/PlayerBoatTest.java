package io.github.annabeths.Boats;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Collectables.PowerupType;
import io.github.annabeths.Colleges.College;
import io.github.annabeths.Colleges.EnemyCollege;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Level.GameMap;
import io.github.annabeths.Projectiles.Projectile;

public class PlayerBoatTest {

	public GameController gc;
	public PlayerBoat b;

	@BeforeAll
	public static void init() {
		Gdx.graphics = mock(Graphics.class);
		Gdx.input = mock(Input.class);
		when(Gdx.graphics.getWidth()).thenReturn(1280);
		when(Gdx.graphics.getHeight()).thenReturn(720);
	}

	@BeforeEach
	public void setup() {
		gc = mock(GameController.class);
		gc.map = mock(GameMap.class);
		gc.colleges = new ArrayList<College>();
		gc.physicsObjects = new ArrayList<PhysicsObject>();
		doCallRealMethod().when(gc).NewPhysicsObject(any(PhysicsObject.class));
		initColleges();
		gc.playerBoat = new PlayerBoat(gc, new Vector2(0, 0));
		when(gc.map.getMapHeight()).thenReturn(1000f);
		when(gc.map.getMapWidth()).thenReturn(1000f);
		doCallRealMethod().when(gc.map).getRandomPointInBounds();
		gc.map.camera = new OrthographicCamera();

		b = newBoat();
	}

	private void initColleges() {
		Vector2 pos = new Vector2(10, 10);
		EnemyCollege c = mock(EnemyCollege.class);

		doCallRealMethod().when(c).getLocalCenterX();
		doCallRealMethod().when(c).getLocalCenterY();
		doCallRealMethod().when(c).getX();
		doCallRealMethod().when(c).getY();
		doCallRealMethod().when(c).setCenter(any(Vector2.class));

		c.sprite = new Sprite();
		Polygon collisionPolygon = new Polygon(new float[] { 0, 0, 100, 0, 100, 100, 0, 100 });
		collisionPolygon.setPosition(pos.x, pos.y);
		c.setCenter(pos);
		c.collisionPolygon = collisionPolygon;
		gc.colleges.add(c);
	}

	private PlayerBoat newBoat() {
		PlayerBoat result = new PlayerBoat(gc, new Vector2(0, 0));
		return result;
	}

	@Test
	public void testReceivePowerup() {
		for (PowerupType p : PowerupType.values()) {
			b.receivePower(p);
			assertTrue(b.activePowerups.containsKey(p));
			assertEquals(p.getDefaultTime(), b.activePowerups.getOrDefault(p, -1f));
		}
	}

	@Test
	public void testUpdatePowerup() {
		// give all powerups
		for (PowerupType p : PowerupType.values()) {
			b.receivePower(p);
		}
		b.updatePowerups(1.5f); // should remove 1.5 seconds from all powerups

		for (PowerupType p : PowerupType.values()) {
			assertEquals(p.getDefaultTime() - 1.5f, b.activePowerups.getOrDefault(p, -1f));
		}
	}

	@Test
	public void testUpdatePowerupRemove() {
		PowerupType p = PowerupType.DAMAGE;
		b.receivePower(p);
		assertFalse(b.activePowerups.isEmpty());
		b.updatePowerups(p.getDefaultTime() / 2f); // skip half way through powerup
		assertFalse(b.activePowerups.isEmpty()); // powerup should still be present
		b.updatePowerups(p.getDefaultTime()); // skip past end of powerup
		assertTrue(b.activePowerups.isEmpty()); // powerup should be removed
	}

	@Test
	public void testIsInvincible() {
		assertFalse(b.isInvincible());
		b.receivePower(PowerupType.INVINCIBILITY);
		assertTrue(b.isInvincible());
	}

	@Test
	public void testGetDamageMul() throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException {
		float projDmgMul = PlayerBoat.class.getDeclaredField("projDmgMul").getFloat(b);
		assertEquals(projDmgMul, b.getDamageMul());
		// Ensure damage multiplier is greater with the damage powerup
		b.receivePower(PowerupType.DAMAGE);
		assertTrue(b.getDamageMul() > projDmgMul);
	}

	@Test
	public void testDraw() {
		assertDoesNotThrow(() -> b.Draw(mock(SpriteBatch.class)));
	}

	@Test
	public void testGetAngleBetweenMouseAndBoat() {
		b.setCenter(new Vector2(0, 0));
		when(Gdx.input.getX()).thenReturn(1280 / 2);
		when(Gdx.input.getY()).thenReturn(720 / 2 - 100); // point above the player
		assertEquals(90, b.getAngleBetweenMouseAndBoat());

		when(Gdx.input.getY()).thenReturn(720 / 2 + 100); // point below the player
		assertEquals(270, b.getAngleBetweenMouseAndBoat());

		when(Gdx.input.getX()).thenReturn(1280 / 2 - 100); // point left of the player
		when(Gdx.input.getY()).thenReturn(720 / 2);
		assertEquals(180, b.getAngleBetweenMouseAndBoat());
	}

	@Test
	public void testShootStockNormal() {
		b.shootStock(1);
		// Shoot 2 projectiles
		assertEquals(2, gc.physicsObjects.size());
		// Ensure projectiles are 180 degrees apart
		Projectile p1 = (Projectile) gc.physicsObjects.get(0);
		Projectile p2 = (Projectile) gc.physicsObjects.get(1);
		assertEquals(180, Math.abs(p1.rotation - p2.rotation));
	}

	@Test
	public void testShootStockBurst() {
		b.receivePower(PowerupType.STARBURSTFIRE);
		b.shootStock(1);
		// Shoot 8 projectiles
		assertEquals(8, gc.physicsObjects.size());
	}

	@Test
	public void testHeal() {
		float initHp = 50;
		b.HP = initHp;
		b.timeSinceLastHeal = 0;
		b.Heal(1, 1);
		assertEquals(initHp + 1, b.getHealth());

		b.HP = initHp;
		b.timeSinceLastHeal = 0;
		b.Heal(10, 1);
		assertEquals(initHp + 10, b.getHealth());

		// time since last heal has not reached >= 1 second
		b.HP = initHp;
		b.timeSinceLastHeal = 0;
		b.Heal(1, 0.5f);
		assertEquals(initHp, b.getHealth());
		// time since last heal is now >= 1 second
		b.Heal(1, 0.5f);
		assertEquals(initHp + 1, b.getHealth());

		// time * heal amount >= 1 health-seconds
		b.HP = initHp;
		b.timeSinceLastHeal = 0;
		b.Heal(2, 0.5f);
		assertEquals(initHp + 1, b.getHealth());
	}

}
