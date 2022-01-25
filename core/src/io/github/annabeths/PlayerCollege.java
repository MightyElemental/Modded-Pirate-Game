package io.github.annabeths;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class PlayerCollege extends College{

    int healAmount;

    public PlayerCollege(Vector2 position, Texture texture) {
        //TODO set a collision polygon
        healAmount = 15;
        range = 50;
        aliveSprite = new Sprite(texture);
        aliveSprite.setPosition(position.x, position.y);
        aliveSprite.setSize(100,100);
        this.position = position;
    }

    @Override
    public boolean OnCollision(PhysicsObject other) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    void Update(float delta, PhysicsObject playerBoat)
    {
        PlayerBoat boat = (PlayerBoat) playerBoat;
        if(isInRange(boat))
        { // if the player boat is in range, heal it
            System.out.println("healed for " + (healAmount * delta));
            boat.Heal((int) (healAmount * delta));
        }
    }

    @Override
    void Draw(SpriteBatch batch)
    {
        aliveSprite.draw(batch);    
    }

}
