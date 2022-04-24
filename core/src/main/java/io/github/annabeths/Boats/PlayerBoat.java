package io.github.annabeths.Boats;

import static com.badlogic.gdx.Gdx.input;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.github.annabeths.Collectables.PowerupType;
import io.github.annabeths.Colleges.College;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameGenerics.Upgrades;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.GeneralControl.DebugUtils;
import io.github.annabeths.GeneralControl.MathHelper;
import io.github.annabeths.Projectiles.Projectile;
import io.github.annabeths.Projectiles.ProjectileData;
import io.github.annabeths.Projectiles.ProjectileRay;

/**
 * @author Leif Kemp
 * @author Anna Singleton
 * @author James Burnell
 * @author Hector Woods
 * @author Ben Faulkner
 */
public class PlayerBoat extends Boat {

	/**
	 * How much to multiply the projectile damage by
	 * 
	 * @see #Upgrade(Upgrades, float)
	 */
	protected float projDmgMul = 1;
	/**
	 * How much to multiply the projectile speed by
	 * 
	 * @see #Upgrade(Upgrades, float)
	 */
	protected float projSpdMul = 1;

	/** The powerups the player has collected but not activated */
	public Map<PowerupType, Integer> collectedPowerups = new HashMap<>();
	/** The currently active powerups */
	public Map<PowerupType, Float> activePowerups = new HashMap<>();

	/**
	 * The higher the defense, the stronger the player, this is subtracted from the
	 * damage
	 */
	protected int defense = 1;

	protected float timeSinceLastHeal = 0;

	/** The type of projectile currently in use */
	public ProjectileData activeProjectileType;

	public PlayerBoat(GameController controller, Vector2 initialPosition) {
		super(controller, initialPosition, "img/entity/boat1.png");

		this.maxHP = 100;
		this.HP = this.maxHP;
		this.speed = 200;
		this.turnSpeed = 150;

		activeProjectileType = ProjectileData.STOCK;

	}

	@Override
	public void Update(float delta) {
		boolean rapidFire = activePowerups.containsKey(PowerupType.RAPIDFIRE);
		timeSinceLastShot += delta * (rapidFire ? 2 : 1);

		updatePowerups(delta);

		processInput(delta);

		if (isDead()) controller.gameOver();

	}

	public void updatePowerups(float delta) {
		// Subtract delta time from each active power up
		activePowerups.replaceAll((k, v) -> v - delta);
		// Remove active power up if it has run out
		activePowerups.entrySet().removeIf(e -> e.getValue() <= 0);
	}

	/**
	 * Processes keyboard and mouse inputs
	 * 
	 * @param delta the time since the last update in seconds
	 */
	public void processInput(float delta) {
		boolean up = input.isKeyPressed(Keys.W);
		boolean down = input.isKeyPressed(Keys.S);
		boolean left = input.isKeyPressed(Keys.A);
		boolean right = input.isKeyPressed(Keys.D);

		int movMul = activePowerups.containsKey(PowerupType.SPEED) ? 2 : 1;

		if (left) Turn(delta, 1);
		if (right) Turn(delta, -1);
		if (up) Move(delta, movMul);
		if (down) Move(delta, -movMul);

		// make sure we don't fire when hovering over a button and clicking
		// doesn't matter if we're over a button or not when pressing space
		boolean click = input.isButtonJustPressed(Buttons.LEFT)
				&& !controller.hud.hoveringOverButton;
		if ((click || input.isKeyJustPressed(Keys.SPACE)) && shotDelay <= timeSinceLastShot) {
			Shoot();
			timeSinceLastShot = 0;
		}

		processPowerupInput(delta);
	}

	public void processPowerupInput(float delta) {
		PowerupType[] powerups = PowerupType.values();
		if (input.isKeyJustPressed(Keys.NUM_1) || input.isKeyJustPressed(Keys.NUMPAD_1))
			activatePowerup(powerups[0]);
		if (input.isKeyJustPressed(Keys.NUM_2) || input.isKeyJustPressed(Keys.NUMPAD_2))
			activatePowerup(powerups[1]);
		if (input.isKeyJustPressed(Keys.NUM_3) || input.isKeyJustPressed(Keys.NUMPAD_3))
			activatePowerup(powerups[2]);
		if (input.isKeyJustPressed(Keys.NUM_4) || input.isKeyJustPressed(Keys.NUMPAD_4))
			activatePowerup(powerups[3]);
		if (input.isKeyJustPressed(Keys.NUM_5) || input.isKeyJustPressed(Keys.NUMPAD_5))
			activatePowerup(powerups[4]);
	}

	/**
	 * Method that executes when a collision is detected
	 * 
	 * @param other the other object, as a PhysicsObject to be generic
	 */
	@Override
	public void OnCollision(PhysicsObject other) {
		// how much damage to deal to the player
		float dmgToInflict = 0;

		if (other instanceof Projectile) { // check the type of object passed
			Projectile p = (Projectile) other;
			if (!p.isPlayerProjectile()) {
				p.kill();
				dmgToInflict = p.getDamage();
			}
		} else if (other instanceof College) {
			// End game if player crashes into college
			controller.gameOver();
		}

		// Deal damage if player is not invincible
		if (!isInvincible()) damage(dmgToInflict);
	}

