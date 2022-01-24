package io.github.annabeths;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Projectiles.ProjectileData;

import java.lang.Math;

public class Projectile extends PhysicsObject{
    
    private Vector2 position;
    private Vector2 movePerFrame;
    private float velocity; 
    private Sprite sprite;

    public Projectile(Vector2 origin, float originRot, ProjectileData data) {
        position = origin;
        velocity = data.velocity;
        
        // Calculate how far the projectile will move per frame
        movePerFrame = new Vector2((float) Math.cos(Math.toRadians(originRot)) * velocity, 
        		(float) Math.sin(Math.toRadians(originRot)) * velocity);
        
        sprite = new Sprite(data.texture);
        sprite.setSize(data.size.x, data.size.y);
        sprite.setOrigin(data.size.x / 2, data.size.y / 2);
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
    public void OnCollision(PhysicsObject other) {
        // TODO handle collisions
    }



}
