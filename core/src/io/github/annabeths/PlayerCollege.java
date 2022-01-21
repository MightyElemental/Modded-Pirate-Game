package io.github.annabeths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class PlayerCollege extends College{

    //TODO
    // check range from centre of sprite, not corner
    // change check of boat to the centre of the boat not its corner

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
    public void OnCollision(PhysicsObject other) {
        // TODO Auto-generated method stub

    }

    @Override
    void Update(float delta, PhysicsObject playerBoat)
    {
        PlayerBoat boat = (PlayerBoat) playerBoat;
        if(isInRange(boat))
        {
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
