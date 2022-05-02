package io.github.annabeths.Collectables;

import com.badlogic.gdx.math.MathUtils;

/**
 * Enum for different types of powerup.
 * @author James Burnell
 * @since Assessment 2
 */
public enum PowerupType {

	/**
	 * Speed powerup - increases player speed
	 */
	SPEED("Speed", 10, "speed.png", "speed-boost.mp3"),
	/**
	 * Rapid fire powerup - increases attack speed
	 */
	RAPIDFIRE("Rapid Fire", 10, "rapidFire.png", "rapid-fire.mp3"),
	/**
	 * Invincibility powerup - makes the player impervious to damage
	 */
	INVINCIBILITY("Invincibility", 10, "invincible.png", "invincibility.mp3"),
	/**
	 * StarBurstFire powerup - makes the player shoot more cannonballs in a star pattern.
	 */
	STARBURSTFIRE("Burst Fire", 10, "starBurst.png", "burst-fire.mp3"),
	/**
	 * Damage powerup - makes the player's cannonballs deal more damage.
	 */
	DAMAGE("Damage Buff", 10, "dmgBuff.png", "damage-buff.mp3");

	/**
	 * Constructor for PowerupType - defines a new powerup in the Enum.
	 * @param name the name of the powerup
	 * @param activeTime how long the powerup should be active for
	 * @param texture the texture of the powerup
	 * @param activationAudio the sound that plays when the powerup is activated
	 */
	PowerupType(String name, float activeTime, String texture, String activationAudio) {
		this.name = name;
		this.defaultActiveTime = activeTime;
		this.texture = "img/powerup/" + texture;
		this.activationAudio = "audio/powerups/" + activationAudio;
	}

	private final String name;
	private final float defaultActiveTime;
	private final String texture;
	private final String activationAudio;

	/**
	 * Get the displayed name of the powerup
	 * @return The displayed name
	 */
	public String getName() {
		return name;
	}

	/**
	 * How long the powerup lasts when collected
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
