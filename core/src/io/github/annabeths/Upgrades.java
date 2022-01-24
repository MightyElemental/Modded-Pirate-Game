package io.github.annabeths;

public enum Upgrades{
	health("Health"), maxhealth("Max Health"), speed("Speed"), turnspeed("Turn Speed");

	public final String label;

	private Upgrades(String label){
		this.label = label;
	}
}
