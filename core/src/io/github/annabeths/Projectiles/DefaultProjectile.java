package io.github.annabeths.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class DefaultProjectile extends ProjectileData {
	public DefaultProjectile() {
		velocity = 200;
		damage = 100;
		shotDelay = 0.5f;
		size = new Vector2(20, 20);
		texture = new Texture("img/cannonball.png");
	}
}