	@Override
	public void damage(float dmg) {
		if (isInvincible()) return; // no damage if invincible
		dmg = Math.max(dmg - defense, 0); // subtract defense
		HP = MathUtils.clamp(HP - dmg, 0, maxHP);
	}

	/** @return {@code true} if invincibility powerup is active */
	public boolean isInvincible() {
		return activePowerups.containsKey(PowerupType.INVINCIBILITY);
	}

	@Override
	public void Shoot() {

		float dmgMul = getDamageMul();

		switch (activeProjectileType) {
		case RAY:
			shootRay(dmgMul);
			break;
		case STOCK:
		default:
			shootStock(dmgMul);
			break;
		}

	}

	public void shootRay(float dmgMul) {
		float angle = getAngleBetweenMouseAndBoat();
		angle += MathUtils.random(-5, 5); // randomize ray so it is not perfect
		angle = MathHelper.normalizeAngle(angle);

		ProjectileRay pr = new ProjectileRay(getCenter(), angle, activeProjectileType, true, 500f,
				dmgMul);
		pr.fireRay(controller.physicsObjects, 1);
		controller.rays.add(pr);
	}

	/**
	 * Calculates the angle between the player boat and the mouse pointer. It does
	 * this by unwrapping the cursor position with the controller camera.
	 * 
	 * @return The angle in degrees
	 */
	public float getAngleBetweenMouseAndBoat() {
		int mouseX = input.getX();
		int mouseY = input.getY();
		Vector3 pos3 = new Vector3(mouseX, mouseY, 0);
		controller.camera.unproject(pos3);
		Vector2 pos = new Vector2(pos3.x, pos3.y);

		return pos.sub(getCenter()).angleDeg();
	}

	public void shootStock(float dmgMul) {
		if (activePowerups.containsKey(PowerupType.STARBURSTFIRE)) {
			for (int i = 0; i < 360; i += 45) {
				Projectile burst = createProjectile(activeProjectileType, i, dmgMul, projSpdMul);
				controller.NewPhysicsObject(burst);
			}
		} else {
			Projectile projLeft = createProjectile(activeProjectileType, -90, dmgMul, projSpdMul);
			Projectile projRight = createProjectile(activeProjectileType, 90, dmgMul, projSpdMul);
			// Add the projectile to the GameController's physics objects list so it
			// receives updates
			controller.NewPhysicsObject(projLeft);
			controller.NewPhysicsObject(projRight);
		}
	}

	public float getDamageMul() {
		float dmgMul = activePowerups.containsKey(PowerupType.DAMAGE) ? 2 : 1;
		// multiply by the overall damage multiplier
		return dmgMul * projDmgMul * controller.getGameDifficulty().getPlayerDmgMul();
	}

	@Override
	public void Destroy() {
		controller.gameOver();
	}

	/**
	 * Allows the player to upgrade their boat
	 * 
	 * @param upgrade The requested upgrade
	 * 
	 * @param amount the amount to upgrade by
	 */
	public void Upgrade(Upgrades upgrade, float amount) {
		switch (upgrade) {
		case defense:
			defense += amount;
			break;
		case health:
			HP = MathUtils.clamp(HP + amount, 0, maxHP);
			break;
		case maxhealth:
			maxHP += amount;
			HP += amount; // Also heal the player, we're feeling generous.
			break;
		case projectiledamage:
			projDmgMul += amount;
			break;
		case projectilespeed:
			projSpdMul += amount;
			break;
		case speed:
			speed += amount;
			break;
		case turnspeed:
			turnSpeed += amount;
			break;
		}
	}

	public void Heal(int amount, float delta) {
		timeSinceLastHeal += delta;
		if (amount * timeSinceLastHeal >= 1) {
			HP += amount * timeSinceLastHeal;
			timeSinceLastHeal = 0;
			HP = MathUtils.clamp(HP, 0, maxHP);
		}
	}

	/**
	 * Activate a pre-collected powerup. Powerups of the same type cannot be
	 * activated if one is already active.
	 * 
	 * @param powerup the powerup to activate
	 * @return {@code true} if powerup is in the collection and was activated,
	 *         {@code false} otherwise
	 */
	public boolean activatePowerup(PowerupType powerup) {
		boolean canActivate = collectedPowerups.containsKey(powerup)
				&& !activePowerups.containsKey(powerup);
		if (canActivate || DebugUtils.FORCE_POWERUP) {
			activePowerups.put(powerup, powerup.getDefaultTime());

			collectedPowerups.replace(powerup, collectedPowerups.getOrDefault(powerup, 0) - 1);
			collectedPowerups.entrySet().removeIf(e -> e.getValue() <= 0);

			// TODO: change to use resource manager
			Gdx.audio.newSound(Gdx.files.internal(powerup.getActivationAudio())).play(1f,
					1f + MathUtils.random(-0.1f, 0.1f), 0f);
			return true;
		}
		return false;
	}

	/**
	 * Place a powerup into the collected powerup collection
	 * 
	 * @param powerup the powerup to collect
	 * @return {@code true} if powerup was received, {@code false} if player already
	 *         had maximum powerups stored
	 */
	public boolean receivePower(PowerupType powerup) {
		int amount = collectedPowerups.getOrDefault(powerup, 0);
		if (amount >= 2) return false;
		collectedPowerups.put(powerup, amount + 1);
		return true;
	}

}
