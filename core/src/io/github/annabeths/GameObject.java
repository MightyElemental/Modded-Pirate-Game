package io.github.annabeths;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameObject {
    //every GameObject needs to have these values, and define these methods

	float x = 0;
	float y = 0;
    float rotation = 0;
    Sprite sprite = null;    

    void Update(float delta);

    void Draw(SpriteBatch batch);
}
