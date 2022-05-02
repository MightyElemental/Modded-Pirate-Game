package io.github.annabeths.GameGenerics;

public enum Upgrades {
	health("Health"), maxhealth("Max Health"), speed("Speed"), turnspeed("Turn Speed"),
	projectiledamage("Damage"), projectilespeed("Projectile Speed"), defense("Defense");

	public final String label;

	Upgrades(String label) {
		this.label = label;
	}
}
