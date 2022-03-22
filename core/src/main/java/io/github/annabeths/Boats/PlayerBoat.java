package io.github.annabeths.Boats;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.github.annabeths.Collectables.PowerupType;
import io.github.annabeths.Colleges.College;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameGenerics.Upgrades;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Projectiles.Projectile;
import io.github.annabeths.Projectiles.ProjectileData;
import io.github.annabeths.Projectiles.ProjectileRay;

/**
 * @author Leif Kemp
 * @author Anna Singleton
 * @author James Burnell
 * @author Ben Faulkner
 */
public class PlayerBoat extends Boat {

	/**
	 * How much to multiply the projectile damage by
	 * 
	 * @see #Upgrade(Upgrades, float)
	 */
	float projDmgMul = 1;
	/**
	 * How much to multiply the projectile speed by
	 * 
	 * @see #Upgrade(Upgrades, float)
	 */
	float projSpdMul = 1;

	public Map<PowerupType, Float> activePowerups = new HashMap<>();

	/**
	 * The higher the defense, the stronger the player, this is subtracted from the
	 * damage
	 */
	int defense = 1;

	float timeSinceLastHeal = 0;

	/** The type of projectile currently in use */
	public ProjectileData activeProjectileType;

	public PlayerBoat(GameController controller, Vector2 initialPosition) {
		super(controller, initialPosition, "img/entity/boat1.png");

		this.HP = 100;
		this.maxHP = 100;
		this.speed = 200;
		this.turnSpeed = 150;

		activeProjectileType = ProjectileData.STOCK;

	}

	@Override
	public void Update(float delta) {
		boolean rapidFire = activePowerups.containsKey(PowerupType.RAPIDFIRE);
		timeSinceLastShot += delta * (rapidFire ? 2 : 1);

		// Subtract delta time from each active power up
		activePowerups.replaceAll((k, v) -> v - delta);
		// Remove active power up if it has run out
		activePowerups.entrySet().removeIf(e -> e.getValue() <= 0);

		boolean up = Gdx.input.isKeyPressed(Input.Keys.W);
		boolean down = Gdx.input.isKeyPressed(Input.Keys.S);
		boolean left = Gdx.input.isKeyPressed(Input.Keys.A);
		boolean right = Gdx.input.isKeyPressed(Input.Keys.D);

		int movMul = activePowerups.containsKey(PowerupType.SPEED) ? 2 : 1;

		if (left) Turn(delta, 1);
		if (right) Turn(delta, -1);
		if (left || right || up || down) Move(delta, movMul);

		// make sure we don't fire when hovering over a button and clicking
		// doesn't matter if we're over a button or not when pressing space
		if (((Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
				&& !controller.hud.hoveringOverButton)
				|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
				&& shotDelay <= timeSinceLastShot) {
			Shoot();
			timeSinceLastShot = 0;
		}

		if (HP <= 0) {
			// the player is dead
			controller.gameOver();
		}

	}

	/**
	 * Method that executes when a collision is detected
	 * 
	 * @param other the other object, as a PhysicsObject to be generic
	 */
	@Override
	public void OnCollision(PhysicsObject other) {
		boolean isInvincible = isInvincible();

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
		} else if (other instanceof Boat) {
			// Damage player if collides with boat
			dmgToInflict = 50;
		}

		// Deal damage if player is not invincible
		if (dmgToInflict != 0 && !isInvincible) damage(dmgToInflict - defense);
	}

	/** @return {@code true} if player has invincibility powerup */
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

	private void shootRay(float dmgMul) {
		float angle = getAngleBetweenMouseAndBoat();

		ProjectileRay pr = new ProjectileRay(getCenter(), angle, activeProjectileType, true, 500f,
				dmgMul);
		pr.fireRay(controller.physicsObjects);
		controller.rays.add(pr);
	}

	/**
	 * Calculates the angle between the player boat and the mouse pointer. It does
	 * this by unwrapping the cursor position with the controller camera.
	 * 
	 * @return The angle in degrees
	 */
	public float getAngleBetweenMouseAndBoat() {
		int mouseX = Gdx.input.getX();
		int mouseY = Gdx.input.getY();
		Vector3 pos3 = new Vector3(mouseX, mouseY, 0);
		controller.map.camera.unproject(pos3);
		Vector2 pos = new Vector2(pos3.x, pos3.y);

		return pos.sub(getCenter()).angleDeg();
	}

	private void shootStock(float dmgMul) {
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

	private float getDamageMul() {
		float dmgMul = activePowerups.containsKey(PowerupType.DAMAGE) ? 3 : 1;
		// multiply by the overall damage multiplier
		return dmgMul * projDmgMul;
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
			HP = MathUtils.clamp((int) (HP + amount), 0, maxHP);
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

	@Override
	public void Draw(SpriteBatch batch) {
		sprite.draw(batch);

	}

	public void Heal(int amount, float delta) {
		timeSinceLastHeal += delta;
		if (amount * timeSinceLastHeal >= 1) {
			HP += amount * timeSinceLastHeal;
			timeSinceLastHeal = 0;
			HP = MathUtils.clamp(HP, 0, maxHP);
		}
	}

	public void receivePower(PowerupType powerup) {
		activePowerups.put(powerup, powerup.getDefaultTime());
	}

}
