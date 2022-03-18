package io.github.annabeths.GeneralControl;

/**
 * Used to define multipliers to different game variables depending on the
 * selected difficulty. Based on the difficulty, other changes may be defined
 * elsewhere in other classes.
 * 
 * @author James Burnell
 */
public enum Difficulty {

	EASY(0.75f, 0.75f, 1.1f, 1.5f),
	/** Medium will be the standard difficulty, i.e. no changes */
	MEDIUM(1, 1, 1, 1), HARD(2f, 2f, 0.8f, 0.75f), INSANE(5f, 5f, 0.5f, 0.5f);

	float enemyDmgMul;
	float enemyHpMul;
	float playerDmgMul;
	float playerXpMul;

	private Difficulty(float enemyDmgMul, float enemyHpMul, float playerDmgMul, float playerXpMul) {
		this.enemyDmgMul = enemyDmgMul;
		this.enemyHpMul = enemyHpMul;
		this.playerDmgMul = playerDmgMul;
		this.playerXpMul = playerXpMul;
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

}
