package io.github.annabeths.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public abstract class ProjectileData {
	// This abstract class is used to define each type of projectile found in the game
	public float velocity;
	public float damage;
	public Vector2 size;
	public Texture texture;
}
