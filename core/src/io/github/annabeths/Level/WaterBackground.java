package io.github.annabeths.Level;

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
    Vector2 screenSize;

    public WaterBackground(int screenWidth, int screenHeight)
    {
        waterTextureRegion = new TextureRegion[3];
        for (int i=0; i < 3; i++)
        {
            Texture x = new Texture("img/water" + (i + 1) + ".png");
            x.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
            waterTextureRegion[i] = new TextureRegion(x);
            waterTextureRegion[i].setRegionWidth(screenWidth);
            waterTextureRegion[i].setRegionHeight(screenHeight);
        }
        waterTextureRegionDrawable = new TextureRegionDrawable(waterTextureRegion[0]);
        lastWaterTextureChange = 0;

        screenSize = new Vector2(screenWidth, screenHeight);
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
        waterTextureRegionDrawable.draw(batch, 0, 0, screenSize.x, screenSize.y);
    }
    
}
