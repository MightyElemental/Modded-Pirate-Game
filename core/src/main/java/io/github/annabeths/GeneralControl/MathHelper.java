package io.github.annabeths.GeneralControl;

/** A collection of useful math functions */
public class MathHelper {

	/**
	 * Normalize an angle between 0-360 degrees
	 * 
	 * @param the angle
	 * @return the normalized angle
	 */
	public static float normalizeAngle(float angle) {
		angle %= 360;
		angle += angle < 0 ? 360 : 0;
		return angle;
	}

	/**
	 * Get the absolute difference between two angles in degrees.
	 * 
	 * @param a1 the first angle
	 * @param a2 the second angle
	 * @return The absolute difference
	 */
	public static float getAbsDiff2Angles(float a1, float a2) {
		float c = 180;
		return c - Math.abs((Math.abs(a1 - a2) % (2 * c)) - c);
	}

}
