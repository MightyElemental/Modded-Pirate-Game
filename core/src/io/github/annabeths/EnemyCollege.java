package io.github.annabeths;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Projectiles.ProjectileData;

public class EnemyCollege extends College{
   
    int damage;
    float shootingInaccuracy = 0f; // in degrees (each side)
    float fireRate = 1.5f;
    float timeSinceLastShot = 0;
    Random rd = new Random();
    GameController gc;
    ProjectileData projectileType;
   
    public EnemyCollege(Vector2 position, Texture aliveTexture,
                        GameController controller, ProjectileData projectileData)
    {
        aliveSprite = new Sprite(aliveTexture);
        aliveSprite.setPosition(position.x, position.y);
        aliveSprite.setSize(100, 100);
        this.position = position;
        range = 500;
        gc = controller;
        projectileType = projectileData;
        collisionPolygon = new Polygon(new float[]{0,0,100,0,100,100,0,100});
        collisionPolygon.setPosition(position.x, position.y);
    }

    @Override
    public void OnCollision(PhysicsObject other) {
        // TODO Auto-generated method stub
    }    

    void Update(float delta, PhysicsObject playerBoat)
    {
        if(timeSinceLastShot < fireRate)
        {
            timeSinceLastShot += delta;
        } // increase the time on the timer to allow for fire rate calculation

        PlayerBoat boat = (PlayerBoat) playerBoat; //cast to playerboat
        if(isInRange(boat)) // is the player boat in range
        {
            if(timeSinceLastShot >= fireRate)
            {
                ShootAt(new Vector2(boat.position.x, boat.position.y));
                timeSinceLastShot = 0;
            }
        }
    }

    void Draw(SpriteBatch batch)
    {
        aliveSprite.draw(batch);
    }

    void ShootAt(Vector2 target)
    {
        float shotAngle = (float) Math.toDegrees(Math.atan2(target.y - (position.y + aliveSprite.getHeight()/2),
                                                            target.x - (position.x + aliveSprite.getWidth()/2)));
        //calculate the shot angle by getting a vector from the centre of the college to the target
        //convert to degrees for the inaccuracy calculation
        shotAngle += (rd.nextFloat() * shootingInaccuracy * 2) - (shootingInaccuracy);
        //inaccuracy calculation works by rd.nextfloat() gets a pseudorandom float from 0-1
        // we multiply it by 2* the shooting inaccuracy to get the right width of distribution
        // then - the shooting inaccuracy to centre the distribution on 0
        gc.NewPhysicsObject(new Projectile(new Vector2(position.x + aliveSprite.getWidth()/2,
                                                       position.y + aliveSprite.getHeight()/2),
                            shotAngle, projectileType, false));
        //instantiate a new bullet and pass a reference to the gamecontroller so it can be updated and drawn
    }

}
