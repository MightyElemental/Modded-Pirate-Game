package io.github.annabeths.GameScreens;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.annabeths.GeneralControl.Difficulty;
import io.github.annabeths.GeneralControl.TestHelper;
import io.github.annabeths.GeneralControl.eng1game;

public class GameDifScreenTest {

	GameDifScreen gds;
	eng1game game;
	Stage s;

	@BeforeAll
	public static void init() {
		TestHelper.setupEnv();
	}

	@BeforeEach
	public void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException {
		game = mock(eng1game.class);
		gds = mock(GameDifScreen.class,
				withSettings().useConstructor(game).defaultAnswer(CALLS_REAL_METHODS));

		s = mock(Stage.class);

		Field fs = GameDifScreen.class.getDeclaredField("stage");
		fs.setAccessible(true);
		fs.set(gds, s);
	}

	@Test
	public void testSetupButtons() {
		assertDoesNotThrow(() -> gds.setupButtons());
		TextButton[] btns = gds.getButtons();

		for (TextButton b : btns) {
			assertNotNull(b);
		}
	}

	@Test
	public void testSetupLabels() {
		assertDoesNotThrow(() -> gds.setupLabel());
		verify(s, times(1)).addActor(any(Label.class));
	}

	@Test
	public void testClickListener() {
		testSetupButtons(); // ensure buttons are set up

		TextButton[] buttons = gds.getButtons();

		ClickListener cl = (ClickListener) buttons[0].getListeners().get(1);
		cl.clicked(null, 0, 0);
		verify(game, times(1)).setDifficulty(eq(Difficulty.EASY));

		ClickListener cl2 = (ClickListener) buttons[1].getListeners().get(1);
		cl2.clicked(null, 0, 0);
		verify(game, times(1)).setDifficulty(eq(Difficulty.MEDIUM));

		ClickListener cl3 = (ClickListener) buttons[2].getListeners().get(1);
		cl3.clicked(null, 0, 0);
		verify(game, times(1)).setDifficulty(eq(Difficulty.HARD));
	}

	@Test
	public void testRender() {
		assertDoesNotThrow(() -> gds.render(1f));
		verify(s, times(1)).act();
		verify(s, times(1)).draw();
	}

	@Test
	public void testKeyPress() {
		testSetupButtons(); // ensure buttons are set up

		when(Gdx.input.isKeyJustPressed(Keys.E)).thenReturn(false);
		gds.render(1f);
		verify(game, never()).setDifficulty(Difficulty.EASY);
		verify(game, never()).gotoScreen(any());

		when(Gdx.input.isKeyJustPressed(Keys.E)).thenReturn(true);
		gds.render(1f);
		verify(game, times(1)).setDifficulty(Difficulty.EASY);
		verify(game, times(1)).gotoScreen(any());
	}

//	@Test
//	public void testEscKeyPress() {
//		testSetupButtons(); // ensure buttons are set up
//
//		when(Gdx.input.isKeyJustPressed(Keys.ESCAPE)).thenReturn(true);
//		gds.render(1f);
//		verify(game, times(1)).gotoScreen(Screens.menuScreen);
//	}

	@Test
	public void testDispose() {
		gds.dispose();
		verify(s, times(1)).dispose();
	}

}
