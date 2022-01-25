package io.github.annabeths;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
    //every GameObject needs to have these values, and define these methods

    Vector2 position;

    float rotation = 0;
    Sprite sprite = null;    
    boolean killOnNextTick = false;

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
