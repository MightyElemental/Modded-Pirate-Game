package io.github.annabeths.GameScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.annabeths.GeneralControl.eng1game;

public class GameOverScreen implements Screen {

    private SpriteBatch batch;
    BitmapFont font;
    GlyphLayout gameOverTextLayout;
    eng1game game;
    String text;

    public GameOverScreen(eng1game g, String text)
    {
        game = g;
        this.text = text;
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("fonts/bobcat.fnt"), false);
        gameOverTextLayout = new GlyphLayout();
        gameOverTextLayout.setText(font, text);
    }

    @Override
    public void render(float delta) {
		if(Gdx.input.isKeyJustPressed(Keys.R))
		{
			game.gotoScreen(Screens.gameScreen);
		}
        else if (Gdx.input.isKeyJustPressed(Keys.ENTER))
        {
            game.gotoScreen(Screens.menuScreen);
        }

		//do draws
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin(); //start batch
		font.getData().setScale(1);
		//the below line centres the text on the centre of the screen
		font.draw(batch, gameOverTextLayout, Gdx.graphics.getWidth()/2 - gameOverTextLayout.width/2 ,Gdx.graphics.getHeight()/2 + gameOverTextLayout.height/2);
		batch.end(); //end batch
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
