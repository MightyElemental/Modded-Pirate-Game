package io.github.annabeths.GameScreens;

import static io.github.annabeths.GeneralControl.ResourceManager.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.annabeths.GeneralControl.eng1game;

/**
 * @author James Burnell
 * @author Annabeth
 * @tt.updated Assessment 2
 */
public class GameWinScreen implements Screen {

	private SpriteBatch batch;
	GlyphLayout winText;
	GlyphLayout gameOverText;
	GlyphLayout scoreText;
	eng1game game;

	public GameWinScreen(eng1game g) {
		game = g;

		font.setColor(Color.WHITE);
		font.getData().setScale(1);
		gameOverText = new GlyphLayout(font, "Press ENTER to return to the menu");
		scoreText = new GlyphLayout(font, "Your final score was " + g.gameScore);
		font.setColor(Color.FOREST);
		font.getData().setScale(2);
		winText = new GlyphLayout(font, "-= YOU WIN =-");
		font.setColor(Color.WHITE);
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			game.gotoScreen(Screens.menuScreen);
		}

		// do draws
		ScreenUtils.clear(Color.BLACK);

		batch.begin();
		font.getData().setScale(1);
		// the below line centers the text on the center of the screen
		font.draw(batch, gameOverText, Gdx.graphics.getWidth() / 2 - gameOverText.width / 2,
				Gdx.graphics.getHeight() / 2 + gameOverText.height / 2);
		font.draw(batch, scoreText, (Gdx.graphics.getWidth() - scoreText.width) / 2,
				(Gdx.graphics.getHeight() - scoreText.height) / 2 - 100);
		font.getData().setScale(2);
		font.draw(batch, winText, (Gdx.graphics.getWidth() - winText.width) / 2,
				(Gdx.graphics.getHeight() - winText.height) / 2 + 150);
		font.getData().setScale(1);

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
