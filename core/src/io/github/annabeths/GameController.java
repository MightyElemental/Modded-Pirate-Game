package io.github.annabeths;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class GameController implements Screen {

    eng1game game;
    ArrayList<GameObject> gameObjects;
    ArrayList<PhysicsObject> physicsObjects;
    float testRot = 0;
    private SpriteBatch batch;
    private Sprite mario;

    public GameController(eng1game g){ //passes the game class so that we can change scene back later
        game = g;
        gameObjects = new ArrayList<GameObject>();
        physicsObjects = new ArrayList<PhysicsObject>();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        mario = new Sprite(new Texture(Gdx.files.internal("mario/yanderedev.jpg")));
        mario.setSize(50, 50);
        mario.setOrigin(25, 25);
        mario.setCenter(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
    }

    @Override
    public void render(float delta) {
        // do updates here
        testRot += 1;
        mario.setRotation(testRot);

        if(Gdx.input.isKeyJustPressed(Keys.SPACE))
        {
            System.out.println("aaaa");
            physicsObjects.add(new Projectile(mario.getX(), mario.getY(), mario.getRotation(), new Texture(Gdx.files.internal("mario/yanderedev.jpg"))));
        }

        if (physicsObjects.size() > 0)
        {
            for (PhysicsObject physicsObject : physicsObjects) {
                physicsObject.Update(delta);
            }
        }
        

        // do draws here
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin(); //begin the sprite batch

        mario.draw(batch); //draw a test sprite
        if (physicsObjects.size() > 0)
        {
            for (PhysicsObject physicsObject : physicsObjects) {
                physicsObject.Draw(batch);
            }
        }


        batch.end(); //end the sprite batch

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
