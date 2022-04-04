package io.github.annabeths.Boats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Colleges.College;
import io.github.annabeths.Colleges.EnemyCollege;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Level.GameMap;
import io.github.annabeths.Projectiles.ProjectileData;

public class AIBoatTest {

	public GameController gc;
	public AIBoat b;

	@BeforeEach
	public void setup() {
		gc = mock(GameController.class);
		gc.map = mock(GameMap.class);
		gc.colleges = new ArrayList<College>();
		initColleges();
		gc.playerBoat = new PlayerBoat(gc, new Vector2(0, 0));

		// Create new generic boat
		b = newBoat();
	}

	private void initColleges() {
		Vector2 pos = new Vector2(500, 500);
		EnemyCollege c = mock(EnemyCollege.class,
				withSettings().useConstructor(pos, "", "", gc, ProjectileData.ENEMY, 100)
						.defaultAnswer(CALLS_REAL_METHODS));
		gc.colleges.add(c);
	}

	private AIBoat newBoat() {
		AIBoat result = mock(AIBoat.class, withSettings().defaultAnswer(CALLS_REAL_METHODS)
				.useConstructor(gc, new Vector2(0, 0), ""));
		result.maxHP = 100;
		result.HP = result.maxHP;
		result.turnSpeed = 1;
		result.speed = 1;
		return result;
	}

	@Test
	public void testGetAngleToDestNull() {
		b.destination = null;
		assertEquals(-1, b.getAngleToDest());
	}

	@Test
	public void testGetAngleToDest() {
		b.setCenter(new Vector2(0, 0));
		b.destination = new Vector2(100, 100);
		assertEquals(45, b.getAngleToDest());

		for (int x = 0; x < 100; x += 5) {
			for (int y = 0; y < 100; y += 5) {
				b.destination = new Vector2(x, y);
				float angle = (float) Math.atan2(b.destination.y, b.destination.x)
						* MathUtils.radiansToDegrees;
				assertEquals(angle, b.getAngleToDest());
			}
		}
	}

	@Test
	public void testSetDestination() {
		Vector2 prev = b.position;
		b.SetDestination(new Vector2(1337, 100));
		assertEquals(new Vector2(1337, 100), b.GetDestination());
		assertEquals(prev, b.initialPosition);
	}

	@Test
	public void testMoveToDestinationInBound() {
		// point is in bounds
		b.setCenter(new Vector2(0, 0));
		b.rotation = 45;
		b.SetDestination(new Vector2(100, 100));
		b.MoveToDestination((float) Math.sqrt(2));

		assertEquals(new Vector2(1, 1), b.getCenter());
	}

	@Test
	public void testMoveToDestinationOutBound() {
		// point is not in bounds
		b.setCenter(new Vector2(0, 0));
		b.SetDestination(new Vector2(-100, -100));
		b.MoveToDestination(1);

		assertEquals(new Vector2(0, 0), b.getCenter()); // should not move
	}

	@Test
	public void testIsDestValidInvalid() {
		Vector2 target = new Vector2(550, 550);
		b.setCenter(new Vector2(0, 0));
		assertFalse(b.isDestValid(target));
	}

	@Test
	public void testIsDestValidValid() {
		Vector2 target = new Vector2(10, 0);
		b.setCenter(new Vector2(0, 0));
		assertTrue(b.isDestValid(target));
	}

	/**
	 * Only has to return a non-null point. Validation testing is done in
	 * {@link AIBoat#isDestValid(Vector2)}.
	 */
	@Test
	public void testGetNewRandomValidTarget() {
		assertNotNull(b.getNewRandomValidTarget());
	}

	@Test
	public void testUpdateDestination() {
		b.SetDestination(new Vector2(0, 0));
		Vector2 old = b.GetDestination();
		b.destinationThreshold = 0; // ensure update does not happen
		b.updateDestination();
		assertEquals(old, b.GetDestination());

		b.destinationThreshold = 100000; // ensure boat is within threshold to force update
		b.updateDestination();
		assertNotEquals(old, b.GetDestination());
	}

	@Test
	public void testGetDestinationThreshold() {
		b.destinationThreshold = 1337;
		assertEquals(1337, b.getDestinationThreshold());
	}

	@Test
	public void testUpdateHP() {
		// Update method should destroy boat if HP is zero
		b.Update(1);
		assertFalse(b.isDead());
		b.HP = 0;
		b.Update(1);
		assertTrue(b.isDead());
	}

	@Test
	public void testIdle() {
		b.destination = null;
		doNothing().when(b).SetDestination(any(Vector2.class));
		b.idle(1);
		verify(b, times(1)).SetDestination(any(Vector2.class));

		b.destination = mock(Vector2.class);
		doNothing().when(b).updateDestination();
		b.idle(1);
		verify(b, times(1)).updateDestination();
	}

}
