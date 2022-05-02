package io.github.annabeths.GameGenerics;

/**
 * @author James Burnell
 * @since Assessment 2
 */
public interface IHealth {

	float getHealth();

	float getMaxHealth();

	void damage(float dmg);

	default boolean isDead() {
		return getHealth() <= 0;
	}

}
