package io.github.annabeths.Colleges;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Level.GameMap;

public class PlayerCollegeTest {

	public static GameController gc;
	public PlayerCollege col;

	@BeforeAll
	public static void init() {
		gc = mock(GameController.class);
		gc.map = mock(GameMap.class);
		gc.colleges = new ArrayList<College>();
		gc.playerBoat = new PlayerBoat(gc, new Vector2(0, 0));
	}

	@BeforeEach
	public void setup() {
		col = new PlayerCollege(new Vector2(0, 0), "img/world/castle/castle_dead.png",
				"img/world/castle/castle_dead.png", gc, false);
	}

	@Test
	public void testDraw() {
		assertDoesNotThrow(() -> col.Draw(mock(SpriteBatch.class)));
	}

	@Test
	public void testUpdate() {
		assertDoesNotThrow(() -> col.Update(0.05f));
		gc.playerBoat.setCenter(new Vector2(600, 0));
		assertDoesNotThrow(() -> col.Update(0.05f));
	}

	@Test
	public void testOnCollision() {
		assertDoesNotThrow(() -> col.OnCollision(null));
	}

}
