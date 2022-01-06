package io.github.annabeths;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class eng1game extends Game {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		/*batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");*/
		setScreen(new Splash());
	}

	@Override
	public void render () {
		/*ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();*/
		super.render();
	}
	
	@Override
	public void dispose () {
		/*batch.dispose();
		img.dispose();*/
		super.dispose();
	}
}
