package io.github.annabeths;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
    //every GameObject needs to have these values, and define these methods

    public Vector2 position;

    public float rotation = 0;
    public Sprite sprite = null;    
    public boolean killOnNextTick = false;

    public void Update(float delta)
    {

    }

    public void Update(float delta, PhysicsObject other)
    {

    }

    public void Draw(SpriteBatch batch)
    {

    }
}
