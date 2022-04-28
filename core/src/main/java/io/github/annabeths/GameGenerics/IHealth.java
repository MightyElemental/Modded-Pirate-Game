package io.github.annabeths.GameGenerics;

/**
 * @author James Burnell
 * @since Assessment 2
 */
public interface IHealth {

	public float getHealth();

	public float getMaxHealth();

	public void damage(float dmg);

	public default boolean isDead() {
		return getHealth() <= 0;
	}

}
