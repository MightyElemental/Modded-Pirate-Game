package io.github.annabeths.Projectiles;

public class ProjectileDataHolder {
	// This class will create one instance of each projectile type there is, this will avoid
	// spamming instances of the same projectile data when one will suffice.
	
	public DefaultProjectile stock;
	public ProjectileDataHolder() {
		stock = new DefaultProjectile();
	}
}
