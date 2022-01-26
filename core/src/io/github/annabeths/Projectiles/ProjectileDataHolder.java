package io.github.annabeths.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public  class ProjectileDataHolder {
	// This class will create one instance of each projectile type there is, this will avoid
	// spamming instances of the same projectile data when one will suffice.
	
	public ProjectileData stock;
	public ProjectileData enemy;
	public ProjectileData boss;

	public ProjectileDataHolder() {
		stock = new ProjectileData(250, 20, new Vector2(20,20),
		new Texture("img/cannonball.png"));
		boss = new ProjectileData(300, 20, new Vector2(20,20),
		new Texture("img/cannonball.png"));
	}
}
