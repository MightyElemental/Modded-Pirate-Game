package io.github.annabeths.Colleges;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.FriendlyBoat;
import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.GeneralControl.ResourceManager;
import io.github.annabeths.Level.GameMap;

public class PlayerCollegeTest {

	public GameController gc;
	public PlayerCollege col;

	@BeforeEach
	public void setup() {
		gc = mock(GameController.class);
		gc.map = mock(GameMap.class);
		gc.colleges = new ArrayList<College>();
		gc.physicsObjects = new ArrayList<PhysicsObject>();

		doCallRealMethod().when(gc).NewPhysicsObject(any(PhysicsObject.class));

		col = mock(PlayerCollege.class,
				withSettings().useConstructor(new Vector2(0, 0), "", "", gc, false)
						.defaultAnswer(CALLS_REAL_METHODS));

		gc.playerBoat = mock(PlayerBoat.class, withSettings().useConstructor(gc, new Vector2(0, 0))
				.defaultAnswer(CALLS_REAL_METHODS));

		ResourceManager.font = mock(BitmapFont.class);
	}

	@Test
	public void testDraw() {
		col.splashText = mock(GlyphLayout.class);
		assertDoesNotThrow(() -> col.Draw(mock(SpriteBatch.class)));
		verify(ResourceManager.font, times(1)).draw(any(SpriteBatch.class), any(GlyphLayout.class),
				anyFloat(), anyFloat());
	}

	@Test
	public void testUpdateHeal() {
		assertTrue(col.isInRange(gc.playerBoat)); // ensure player is in bounds
		col.Update(0);
		verify(gc.playerBoat, times(1)).Heal(anyInt(), anyFloat());
	}

	@Test
	public void testBoatSpawn() {
		col.timeSinceLastSpawn = col.boatSpawnTime;
		col.checkForSpawnFriendlyBoat(1);
		assertTrue(gc.physicsObjects.stream().anyMatch(c -> c instanceof FriendlyBoat));
	}

	@Test
	public void testUpdate() {
		col.splashText = mock(GlyphLayout.class);
		assertDoesNotThrow(() -> col.Update(0.05f));
		gc.playerBoat.setCenter(new Vector2(600, 0));
		assertDoesNotThrow(() -> col.Update(0.05f));
	}

	@Test
	public void testOnCollision() {
		assertDoesNotThrow(() -> col.OnCollision(null));
	}

	@Test
	public void testUpdateSplashText() {
		col.splashText = mock(GlyphLayout.class);
		col.updateSplashText("");
		verify(col.splashText, times(1)).setText(ResourceManager.font, "");
	}

}
