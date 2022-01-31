package io.github.annabeths.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import io.github.annabeths.GameGenerics.GameObject;

public class WaterBackground extends GameObject {


    TextureRegion[] waterTextureRegion;
    TextureRegionDrawable waterTextureRegionDrawable;
    int waterTextureNumber = 0;
    float lastWaterTextureChange;
    final float waterChangeDelay = 1f;

    TextureRegion grassTextureRegion;
    TextureRegionDrawable grassTextureRegionDrawable;
    Vector2 mapSize;

    public WaterBackground(int mapWidth, int mapHeight)
    {
        waterTextureRegion = new TextureRegion[3];
        for (int i=0; i < 3; i++)
        {
            Texture x = new Texture("img/water" + (i + 1) + ".png"); //load a texture into memory
            x.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
            waterTextureRegion[i] = new TextureRegion(x); // make a texture region and set the size
            waterTextureRegion[i].setRegionWidth(mapWidth);
            waterTextureRegion[i].setRegionHeight(mapHeight);
        }
        waterTextureRegionDrawable = new TextureRegionDrawable(waterTextureRegion[0]);
        //make a drawable texture region

        lastWaterTextureChange = 0; //setup the counter

        mapSize = new Vector2(mapWidth, mapHeight);
        Texture grassTexture = new Texture("img/grass.png"); // load the grass texture
        grassTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        grassTextureRegion = new TextureRegion(grassTexture);
        grassTextureRegion.setRegionWidth(mapWidth*4); //draw the grass texture off screen
        grassTextureRegion.setRegionHeight(mapWidth*4);
        grassTextureRegionDrawable = new TextureRegionDrawable(grassTextureRegion);
    }

    @Override
    public void Update(float delta) {
        lastWaterTextureChange += delta;
        if(lastWaterTextureChange >= waterChangeDelay)
        {
            waterTextureNumber = (waterTextureNumber + 1) % 3;
            waterTextureRegionDrawable.setRegion(waterTextureRegion[waterTextureNumber]);
            lastWaterTextureChange = 0;
        }
    }

    @Override
    public void Draw(SpriteBatch batch) {
        grassTextureRegionDrawable.draw(batch, -Gdx.graphics.getWidth(), -Gdx.graphics.getHeight(),
                                     mapSize.x*4, mapSize.y*4);
        waterTextureRegionDrawable.draw(batch, 0, 0, mapSize.x, mapSize.y);
    }
    
}
