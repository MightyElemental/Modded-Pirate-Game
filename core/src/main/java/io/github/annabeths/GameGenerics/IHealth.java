package io.github.annabeths.GameGenerics;

/**
 * Interface that represents GameObjects with health.
 * @author James Burnell
 * @since Assessment 2
 */
public interface IHealth {

	/**
	 * Getter method for the IHealth's health
	 * @return health of the IHealth
	 */
	float getHealth();

	/**
	 * Getter method for the IHealth's maximum health
	 * @return maximum health of the IHealth
	 */
	float getMaxHealth();

	/**
	 * Damage the IHealth, i.e. reduce health by damage.
	 * @param dmg the amount of damage to be dealt.
	 */
	void damage(float dmg);

	/**
	 * IHealths die when their health is less than or equal to 0.
	 * @return is the IHealth dead?
	 */
	default boolean isDead() {
		return getHealth() <= 0;
	}

}
