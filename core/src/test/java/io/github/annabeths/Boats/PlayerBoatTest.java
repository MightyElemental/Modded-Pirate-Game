package io.github.annabeths.Boats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Collectables.PowerupType;
import io.github.annabeths.Colleges.College;
import io.github.annabeths.Colleges.EnemyCollege;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameGenerics.Upgrades;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Level.GameMap;
import io.github.annabeths.Projectiles.Projectile;
import io.github.annabeths.Projectiles.ProjectileData;
import io.github.annabeths.Projectiles.ProjectileRay;
import io.github.annabeths.UI.HUD;

public class PlayerBoatTest {

	public GameController gc;
	public PlayerBoat b;

	@BeforeEach
	public void setup() {
		setupInput();

		gc = mock(GameController.class);
		gc.map = mock(GameMap.class);
		gc.colleges = new ArrayList<College>();
		gc.physicsObjects = new ArrayList<PhysicsObject>();
		gc.rays = new ArrayList<ProjectileRay>();
		doCallRealMethod().when(gc).NewPhysicsObject(any(PhysicsObject.class));
		setupColleges();
		gc.playerBoat = new PlayerBoat(gc, new Vector2(0, 0));
		gc.camera = new OrthographicCamera();

		gc.hud = mock(HUD.class);

		b = newBoat();
	}

	private void setupInput() {
		Gdx.input = mock(Input.class);
		when(Gdx.input.isKeyPressed(Input.Keys.W)).thenReturn(false);
		when(Gdx.input.isKeyPressed(Input.Keys.S)).thenReturn(false);
		when(Gdx.input.isKeyPressed(Input.Keys.A)).thenReturn(false);
		when(Gdx.input.isKeyPressed(Input.Keys.D)).thenReturn(false);

		Gdx.graphics = mock(Graphics.class);
		when(Gdx.graphics.getWidth()).thenReturn(1280);
		when(Gdx.graphics.getHeight()).thenReturn(720);
	}

	private void setupColleges() {
		Vector2 pos = new Vector2(500, 500);
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
		// gc.colleges.add(c);
	}

	private PlayerBoat newBoat() {
		PlayerBoat result = mock(PlayerBoat.class, withSettings()
				.useConstructor(gc, new Vector2(0, 0)).defaultAnswer(CALLS_REAL_METHODS));
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

	@Test
	public void testProcessInputTurn() {
		float rotation = 180;

		// Should not rotate
		b.rotation = rotation;
		b.processInput(1);
		assertEquals(rotation, b.rotation);

		// Rotate right
		when(Gdx.input.isKeyPressed(Input.Keys.D)).thenReturn(true);
		b.rotation = rotation;
		b.processInput(1);
		assertTrue(b.rotation < rotation);

		// Both inputs pressed
		when(Gdx.input.isKeyPressed(Input.Keys.A)).thenReturn(true);
		when(Gdx.input.isKeyPressed(Input.Keys.D)).thenReturn(true);
		b.rotation = rotation;
		b.processInput(1);
		assertEquals(rotation, b.rotation);

		// Rotate left
		when(Gdx.input.isKeyPressed(Input.Keys.A)).thenReturn(true);
		when(Gdx.input.isKeyPressed(Input.Keys.D)).thenReturn(false);
		b.rotation = rotation;
		b.processInput(1);
		assertTrue(b.rotation > rotation);
	}

	@Test
	public void testProcessInputMoveNoInput() {
		// should not move
		b.setCenter(new Vector2(10, 10));
		b.rotation = 45;
		Vector2 pos = b.getCenter();
		b.processInput(1);
		assertEquals(0, b.getCenter().dst(pos));
	}

	@Test
	public void testProcessInputMove() {
		when(Gdx.input.isKeyPressed(Input.Keys.W)).thenReturn(true);

		// Should move
		b.setCenter(new Vector2(10, 10));
		Vector2 pos = b.getCenter();
		b.processInput(1f);
		float dist = b.getCenter().dst(pos);
		assertNotEquals(0, dist);

		// Assert boat is faster with speed powerup
		b.receivePower(PowerupType.SPEED);
		b.setCenter(new Vector2(10, 10));
		b.processInput(1f);
		assertTrue(b.getCenter().dst(pos) > dist);
	}

	@Test
	public void testProcessInputClick() {
		when(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)).thenReturn(true);
		gc.hud.hoveringOverButton = false;
		b.timeSinceLastShot = 10;
		b.processInput(1f);
		// Boat will shoot
		assertEquals(0, b.timeSinceLastShot);

		gc.hud.hoveringOverButton = true;
		b.timeSinceLastShot = 10;
		b.processInput(1f);
		// Boat will not shoot
		assertNotEquals(0, b.timeSinceLastShot);
	}

	@Test
	public void testProcessInputSpace() {
		when(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)).thenReturn(true);
		b.timeSinceLastShot = 10;
		b.processInput(1f);
		// Boat will shoot
		assertEquals(0, b.timeSinceLastShot);

