package io.github.annabeths.Colleges;

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
import io.github.annabeths.Projectiles.ProjectileData;

public class GenericCollegeTests {

	GameController gc;

	@BeforeAll
	public static void init() {
		TestHelper.setupEnv();
	}

	@BeforeEach
	public void setup() {
		gc = mock(GameController.class, withSettings().useConstructor(mock(eng1game.class))
				.defaultAnswer(CALLS_REAL_METHODS));
	}

	@Test
	public void testCollegeInstantiation() {
		assertDoesNotThrow(() -> {
			new College() {

				@Override
				public void OnCollision(PhysicsObject other) {
				}
			};
		});
		assertDoesNotThrow(() -> {
			new College(new Vector2(0, 0), "img/world/castle/castle_dead.png",
					"img/world/castle/castle_dead.png", gc) {

				@Override
				public void OnCollision(PhysicsObject other) {
				}
			};
		});
	}

	@Test
	public void testEnemyCollegeInstantiation() {
		ProjectileData pd = ProjectileData.STOCK;
		assertDoesNotThrow(() -> {
			new EnemyCollege(new Vector2(0, 0), "img/world/castle/castle_dead.png",
					"img/world/castle/castle_dead.png", gc, pd, 100);
		});
	}

	@Test
	public void testPlayerCollegeInstantiation() {
		assertDoesNotThrow(() -> {
			new PlayerCollege(new Vector2(0, 0), "img/world/castle/castle_dead.png",
					"img/world/castle/castle_dead.png", gc, false);
		});
	}

}
