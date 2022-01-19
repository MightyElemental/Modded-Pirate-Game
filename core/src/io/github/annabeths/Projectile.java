package io.github.annabeths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.io.Console;
import java.lang.Math;

public class Projectile extends PhysicsObject{
    
    private Vector2 position;
    private Vector2 movePerFrame;
    private float velocity; 
    private Sprite sprite;

    public Projectile(float originPosX, float originPosY, float originRot, Texture bullet) {
        position = new Vector2(originPosX, originPosY);
        velocity = 15f;
        movePerFrame = new Vector2((float) Math.cos(Math.toRadians(originRot+90)) * velocity, (float) Math.sin(Math.toRadians(originRot+90)) * velocity);
        System.out.println("your mum");
        sprite = new Sprite(bullet);
        sprite.setSize(20, 20);
        sprite.setOrigin(10, 10);
        sprite.setRotation(originRot);
    }


    @Override
    public void Update(float delta) {
        // TODO Auto-generated method stub
        position.x += movePerFrame.x * delta;
        position.y += movePerFrame.y * delta;
    }

    @Override
    public void Draw(SpriteBatch batch) {
        // TODO Auto-generated method stub
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }


    @Override
    void OnCollision(PhysicsObject other) {
        // TODO Auto-generated method stub
        
    }



}
