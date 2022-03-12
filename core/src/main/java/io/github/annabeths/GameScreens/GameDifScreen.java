package io.github.annabeths.GameScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.annabeths.GeneralControl.eng1game;
import io.github.annabeths.UI.DifficultySelectionButtons;

public class GameDifScreen implements Screen {

	private SpriteBatch batch;
	eng1game game;
	DifficultySelectionButtons dif_sel_button;

	public GameDifScreen(eng1game g) {
		batch = new SpriteBatch();
		dif_sel_button = new DifficultySelectionButtons();
		game = g;
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		boolean Easy_click = dif_sel_button.is_easy();
		boolean Middle_click = dif_sel_button.is_middle();
		boolean Hard_click = dif_sel_button.is_hard();
		boolean Esc_click = dif_sel_button.is_esc();

		if (Easy_click) {
			game.gotoScreen(Screens.gameScreen);
		}
		if (Middle_click) {
			game.gotoScreen(Screens.gameScreen);
		}
		if (Hard_click) {
			game.gotoScreen(Screens.gameScreen);
		}
		if (Esc_click) {
			game.gotoScreen(Screens.menuScreen);
		}

		// do draws
		Gdx.gl.glClearColor(0, 0, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		// the below line centers the text on the center of the screen
		dif_sel_button.Draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}

}
