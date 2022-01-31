package io.github.annabeths.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.GameGenerics.GameObject;


public class GameMap extends GameObject{

    public OrthographicCamera camera;
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
        // centre the camera on the player

        if(Gdx.graphics.getWidth() >= boundaries.x)
        { // if the screen is wider than the map, then just centre on the map
            camPos.x = boundaries.x / 2;
        }
        else
        {
            camPos.x = MathUtils.clamp(camPos.x, camera.viewportWidth/2, boundaries.x - camera.viewportWidth/2);
            //else clamp the camera to only show the map, and try to centre on the player
        }
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
