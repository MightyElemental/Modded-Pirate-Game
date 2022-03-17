package io.github.annabeths.Projectiles;

import com.badlogic.gdx.math.Vector2;

/**
 * Creates and holds one instance of each projectile type there is. This will
 * avoid duplicating instances of the same projectile data when one will
 * suffice.
 * 
 * @author Leif Kemp
 * @author Anna Singleton
 * @author James Burnell
 */
public class ProjectileDataHolder {

	// Prevent instantiation
	private ProjectileDataHolder() {
	}

	public static final ProjectileData stock;
	public static final ProjectileData enemy;
	public static final ProjectileData boss;

	static {
		stock = new ProjectileData(250, 20, new Vector2(20, 20), "img/entity/cannonball.png");
		boss = new ProjectileData(300, 20, new Vector2(20, 20), "img/entity/cannonball.png");
		enemy = new ProjectileData(250, 20, new Vector2(20, 20), "img/entity/cannonball.png");
	}
}
