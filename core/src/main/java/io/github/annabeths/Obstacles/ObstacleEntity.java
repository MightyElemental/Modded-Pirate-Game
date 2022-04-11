package io.github.annabeths.Obstacles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import io.github.annabeths.Boats.FriendlyBoat;
import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Level.GameMap;
import io.github.annabeths.Projectiles.Projectile;
import io.github.annabeths.Projectiles.ProjectileData;

public abstract class ObstacleEntity extends PhysicsObject {

    GameController controller;
    String texturePath;

    public ObstacleEntity(GameController controller, Vector2 position, String texLoc, Vector2 size) {
        this.controller = controller;
        this.position = position.cpy();
        texturePath = texLoc;
        setSprite(texLoc, position, size);
    }


    public abstract void Update(float delta);

    public abstract void Shoot();

    public abstract void Destroy();


    /**
     * Place the entity  somewhere in global space
     *
     * @param x the x position
     * @param y the y position
     */
    void SetPosition(float x, float y) {
        position.x = x;
        position.y = y;
        sprite.setPosition(x, y);
    }

    public void Draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    /**
     * Creates a new projectile fired from the entity.
     *
     * @param type the type of projectile to shoot
     * @param rotationOffset the angle to fire the projectile relative to the boat
     * @param dmgMul the damage multiplier
     * @param spdMul the speed multiplier
     *
     * @return A new projectile object
     */
    protected Projectile createProjectile(ProjectileData type, float rotationOffset, float dmgMul,
                                          float spdMul, Vector2 pos) {
        return new Projectile(pos, rotation + rotationOffset, type, false, dmgMul,
                spdMul);
    }

}
