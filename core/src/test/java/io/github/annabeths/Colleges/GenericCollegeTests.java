package io.github.annabeths.Colleges;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.Projectiles.ProjectileData;

public class GenericCollegeTests {

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
			new College(Vector2.Zero, "img/world/castle/castle_dead.png",
					"img/world/castle/castle_dead.png", null) {

				@Override
				public void OnCollision(PhysicsObject other) {
				}
			};
		});
	}

	@Test
	public void testEnemyCollegeInstantiation() {
		ProjectileData pd = mock(ProjectileData.class);
		assertDoesNotThrow(() -> {
			new EnemyCollege(Vector2.Zero, "img/world/castle/castle_dead.png",
					"img/world/castle/castle_dead.png", null, pd, 100);
		});
	}

	@Test
	public void testPlayerCollegeInstantiation() {
		assertDoesNotThrow(() -> {
			new PlayerCollege(Vector2.Zero, "img/world/castle/castle_dead.png",
					"img/world/castle/castle_dead.png", null);
		});
	}

}
