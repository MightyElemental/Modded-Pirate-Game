package io.github.annabeths.GameScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.annabeths.GeneralControl.eng1game;

public class Menu implements Screen {
	private SpriteBatch batch;
	BitmapFont font;
	GlyphLayout menuTextLayout;
	eng1game game;

	public Menu(eng1game g) {
		game = g;
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("fonts/bobcat.fnt"), false);
		// layouts can be used to manage text to allow it to be centered
		menuTextLayout = new GlyphLayout();
		menuTextLayout.setText(font, "press ENTER to goto game screen\npress ESCAPE to quit");
	}

	@Override
	public void render(float delta) {
		// do updates

		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			// if the ENTER key is pressed, switch to the game screen
			game.gotoScreen(Screens.gameScreen);
		} else if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		// do draws
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin(); // start batch
		font.getData().setScale(1);
		// the below line centers the text on the center of the screen
		font.draw(batch, menuTextLayout, Gdx.graphics.getWidth() / 2 - menuTextLayout.width / 2,
				Gdx.graphics.getHeight() / 2 + menuTextLayout.height / 2);
		batch.end(); // end batch
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
