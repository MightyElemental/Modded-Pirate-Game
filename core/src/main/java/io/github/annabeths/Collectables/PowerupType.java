package io.github.annabeths.Collectables;

import java.util.Random;

/** @author James Burnell */
public enum PowerupType {

	SPEED("Speed", 10), RAPIDFIRE("Rapid Fire", 10), INVINCIBILITY("Invincibility", 10),
	STARBURSTFIRE("Burst Fire", 10), DAMAGE("Damage Buff", 10);

	private PowerupType(String name, float activeTime) {
		this.name = name;
		this.defaultActiveTime = activeTime;
	}

	private String name;
	private float defaultActiveTime;

	/**
	 * Get the displayed name of the powerup
	 * 
	 * @return The displayed name
	 */
	public String getName() {
		return name;
	}

	/**
	 * How long the powerup lasts when collected
	 * 
	 * @return the duration in seconds
	 */
	public float getDefaultTime() {
		return defaultActiveTime;
	}

	private static Random rand = new Random();
	public static PowerupType randomPower() {
		return PowerupType.values()[rand.nextInt(5)];
	}
}
