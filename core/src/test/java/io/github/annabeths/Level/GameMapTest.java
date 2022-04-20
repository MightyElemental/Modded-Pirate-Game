package io.github.annabeths.Level;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.GeneralControl.TestHelper;
import io.github.annabeths.GeneralControl.eng1game;

public class GameMapTest {

	GameController gc;
	GameMap gm;

	@BeforeEach
	public void setup() {
		TestHelper.setupEnv();
		eng1game game = mock(eng1game.class);

		gc = mock(GameController.class,
				withSettings().useConstructor(game).defaultAnswer(CALLS_REAL_METHODS));
		gm = mock(GameMap.class,
				withSettings().useConstructor(gc).defaultAnswer(CALLS_REAL_METHODS));
	}

	@Test
	public void testUpdate() {
		assertDoesNotThrow(() -> gm.Update(1f));
	}

	@Test
	public void testDraw() {
		gm.bg = mock(WaterBackground.class);
		gm.Draw(mock(SpriteBatch.class));
		verify(gm.bg, times(1)).Draw(any(SpriteBatch.class));
	}

}
