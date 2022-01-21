package io.github.annabeths;

import com.badlogic.gdx.math.Vector2;

import java.lang.Math;

public abstract class College extends PhysicsObject{
    Vector2 position;
    int range;
    int HP;
    int damage;
    int fireRate;

    boolean isInRange(Boat other)
    {
        // work out euclidean distance to the other physics object, and then returns true if the 
        // that distance is <= the range of the college
        // this will be used to check if the enemy college should attack the player
        // this will be used to check if the friendly college should heal the player
        return range >= Math.sqrt(Math.pow(position.x - other.x, 2) + Math.pow(position.y - other.y, 2));
    }
}
