package io.github.annabeths;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.PlayerBoat;

public class PlayerCollege extends College{

    int healAmount;

    public PlayerCollege(Vector2 position, Texture aliveTexture, Texture islandTexture) {
        //TODO set a collision polygon
        healAmount = 15;
        range = 400;
        aliveSprite = new Sprite(aliveTexture);
        aliveSprite.setPosition(position.x, position.y);
        aliveSprite.setSize(100,100);
        islandSprite = new Sprite(islandTexture);
        islandSprite.setCenter(aliveSprite.getX()+5, aliveSprite.getY()+5);
        islandSprite.setSize(120, 120);
        this.position = position;
        collisionPolygon = new Polygon(new float[]{0,0,100,0,100,100,0,100});
        collisionPolygon.setPosition(position.x, position.y);
    }

    @Override
    public void OnCollision(PhysicsObject other) {
        // TODO Auto-generated method stub
    }

    @Override
    public void Update(float delta, PhysicsObject playerBoat)
    {
        PlayerBoat boat = (PlayerBoat) playerBoat;
        if(isInRange(boat))
        { // if the player boat is in range, heal it
            boat.Heal(healAmount, delta);
        }
    }

    @Override
    public void Draw(SpriteBatch batch)
    {
        islandSprite.draw(batch);
        aliveSprite.draw(batch);    
    }

}
