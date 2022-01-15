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
		// create a menu and game screen, then switch to a new splash screen
		menuScreen = new Menu(this);
		gameScreen = new GameController(this);
		gotoScreen(Screens.splashScreen);
	}
	
	// uses the Screens enum to change between any screen
	public void gotoScreen(Screens s)
	{
		switch(s){
			case splashScreen: //creates a new splash screen
				Splash splashScreen = new Splash(this);
				setScreen(splashScreen);
				break;
			case menuScreen: //switch back to the menu screen
				setScreen(menuScreen);
				break;
			case gameScreen: //switch back to the game screen
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

