package io.github.annabeths.Collectables;

import com.badlogic.gdx.math.MathUtils;

/** @author James Burnell */
public enum PowerupType {

	SPEED("Speed", 20, "speed.png"), RAPIDFIRE("Rapid Fire", 30, "rapidFire.png"),
	INVINCIBILITY("Invincibility", 30, "invincible.png"),
	STARBURSTFIRE("Burst Fire", 15, "starBurst.png"), DAMAGE("Damage Buff", 20, "dmgBuff.png");

	private PowerupType(String name, float activeTime, String texture) {
		this.name = name;
		this.defaultActiveTime = activeTime;
		this.texture = "img/powerup/" + texture;
	}

	private String name;
	private float defaultActiveTime;
	private String texture;

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

	public static PowerupType randomPower() {
		PowerupType[] vals = PowerupType.values();
		return vals[MathUtils.random(vals.length - 1)];
	}

	/**
	 * @return the texture
	 */
	public String getTexture() {
		return texture;
	}
}
