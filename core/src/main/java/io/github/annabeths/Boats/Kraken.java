package io.github.annabeths.Boats;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import io.github.annabeths.Boats.AIBoat;
import io.github.annabeths.GameGenerics.IHealth;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Projectiles.Projectile;
import io.github.annabeths.Projectiles.ProjectileData;

public class Kraken extends AIBoat implements IHealth {

    float maxHealth = 500f;
    float health = maxHealth;
    int size = 300;


    public Kraken(GameController gc, Vector2 p){
        super(gc, p,"img/entity/kraken1.png");
        xpValue = 500;
        plunderValue = 500;

        setSprite("img/entity/kraken1.png", p, new Vector2(size, size));
        setCenter(p);
        collisionPolygon = new Polygon(new float[] { 0, size/4, size/4, size/2, size/2, size/4, size/4, 0 });
        //collisionPolygon.setPosition(position.x - (size/2),
        //        position.y - (size/2));
        //collisionPolygon.setOrigin(position.x + (size/2), position.y + (size/2));
    }


    @Override
    public float getHealth() {
        return health;
    }

    @Override
    void Shoot() {
        // the projectile type to shoot
        ProjectileData pd = ProjectileData.STOCK;

        Projectile projLeft = createProjectile(pd, -90, 1, 1);
        Projectile projRight = createProjectile(pd, 90, 1, 1);

        // Add the projectile to the GameController's physics objects list so it
        // receives updates
        controller.NewPhysicsObject(projLeft);
        controller.NewPhysicsObject(projRight);

    }

    void Destroy(){

    }

    @Override
    public float getMaxHealth() {
        return maxHealth;
    }

    @Override
    public void damage(float dmg) {

    }


    @Override
    public void OnCollision(PhysicsObject other) {
        System.out.println(other);
        if(other instanceof PlayerBoat){
            ((PlayerBoat) other).HP = 0;
        }
    }
}
