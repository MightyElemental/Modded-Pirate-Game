package io.github.annabeths;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class eng1game extends Game {
	SpriteBatch batch;
	Texture img;
	Menu menuScreen;
	GameController gameScreen;
	
	@Override
	public void create () {
		menuScreen = new Menu(this);
		gameScreen = new GameController(this);
		gotoScreen(Screens.splashScreen);
	}
	
	public void gotoScreen(Screens s)
	{
		switch(s){
			case splashScreen:
				Splash splashScreen = new Splash(this);
				setScreen(splashScreen);
				break;
			case menuScreen:
				setScreen(menuScreen);
				break;
			case gameScreen:
				setScreen(gameScreen);
				break;
		}
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

