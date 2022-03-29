package io.github.annabeths.Colleges;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.NeutralBoat;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Level.GameMap;

public class CollegeTest {

	public static GameController gc;
	public College col;
	public NeutralBoat nb;

	@BeforeAll
	public static void init() {
		gc = mock(GameController.class);
		gc.map = mock(GameMap.class);
		gc.colleges = new ArrayList<College>();
	}

	@BeforeEach
	public void setup() {
		col = new College(new Vector2(0, 0), "img/world/castle/castle_dead.png",
				"img/world/castle/castle_dead.png", null) {

			@Override
			public void OnCollision(PhysicsObject other) {
			}
		};
		col.maxHP = 100;
		col.HP = 100;
		col.setRange(500);
		nb = new NeutralBoat(gc, new Vector2(0, 0));
	}

	@Test
	public void testIsInRange() {
		assertTrue(col.isInRange(nb));
		nb.setCenter(new Vector2(600, 0));
		assertFalse(col.isInRange(nb));
	}

	@Test
	public void testSetGetRange() {
		for (int i = 0; i < 10; i += 13) {
			col.setRange(i);
			assertEquals(i, col.getRange());
		}
	}

	@Test
	public void testSetGetDamage() {
		for (int i = 0; i < 10; i += 13) {
			col.setDamage(i);
			assertEquals(i, col.getDamage());
		}
	}

	@Test
	public void testSetGetFireRate() {
		for (int i = 0; i < 10; i += 13) {
			col.setFireRate(i);
			assertEquals(i, col.getFireRate());
		}
	}

	@Test
	public void testSetGetInvulnerable() {
		col.setInvulnerable(true);
		assertTrue(col.isInvulnerable());
		col.setInvulnerable(false);
		assertFalse(col.isInvulnerable());
	}

	@Test
	public void testHealth() {
		assertEquals(100, col.getHealth());
		assertEquals(100, col.getMaxHealth());
	}

	@Test
	public void testDamage() {
		col.damage(50);
		assertEquals(50, col.getHealth());
	}

	@Test
	public void testDamageClamp() {
		col.damage(150); // damage more than max health
		assertEquals(0, col.getHealth());
	}

}
