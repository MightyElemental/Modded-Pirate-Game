package io.github.annabeths;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.lang.Math;

public class Projectile extends PhysicsObject{
    
    private Vector2 position;
    private Vector2 movePerFrame;
    private float velocity; 
    private Sprite sprite;

    public Projectile(float originPosX, float originPosY, float originRot, Texture bullet) {
        position = new Vector2(originPosX, originPosY);
        velocity = 200f;
        movePerFrame = new Vector2((float) Math.cos(Math.toRadians(originRot)) * velocity, (float) Math.sin(Math.toRadians(originRot)) * velocity);
        sprite = new Sprite(bullet);
        sprite.setSize(20, 20);
        sprite.setOrigin(10, 10);
        sprite.setRotation(originRot);
    }


    @Override
    public void Update(float delta) {
        position.x += movePerFrame.x * delta;
        position.y += movePerFrame.y * delta;
    }

    @Override
    public void Draw(SpriteBatch batch) {
        sprite.setCenter(position.x, position.y);
        sprite.draw(batch);
    }


    @Override
    void OnCollision(PhysicsObject other) {
        // TODO handle collisions
    }



}
