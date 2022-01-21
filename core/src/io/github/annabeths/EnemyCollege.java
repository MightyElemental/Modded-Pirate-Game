package io.github.annabeths;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Projectiles.ProjectileData;

public class EnemyCollege extends College{
   
    int damage;
    float shootingInaccuracy = 15f; // in degrees (each side)
    float fireRate = 1.5f;
    float timeSinceLastShot = 0;
    Random rd = new Random();
    GameController gc;
    ProjectileData projectileType;
   
    public EnemyCollege(Vector2 position, Texture aliveTexture, GameController controller, ProjectileData projectileData)
    {
        aliveSprite = new Sprite(aliveTexture);
        aliveSprite.setPosition(position.x, position.y);
        aliveSprite.setSize(100, 100);
        this.position = position;
        range = 500;
        gc = controller;
        projectileType = projectileData;
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
        }

        PlayerBoat boat = (PlayerBoat) playerBoat;
        if(isInRange(boat))
        {
            if(timeSinceLastShot >= fireRate)
            {
                ShootAt(new Vector2(boat.x, boat.y));
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
        float shotAngle = (float) Math.atan2(position.x - target.x, position.y - target.y);
        shotAngle += (rd.nextFloat() * shootingInaccuracy * 2) - (shootingInaccuracy);
        gc.NewPhysicsObject(new Projectile(position, shotAngle, projectileType));
        System.out.println("bang! towards " + target.toString());
    }

}
