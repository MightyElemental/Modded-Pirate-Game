package io.github.annabeths.Boats;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.GeneralControl.TestHelper;
import io.github.annabeths.GeneralControl.eng1game;
import io.github.annabeths.Level.GameMap;

/**
 * This is required to ensure boats can be instantiated for other, future tests.
 */
public class GeneralBoatTests {

	private static GameController gc;

	@BeforeAll
	public static void init() {
		TestHelper.setupEnv();
	}

	@BeforeEach
	public void setup() {
		gc = mock(GameController.class, withSettings().useConstructor(mock(eng1game.class))
				.defaultAnswer(CALLS_REAL_METHODS));
		gc.map = mock(GameMap.class);
	}

	@Test
	public void testBoatInstantiation() {
		assertDoesNotThrow(() -> {
			new Boat(gc, new Vector2(0, 0), null) {

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
			new PlayerBoat(gc, new Vector2(0, 0));
		});
	}

	@Test
	public void testEnemyBoatInstantiation() {
		assertDoesNotThrow(() -> {
			new EnemyBoat(gc, new Vector2(0, 0));
		});
	}

	@Test
	public void testNeutralBoatInstantiation() {
		assertDoesNotThrow(() -> {
			new NeutralBoat(gc, new Vector2(0, 0));
		});
	}

}
