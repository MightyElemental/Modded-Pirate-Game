package io.github.annabeths.GameGenerics;

/**
 * Enum that represents possible update types.
 */
public enum Upgrades {
	health("Health"), maxhealth("Max Health"), speed("Speed"), turnspeed("Turn Speed"),
	projectiledamage("Damage"), projectilespeed("Projectile Speed"), defense("Defense");

	public final String label;

	/**
	 * Constructor for a new Upgrades
	 * @param label label for the upgrade
	 */
	Upgrades(String label) {
		this.label = label;
	}
}
