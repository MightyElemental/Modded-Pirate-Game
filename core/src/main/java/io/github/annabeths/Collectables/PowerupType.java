package io.github.annabeths.Collectables;

import com.badlogic.gdx.math.MathUtils;

/**
 * @author James Burnell
 * @since Assessment 2
 */
public enum PowerupType {

	SPEED("Speed", 10, "speed.png", "speed-boost.mp3"),
	RAPIDFIRE("Rapid Fire", 10, "rapidFire.png", "rapid-fire.mp3"),
	INVINCIBILITY("Invincibility", 10, "invincible.png", "invincibility.mp3"),
	STARBURSTFIRE("Burst Fire", 10, "starBurst.png", "burst-fire.mp3"),
	DAMAGE("Damage Buff", 10, "dmgBuff.png", "damage-buff.mp3");

	private PowerupType(String name, float activeTime, String texture, String activationAudio) {
		this.name = name;
		this.defaultActiveTime = activeTime;
		this.texture = "img/powerup/" + texture;
		this.activationAudio = "audio/powerups/" + activationAudio;
	}

	private String name;
	private float defaultActiveTime;
	private String texture;
	private String activationAudio;

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

	/**
	 * @return the activationAudio
	 */
	public String getActivationAudio() {
		return activationAudio;
	}
}
