package io.github.annabeths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Splash implements Screen{
	private SpriteBatch batch;
	private Sprite splash;
	private Sound splashSound1 = Gdx.audio.newSound(Gdx.files.internal("audio/splash/ding.mp3"));
	private Sound splashSound2 = Gdx.audio.newSound(Gdx.files.internal("audio/splash/burp.wav"));
	public eng1game game;
	private boolean fading = false;

	public Splash(eng1game g)
	{
		game = g;
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub		
		batch = new SpriteBatch();
		
		Texture splashTexture = new Texture(Gdx.files.internal("mario/mario_0.png"));
		splash = new Sprite(splashTexture);
		splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		Timer.schedule(new Task(){
		    @Override
		    public void run() {
		        // Do your work
		    	splash = new Sprite(new Texture(Gdx.files.internal("mario/mario_1.png")));
		    	splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		    	splashSound1.play();
		    }
		}, 1f);
		
		Timer.schedule(new Task(){
		    @Override
		    public void run() {
		        // Where's my full marks Tommy
		    	splash = new Sprite(new Texture(Gdx.files.internal("mario/mario_3.png")));
		    	splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		    	splashSound2.play();
		    }
		}, 2.5f);
		
		Timer.schedule(new Task(){
		    @Override
		    public void run() {
		        // Do your work
		    	// Fade sprite out
		    	fading = true;
		    }
		}, 5f
		);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(1, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		splash.draw(batch);
		
		if(fading && splash.getColor().a > 0) splash.setAlpha(splash.getColor().a - (Gdx.graphics.getDeltaTime() / 2));
		else if(splash.getColor().a <= 0) { 
			game.gotoScreen(Screens.menuScreen);;
		}
		
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
