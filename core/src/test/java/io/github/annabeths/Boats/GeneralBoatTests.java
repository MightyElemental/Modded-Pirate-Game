package io.github.annabeths.Boats;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Colleges.College;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Level.GameMap;

/**
 * This is required to ensure boats can be instantiated for other, future tests.
 */
public class GeneralBoatTests {

	private static GameController gc;

	@BeforeAll
	public static void init() {
		gc = mock(GameController.class);
		gc.map = mock(GameMap.class);
		gc.colleges = new ArrayList<College>();
	}

	@Test
	public void testBoatInstantiation() {
		assertDoesNotThrow(() -> {
			new Boat(gc, Vector2.Zero, null) {

				@Override
				public void Update(float delta) {
				}

				@Override
				void Shoot() {
				}

				@Override
				void Destroy() {
				}

				@Override
				public void OnCollision(PhysicsObject other) {
				}
			};
		});
	}

	@Test
	public void testPlayerBoatInstantiation() {
		assertDoesNotThrow(() -> {
			new PlayerBoat(gc, Vector2.Zero);
		});
	}

	@Test
	public void testEnemyBoatInstantiation() {
		assertDoesNotThrow(() -> {
			new EnemyBoat(gc, Vector2.Zero);
		});
	}

	@Test
	public void testNeutralBoatInstantiation() {
		assertDoesNotThrow(() -> {
			new NeutralBoat(gc, Vector2.Zero);
		});
	}

}
