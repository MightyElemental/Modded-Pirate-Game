package io.github.annabeths;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Projectiles.ProjectileData;

import java.lang.Math;

public class Projectile extends PhysicsObject{
    
    private Vector2 movePerFrame;
    private float velocity; 
    private Sprite sprite;
    public boolean isPlayerProjectile;

    public Projectile(Vector2 origin, float originRot, ProjectileData data, boolean isPlayerProjectile) {
        position = origin;
        velocity = data.velocity;
        this.isPlayerProjectile = isPlayerProjectile;
        
        // Calculate how far the projectile will move per frame
        movePerFrame = new Vector2((float) Math.cos(Math.toRadians(originRot)) * velocity, 
        		(float) Math.sin(Math.toRadians(originRot)) * velocity);
        
        sprite = new Sprite(data.texture);
        sprite.setSize(data.size.x, data.size.y);
        sprite.setOrigin(data.size.x / 2, data.size.y / 2);
        sprite.setRotation(originRot);

        collisionPolygon = new Polygon(new float[]{data.size.x/2,0,
            data.size.x,data.size.y/2,
            data.size.x/2,data.size.y,
            0,data.size.y/2});
    }


    @Override
    public void Update(float delta) {
        position.x += movePerFrame.x * delta;
        position.y += movePerFrame.y * delta;
        collisionPolygon.setPosition(position.x, position.y);
    }

    @Override
    public void Draw(SpriteBatch batch) {
        sprite.setCenter(position.x, position.y);
        sprite.draw(batch);
    }


    @Override
    public boolean OnCollision(PhysicsObject other) {
        // projectile should not do anything on collision itself, the other object should handle it
        return false;
    }
}
