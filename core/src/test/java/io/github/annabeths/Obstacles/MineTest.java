package io.github.annabeths.Obstacles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Collectables.PowerupType;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.GeneralControl.TestHelper;
import io.github.annabeths.GeneralControl.eng1game;
import io.github.annabeths.Projectiles.Projectile;

public class MineTest {

	GameController gc;
	eng1game game;
	Mine m;

	@BeforeAll
	public static void init() {
		TestHelper.setupEnv();
	}

	@BeforeEach
	public void setup() {
		game = mock(eng1game.class);
		gc = mock(GameController.class,
				withSettings().useConstructor(game).defaultAnswer(CALLS_REAL_METHODS));
		m = mock(Mine.class, withSettings().useConstructor(gc, new Vector2(0, 0))
				.defaultAnswer(CALLS_REAL_METHODS));
	}

	@Test
	public void testOnCollisionVulnerable() {
		gc.playerBoat.activePowerups.clear();
		m.OnCollision(gc.playerBoat);
		assertTrue(gc.playerBoat.getHealth() < gc.playerBoat.getMaxHealth());
		verify(m, times(1)).kill();
	}

	@Test
	public void testOnCollisionInvincible() {
		gc.playerBoat.activePowerups.put(PowerupType.INVINCIBILITY, 2f);
		m.OnCollision(gc.playerBoat);
		assertEquals(gc.playerBoat.getMaxHealth(), gc.playerBoat.getHealth());
		verify(m, times(1)).kill();
	}

	@Test
	public void testOnCollisionNonBoat() {
		m.OnCollision(mock(Projectile.class));
		verify(m, never()).kill();
	}

}
