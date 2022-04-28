package io.github.annabeths.GeneralControl;

/**
 * Used to define multipliers to different game variables depending on the
 * selected difficulty. Based on the difficulty, other changes may be defined
 * elsewhere in other classes.
 * 
 * @since Assessment 2
 * @author James Burnell
 * @author Hector Woods
 */
public enum Difficulty {

	/**
	 * Kraken does not spawn on easy, very few mines and ships are much weaker
	 */
	EASY(0.75f, 0.75f, 1.1f, 1.5f, 40, false, 9),
	/** Medium will be the standard difficulty, i.e. no changes */
	MEDIUM(1, 1, 1, 1, 50, true, 7),
	/**
	 * On hard difficulty there are significantly more mines and enemies are a
	 * lot stronger
	 */
	HARD(2f, 2f, 0.8f, 0.75f, 75, true, 5);

	float enemyDmgMul;
	float enemyHpMul;
	float playerDmgMul;
	float playerXpMul;
	float numMines;
	boolean krakenSpawns;
	float timeBetweenWeather;

	private Difficulty(float enemyDmgMul, float enemyHpMul, float playerDmgMul, float playerXpMul,
			float numMines, boolean krakenSpawns, float timeBetweenWeather) {
		this.enemyDmgMul = enemyDmgMul;
		this.enemyHpMul = enemyHpMul;
		this.playerDmgMul = playerDmgMul;
		this.playerXpMul = playerXpMul;
		this.numMines = numMines;
		this.krakenSpawns = krakenSpawns;
		this.timeBetweenWeather = timeBetweenWeather;
	}

	/**
	 * @return the enemyDmgMul
	 */
	public float getEnemyDmgMul() {
		return enemyDmgMul;
	}

	/**
	 * @return the enemyHpMul
	 */
	public float getEnemyHpMul() {
		return enemyHpMul;
	}

	/**
	 * @return the playerDmgMul
	 */
	public float getPlayerDmgMul() {
		return playerDmgMul;
	}

	/**
	 * @return the playerXpMul
	 */
	public float getPlayerXpMul() {
		return playerXpMul;
	}

	/**
	 * @return whether the Kraken spawns or not
	 */
	public boolean doesKrakenSpawn() {
		return krakenSpawns;
	}

	/**
	 * @return the number of mines that should spawn
	 */
	public float getNumMines() {
		return numMines;
	}

	/**
	 * @return the time between weather spawns
	 */
	public float getTimeBetweenWeather() {
		return timeBetweenWeather;
	}

}
