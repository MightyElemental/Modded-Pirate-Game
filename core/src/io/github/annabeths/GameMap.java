package io.github.annabeths;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class GameMap extends GameObject{

    OrthographicCamera camera;
    PlayerBoat boat;
    SpriteBatch batch;
    Vector2 boundaries;
    
    
    public GameMap(int screenHeight, int screenWidth, PlayerBoat boat, SpriteBatch batch) {
        camera = new OrthographicCamera();
        camera.viewportHeight = screenHeight;
        camera.viewportWidth = screenWidth;
        this.boat = boat;
    }

    @Override
    public void Update(float delta) {
        Vector2 boatPos = boat.GetPosition();
        System.out.println(boatPos.x + "," + boatPos.y);
        camera.position.set(boat.sprite.getX() + boat.GetCenterX(), boat.sprite.getY() + boat.GetCenterY(), 0);
        camera.update();
    }

    public void CameraUpdate() {
        
    }


    @Override
    public void Draw(SpriteBatch batch) {
        
    }


}
