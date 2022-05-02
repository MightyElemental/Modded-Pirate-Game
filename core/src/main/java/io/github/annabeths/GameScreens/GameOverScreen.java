package io.github.annabeths.GameScreens;

import static io.github.annabeths.GeneralControl.ResourceManager.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.annabeths.GeneralControl.eng1game;

/**
 * Screen that is shown when the player gets a game over (i.e runs out of health.)
 * @author James Burnell
 * @author Annabeth
 * @tt.updated Assessment 2
 */
public class GameOverScreen implements Screen {

	private SpriteBatch batch;
	GlyphLayout loseText;
	GlyphLayout gameOverText;
	GlyphLayout scoreText;
	eng1game game;
	String text;

	/**
	 * Constructor for GameOverScreen
	 * @param g reference to eng1game
	 * @param text string; forms part of the text displayed on screen
	 */
	public GameOverScreen(eng1game g, String text) {
		game = g;
		this.text = text;

		font.setColor(Color.WHITE);
		font.getData().setScale(1);
		gameOverText = new GlyphLayout(font, text);
		scoreText = new GlyphLayout(font, "Your final score was " + g.gameScore);
		font.setColor(Color.RED);
		font.getData().setScale(2);
		loseText = new GlyphLayout(font, "-= YOU LOSE =-");
		font.setColor(Color.WHITE);
	}

	/**
	 * called when the screen is created.
	 */
	@Override
	public void show() {
		batch = new SpriteBatch();
	}

	/**
	 * Draw the text to the screen. Called once per frame
	 * @param delta time since the last frame
	 */
	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyJustPressed(Keys.R)) {
			game.gotoScreen(Screens.gameScreen);
		}
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			game.gotoScreen(Screens.menuScreen);
		}
		if(Gdx.input.isKeyJustPressed(Keys.L)){
			game.goToLoadScreen();
		}

		// do draws
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		font.getData().setScale(1);
		// the below line centers the text in the center of the screen
		font.draw(batch, gameOverText, Gdx.graphics.getWidth() / 2f - gameOverText.width / 2,
				Gdx.graphics.getHeight() / 2f + gameOverText.height / 2);
		font.draw(batch, scoreText, (Gdx.graphics.getWidth() - scoreText.width) / 2,
				(Gdx.graphics.getHeight() - scoreText.height) / 2 - 100);
		font.getData().setScale(2);
		font.draw(batch, loseText, (Gdx.graphics.getWidth() - loseText.width) / 2,
				(Gdx.graphics.getHeight() - loseText.height) / 2 + 150);
		font.getData().setScale(1);

		batch.end();
	}

	/**
	 * resize the window
	 * @param width new width
	 * @param height new height
	 */
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
