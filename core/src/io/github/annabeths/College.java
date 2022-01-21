package io.github.annabeths;

import com.badlogic.gdx.math.Vector2;
import java.lang.Math;

public abstract class College extends PhysicsObject{
    Vector2 position;
    int range;
    int HP;
    int damage;
    int fireRate;

    boolean isInRange(PhysicsObject other)
    {
        return range >= Math.sqrt(Math.pow(position.x - other.x, 2) + Math.pow(position.y - other.y, 2));
    }
}
