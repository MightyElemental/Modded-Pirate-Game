package io.github.annabeths;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameObject {
    //every GameObject needs to have these values, and define these methods

    float x;
    float y;

    float rotation = 0;
    Sprite sprite = null;    

    void Update(float delta)
    {

    }

    void Update(float delta, PhysicsObject other)
    {

    }

    void Draw(SpriteBatch batch)
    {

    }
}
