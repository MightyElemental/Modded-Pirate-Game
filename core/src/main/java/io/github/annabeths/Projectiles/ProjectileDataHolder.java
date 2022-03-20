package io.github.annabeths.Projectiles;

import com.badlogic.gdx.math.Vector2;

/**
 * Creates and holds one instance of each projectile type there is. This will
 * avoid duplicating instances of the same projectile data when one will
 * suffice.
 */
public class ProjectileDataHolder {

	public ProjectileData stock;
	public ProjectileData enemy;
	public ProjectileData boss;

	public ProjectileDataHolder() {
		stock = new ProjectileData(250, 20, new Vector2(20, 20), "img/entity/cannonball.png");
		boss = new ProjectileData(300, 20, new Vector2(20, 20), "img/entity/cannonball.png");
	}
}
