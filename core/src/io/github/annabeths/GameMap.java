package io.github.annabeths;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


public class GameMap extends GameObject{

    OrthographicCamera camera;
    PlayerBoat boat;
    Vector2 boundaries;
    WaterBackground bg;
    
    
    public GameMap(int screenHeight, int screenWidth, PlayerBoat boat, SpriteBatch batch, int mapWidth, int mapHeight) {
        camera = new OrthographicCamera();
        camera.viewportHeight = screenHeight;
        camera.viewportWidth = screenWidth;
        this.boat = boat;
        bg = new WaterBackground(mapWidth, mapHeight);
        boundaries = new Vector2(mapWidth, mapHeight);
    }

    @Override
    public void Update(float delta) {
        Vector2 camPos = new Vector2(boat.position.x + boat.GetCenterX(), boat.position.y + boat.GetCenterY());
        camPos.x = MathUtils.clamp(camPos.x, camera.viewportWidth/2, boundaries.x - camera.viewportWidth/2);
        camPos.y = MathUtils.clamp(camPos.y, camera.viewportHeight/2, boundaries.y - camera.viewportHeight/2);
        camera.position.set(camPos.x, camPos.y, 0);
        camera.update();
        bg.Update(delta);
    }


    @Override
    public void Draw(SpriteBatch batch) {
        bg.Draw(batch);
    }


}
