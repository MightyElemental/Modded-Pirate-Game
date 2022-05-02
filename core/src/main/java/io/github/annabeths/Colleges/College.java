package io.github.annabeths.Colleges;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.Boat;
import io.github.annabeths.GameGenerics.IHealth;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;

/**
 * Abstract class that represents all colleges.
 * @tt.updated Assessment 2
 * @author Hector Woods
 * @author James Burnell
 */
public abstract class College extends PhysicsObject implements IHealth {

	protected float maxHP;
	protected float HP;
	protected int range;
	protected int damage;

	protected float fireRate;

	protected boolean invulnerable;

	public Sprite deadSprite;
	public Sprite islandSprite;
	public String islandTextureFile;
	public Sprite aliveSprite;
	public String aliveTextureFile;

	/**
	 * Used to access player boat and to notify the game controller when the college
	 * has been destroyed
	 */
	protected GameController gc;

	/**
	 * @author James Burnell
	 * @tt.updated Assessment 2
	 * @param pos the position of the college
	 * @param aliveTexture the texture to use for when the college is alive
	 * @param islandTexture the texture of the island the college sits on
	 * @param gc the game controller
	 */
	public College(Vector2 pos, String aliveTexture, String islandTexture, GameController gc) {
		this.gc = gc;
		position = pos;

		setSprite(aliveTexture, position, new Vector2(100, 100));
		this.aliveTextureFile = aliveTexture;
		aliveSprite = this.sprite;
		islandSprite = initSprite(islandTexture,
				new Vector2(sprite.getX() - 10, sprite.getY() - 10), new Vector2(120, 120));
		this.islandTextureFile = islandTexture;

		collisionPolygon = new Polygon(new float[] { 0, 0, 100, 0, 100, 100, 0, 100 });
		collisionPolygon.setPosition(position.x, position.y);
	}


	/**
	 * Work out euclidean distance to the other physics object, and then returns
	 * true if the that distance is &lt;= the range of the college.<br>
	 * This will be used to check if the enemy college should attack the player.<br>
	 * This will be used to check if the friendly college should heal the player.
	 * 
	 * @param other the boat to check the range of
	 * @return {@code true} if the boat is in range of the college, {@code false}
	 *         otherwise
	 * @author James Burnell
	 */
	public boolean isInRange(Boat other) {
		return getCenter().dst(other.getCenter()) <= range;
	}

	/**
	 * getter method for the college's HP.
	 * @return the college's hp
	 */
	@Override
	public float getHealth() {
		return HP;
	}

	/**
	 * setter method for the college's HP
	 * @param newHP newHP for the college
	 */
	public void setHealth(float newHP){
		this.HP = newHP;
	}

	/**
	 * getter method for the college's max HP
	 * @return the college's max hp
	 */
	@Override
	public float getMaxHealth() {
		return maxHP;
	}

	/**
	 * deal damage to the college.
	 * @param dmg damage to be dealt
	 */
	@Override
	public void damage(float dmg) {
		HP = MathUtils.clamp(HP - dmg, 0, maxHP);
	}

	/**
	 * @return is the college invulnerable?
	 */
	public boolean isInvulnerable() {
		return invulnerable;
	}

	/**
	 * setter method for invulnerable
	 * @param invulnerable is the college invulnerable?
	 */
	public void setInvulnerable(boolean invulnerable) {
		this.invulnerable = invulnerable;
	}

	/**
	 * @return the range
	 */
	public int getRange() {
		return range;
	}

	/**
	 * @param range the range to set
	 */
	public void setRange(int range) {
		this.range = range;
	}

	/**
	 * @return the damage
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * @param damage the damage to set
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * @return the fireRate
	 */
	public float getFireRate() {
		return fireRate;
	}

	/**
	 * @param fireRate the fireRate to set
	 */
	public void setFireRate(float fireRate) {
		this.fireRate = fireRate;
	}

	/**
	 * @return is the college dead?
	 */
	@Override
	public boolean isDead() {
		return removeOnNextTick() || IHealth.super.isDead();
	}
}
