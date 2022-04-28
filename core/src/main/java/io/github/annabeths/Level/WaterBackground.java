package io.github.annabeths.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import io.github.annabeths.GameGenerics.GameObject;
import io.github.annabeths.GeneralControl.ResourceManager;

/**
 * @author Annabeth
 * @since Assessment 1
 */
public class WaterBackground extends GameObject {

	TextureRegion[] waterTextureRegion;
	TextureRegionDrawable waterTextureRegionDrawable;
	int waterTextureNumber = 0;
	float lastWaterTextureChange;
	final float waterChangeDelay = 1f;

	TextureRegion grassTextureRegion;
	TextureRegionDrawable grassTextureRegionDrawable;
	Vector2 mapSize;

	public WaterBackground(int mapWidth, int mapHeight) {
		waterTextureRegion = new TextureRegion[3];
		for (int i = 0; i < 3; i++) {
			// load a texture into memory
			Texture x = ResourceManager.getTexture("img/world/water/water" + (i + 1) + ".png");

			x.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
			// make a texture region and set the size
			waterTextureRegion[i] = new TextureRegion(x);
			waterTextureRegion[i].setRegionWidth(mapWidth);
			waterTextureRegion[i].setRegionHeight(mapHeight);
		}
		// make a drawable texture region
		waterTextureRegionDrawable = new TextureRegionDrawable(waterTextureRegion[0]);

		// setup the counter
		lastWaterTextureChange = 0;

		mapSize = new Vector2(mapWidth, mapHeight);
		// load the grass texture
		Texture grassTexture = ResourceManager.getTexture("img/world/grass.png");
		grassTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		grassTextureRegion = new TextureRegion(grassTexture);
		// draw the grass texture off screen
		grassTextureRegion.setRegionWidth(mapWidth * 4);
		grassTextureRegion.setRegionHeight(mapWidth * 4);
		grassTextureRegionDrawable = new TextureRegionDrawable(grassTextureRegion);
	}

	@Override
	public void Update(float delta) {
		lastWaterTextureChange += delta;
		if (lastWaterTextureChange >= waterChangeDelay) {
			waterTextureNumber = (waterTextureNumber + 1) % 3;
			waterTextureRegionDrawable.setRegion(waterTextureRegion[waterTextureNumber]);
			lastWaterTextureChange = 0;
		}
	}

	@Override
	public void Draw(SpriteBatch batch) {
		grassTextureRegionDrawable.draw(batch, -Gdx.graphics.getWidth(), -Gdx.graphics.getHeight(),
				mapSize.x * 4, mapSize.y * 4);
		waterTextureRegionDrawable.draw(batch, 0, 0, mapSize.x, mapSize.y);
	}

}