		when(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)).thenReturn(false);
		b.timeSinceLastShot = 10;
		b.processInput(1f);
		// Boat will not shoot
		assertNotEquals(0, b.timeSinceLastShot);

		/* Should not shoot */

		when(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)).thenReturn(true);
		b.timeSinceLastShot = 0.05f;
		b.processInput(1f);
		assertNotEquals(0, b.timeSinceLastShot);

		when(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)).thenReturn(false);
		b.timeSinceLastShot = 0.05f;
		b.processInput(1f);
		assertNotEquals(0, b.timeSinceLastShot);
	}

	@Test
	public void testShootRay() {
		when(Gdx.input.getX()).thenReturn(1280 / 2);
		when(Gdx.input.getY()).thenReturn(720 / 2 - 100);
		b.shootRay(1);
		assertEquals(1, gc.rays.size());
	}

	@Test
	public void testShootTypes() {
		b.activeProjectileType = ProjectileData.STOCK;
		b.Shoot();
		verify(b, times(1)).shootStock(any(Float.class));

		b.activeProjectileType = ProjectileData.RAY;
		b.Shoot();
		verify(b, times(1)).shootRay(any(Float.class));
	}

	@Test
	public void testOnCollisionPlayerProjectile() {
		Projectile p = new Projectile(new Vector2(0, 0), 0, ProjectileData.STOCK, true);
		b.HP = 100;
		b.OnCollision(p);
		assertFalse(p.removeOnNextTick());
		assertEquals(100, b.getHealth());
	}

	@Test
	public void testOnCollisionProjectile() {
		Projectile p = new Projectile(new Vector2(0, 0), 0, ProjectileData.STOCK, false);
		b.HP = 100;
		b.OnCollision(p);
		assertTrue(p.removeOnNextTick());
		assertEquals(100 - ProjectileData.STOCK.getDamage() + b.defense, b.getHealth());
	}

	@Test
	public void testOnCollisionBoat() {
		NeutralBoat nb = new NeutralBoat(gc, new Vector2(0, 0));
		b.HP = 100;
		b.OnCollision(nb);
		assertEquals(100 - 50 + b.defense, b.getHealth());
		nb.OnCollision(b);
		assertTrue(nb.removeOnNextTick());
	}

	@Test
	public void testOnCollisionBoatInvincible() {
		b.receivePower(PowerupType.INVINCIBILITY);
		NeutralBoat nb = new NeutralBoat(gc, new Vector2(0, 0));
		b.HP = 100;
		b.OnCollision(nb);
		assertEquals(100, b.getHealth());
		nb.OnCollision(b);
		assertTrue(nb.removeOnNextTick());
	}

	@Test
	public void testOnCollisionCollege() {
		EnemyCollege c = mock(EnemyCollege.class);
		b.OnCollision(c);
		verify(gc, times(1)).gameOver();
	}

	@Test
	public void testUpdate() {
		b.Update(1f);

		verify(b, times(1)).updatePowerups(any(Float.class));
		verify(b, times(1)).processInput(any(Float.class));
	}

	@Test
	public void testUpdateRapidFire() {
		float t = b.timeSinceLastShot;
		b.Update(0.05f);
		float newTime = b.timeSinceLastShot;
		assertTrue(newTime > t);

		// ensure rapid fire results in faster shot recharging
		b.receivePower(PowerupType.RAPIDFIRE);
		float t2 = b.timeSinceLastShot;
		b.Update(0.05f);
		float newTime2 = b.timeSinceLastShot;
		assertTrue(newTime2 - t2 > newTime - t);
	}

	@Test
	public void testUpdateEndgameAlive() {
		b.Update(1f);
		// game should not end if player is alive
		verify(gc, times(0)).gameOver();
	}

	@Test
	public void testUpdateEndgameDead() {
		b.HP = -1;
		b.Update(1f);
		// game should end if player is dead
		verify(gc, times(1)).gameOver();
	}

	@Test
	public void testDestroy() {
		b.Destroy();
		// game should end if player is destroyed
		verify(gc, times(1)).gameOver();
	}

	@Test
	public void testUpgrade() {
		float upgrade = b.defense;
		b.Upgrade(Upgrades.defense, 1);
		assertTrue(b.defense > upgrade);

		b.HP /= 2; // harm player so HP upgrade can be tested
		upgrade = b.HP;
		b.Upgrade(Upgrades.health, 1);
		assertTrue(b.HP > upgrade);

		upgrade = b.maxHP;
		b.Upgrade(Upgrades.maxhealth, 1);
		assertTrue(b.maxHP > upgrade);

		upgrade = b.projDmgMul;
		b.Upgrade(Upgrades.projectiledamage, 1);
		assertTrue(b.projDmgMul > upgrade);

		upgrade = b.projSpdMul;
		b.Upgrade(Upgrades.projectilespeed, 1);
		assertTrue(b.projSpdMul > upgrade);

		upgrade = b.speed;
		b.Upgrade(Upgrades.speed, 1);
		assertTrue(b.speed > upgrade);

		upgrade = b.turnSpeed;
		b.Upgrade(Upgrades.turnspeed, 1);
		assertTrue(b.turnSpeed > upgrade);
	}

}
