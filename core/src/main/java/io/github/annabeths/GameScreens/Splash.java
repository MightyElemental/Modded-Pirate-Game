package io.github.annabeths.GameScreens;

import java.io.FileNotFoundException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;

import io.github.annabeths.GeneralControl.eng1game;

/**
 * Intro video. First screen that is shown to the player.
 * @author James Burnell
 * @author Leif Kemp
 * @tt.updated Assessment 2
 */
public class Splash implements Screen {

	private SpriteBatch batch;
	private Sprite splash;
	private final Sound shardSound = Gdx.audio.newSound(Gdx.files.internal("audio/splash/bruh.mp3"));
	private final Sound marioSound1 = Gdx.audio.newSound(Gdx.files.internal("audio/splash/ding.mp3"));
	private final Sound marioSound2 = Gdx.audio.newSound(Gdx.files.internal("audio/splash/burp.wav"));
	public eng1game game;
	public boolean fading = false;
	public boolean showShard = true;

	public boolean canSkip = false;

	private Texture mario1, mario2;

	private VideoPlayer vPlayer;

	/**
	 * Constructor for Splash
	 * @param g reference to eng1game
	 */
	public Splash(eng1game g) {
		game = g;
	}

	/**
	 * Called when the screen is created.
	 */
	@Override
	public void show() {
		batch = new SpriteBatch();

		Texture splashTexture = new Texture(Gdx.files.internal("mario/mario_0.png"));
		mario1 = new Texture(Gdx.files.internal("mario/mario_1.png"));
		mario2 = new Texture(Gdx.files.internal("mario/mario_3.png"));
		splash = new Sprite(splashTexture);
		splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		vPlayer = VideoPlayerCreator.createVideoPlayer();
		try {
			vPlayer.play(Gdx.files.internal("mario/shardlogo.webm"));
			vPlayer.setVolume(1);
			scheduleTask(() -> {
				//shardSound.play();
				canSkip = true;
			}, 1f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		vPlayer.setOnCompletionListener(v -> scheduleTask(() -> {
			showShard = false;

			scheduleTask(() -> {
				splash = new Sprite(mario1);
				splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				marioSound1.play();
			}, 0.5f);

			scheduleTask(() -> {
				splash = new Sprite(mario2);
				splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				marioSound2.play();
			}, 2.5f);

			scheduleTask(() -> fading = true, 5f);
		}, 1f));
	}

	/**
	 * A wrapper method for {@link Timer#scheduleTask(Task, float)} to make code
	 * cleaner.
	 * 
	 * @param r the code to run
	 * @param time in seconds to wait before running code
	 */
	private void scheduleTask(Runnable r, float time) {
		Timer.schedule(new Task() {
			@Override
			public void run() {
				r.run();
			}
		}, time);
	}

	float alpha = 1;

	/**
	 * Play the video
	 * @param delta time since the last frame
	 */
	@Override
	public void render(float delta) {

		if (Gdx.input.isKeyJustPressed(Keys.SPACE) && canSkip) {
			shardSound.stop();
			marioSound1.stop();
			marioSound2.stop();
			vPlayer.setVolume(0);
			game.gotoScreen(Screens.menuScreen);
		}

		ScreenUtils.clear(Color.BLACK);

		batch.begin();

		if (showShard) {
			vPlayer.update();
			batch.draw(vPlayer.getTexture(), 0, 0, Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight());
		} else {
			splash.draw(batch);
		}

		if (fading && alpha > 0) {
			alpha -= (delta / 2);
			splash.setAlpha(alpha < 0 ? 0 : alpha);
		} else if (alpha <= 0) {
			game.gotoScreen(Screens.menuScreen);
		}

		batch.end();
	}

	/**
	 * Resize the window
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
