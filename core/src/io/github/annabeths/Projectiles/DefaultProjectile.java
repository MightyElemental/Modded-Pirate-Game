package io.github.annabeths.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class DefaultProjectile extends ProjectileData {
	public DefaultProjectile() {
		super.velocity = 200;
		super.damage = 100;
		super.size = new Vector2(20, 20);
		this.texture = new Texture("img/cannonball.png");
	}
}
