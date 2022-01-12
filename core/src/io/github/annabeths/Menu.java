package io.github.annabeths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Menu implements Screen {
	private SpriteBatch batch;
	BitmapFont font;
	GlyphLayout menuTextLayout;
	eng1game game;

	public Menu(eng1game g)
	{
		game = g;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("fonts/bobcat.fnt"), false);
		menuTextLayout = new GlyphLayout();
		menuTextLayout.setText(font, "press ENTER to goto game screen");
	}

	@Override
	public void render(float delta) {
		// do updates

		if(Gdx.input.isKeyPressed(Keys.ENTER))
		{
			game.gotoScreen(Screens.gameScreen);
		}

		//do draws
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		font.getData().setScale(1);
		font.draw(batch, menuTextLayout, Gdx.graphics.getWidth()/2 - menuTextLayout.width/2 ,Gdx.graphics.getHeight()/2 + menuTextLayout.height/2);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
