package io.github.annabeths.GameScreens;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.annabeths.GeneralControl.TestHelper;
import io.github.annabeths.GeneralControl.eng1game;

public class SplashTest {

	eng1game game;
	Splash s;
	Sprite sprite;
	SpriteBatch batch;

	@BeforeEach
	public void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException {
		TestHelper.setupEnv();

		game = mock(eng1game.class);
		s = mock(Splash.class,
				withSettings().useConstructor(game).defaultAnswer(CALLS_REAL_METHODS));

		batch = mock(SpriteBatch.class);
		Field f = Splash.class.getDeclaredField("batch");
		f.setAccessible(true);
		f.set(s, batch);

		sprite = mock(Sprite.class,
				withSettings().useConstructor().defaultAnswer(RETURNS_DEEP_STUBS));
		doCallRealMethod().when(sprite).setColor(anyFloat(), anyFloat(), anyFloat(), anyFloat());
		when(sprite.getColor()).thenCallRealMethod();
		doCallRealMethod().when(sprite).setAlpha(anyFloat());
		sprite.setColor(1f, 1f, 1f, 1f);

		Field f2 = Splash.class.getDeclaredField("splash");
		f2.setAccessible(true);
		f2.set(s, sprite);

		Field f3 = Splash.class.getDeclaredField("fading");
		f3.setAccessible(true);
		f3.set(s, true);

	}

	@Test
	public void testRender() {
		s.showShard = false;
		assertDoesNotThrow(() -> s.render(1f));

		verify(sprite, times(1)).draw(any());
	}

	@Test
	public void testFade() {
		s.showShard = false;
		s.render(0.5f);
		verify(sprite, times(1)).setAlpha(anyFloat());
		// ensure fade is not skipped half way through
		verify(game, never()).gotoScreen(any());
		// ensure alpha is reduced
		assertTrue(sprite.getColor().a < 1);

		sprite.setColor(1, 1, 1, 0);
		s.alpha = 0;
		s.render(1f);
		// ensure setAlpha is not called again
		verify(sprite, times(1)).setAlpha(anyFloat());
		// ensure the screen is changed when alpha is 0
		verify(game, times(1)).gotoScreen(any());
	}

}
