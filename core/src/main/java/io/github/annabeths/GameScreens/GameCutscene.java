package io.github.annabeths.GameScreens;

import java.io.FileNotFoundException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;

import io.github.annabeths.GeneralControl.eng1game;

/**
 * Cutscene video to the game
 * 
 * @author MightyElemental
 * @tt.updated Nikocado Mod
 */
public class GameCutscene implements Screen {

	private SpriteBatch	batch;
	public eng1game		game;

	private VideoPlayer	vPlayer;
	private FileHandle	cutsceneLocation;

	/**
	 * Constructor for Splash
	 * 
	 * @param g reference to eng1game
	 */
	public GameCutscene( eng1game g, FileHandle cutsceneLocation ) {
		game = g;
		this.cutsceneLocation = cutsceneLocation;
	}

	/**
	 * Called when the screen is created.
	 */
	@Override
	public void show() {
		batch = new SpriteBatch();

		vPlayer = VideoPlayerCreator.createVideoPlayer();
		try {
			vPlayer.play(cutsceneLocation);
			vPlayer.setVolume(1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		vPlayer.setOnCompletionListener(v -> {
			game.gotoScreen(Screens.gameScreen);
		});
	}

	float alpha = 1;

	/**
	 * Play the video
	 * 
	 * @param delta time since the last frame
	 */
	@Override
	public void render( float delta ) {

		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			vPlayer.setVolume(0);
			game.gotoScreen(Screens.gameScreen);
		}

		ScreenUtils.clear(Color.BLACK);

		batch.begin();

		vPlayer.update();
		batch.draw(vPlayer.getTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		batch.end();
	}

	/**
	 * Resize the window
	 * 
	 * @param width new width
	 * @param height new height
	 */
	@Override
	public void resize( int width, int height ) {
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
		vPlayer.dispose();
	}

}
