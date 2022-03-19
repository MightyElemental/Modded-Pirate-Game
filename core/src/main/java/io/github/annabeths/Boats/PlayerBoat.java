package io.github.annabeths.Boats;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Collectables.PowerupType;
import io.github.annabeths.Colleges.College;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameGenerics.Upgrades;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Projectiles.Projectile;
import io.github.annabeths.Projectiles.ProjectileData;

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

	public PlayerBoat(GameController controller, Vector2 initialPosition, Vector2 mapSize) {
		super(controller, initialPosition, "img/boat1.png");

		this.HP = 100;
		this.maxHP = 100;
		this.speed = 200;
		this.turnSpeed = 150;

	}

	@Override
	public void Update(float delta) {
		timeSinceLastShot += delta;
		// TODO: Rewrite to change shotDelay instead
		if (activePowerups.containsKey(PowerupType.RAPIDFIRE)) {
			timeSinceLastShot += (delta * 2);
		}

		// Subtract delta time from each active power up
		activePowerups.replaceAll((k, v) -> v - delta);
		// Remove active power up if it has run out
		activePowerups.entrySet().removeIf(e -> e.getValue() <= 0);

		boolean up = Gdx.input.isKeyPressed(Input.Keys.W);
		boolean down = Gdx.input.isKeyPressed(Input.Keys.S);
		boolean left = Gdx.input.isKeyPressed(Input.Keys.A);
		boolean right = Gdx.input.isKeyPressed(Input.Keys.D);

		int movMul = activePowerups.containsKey(PowerupType.SPEED) ? 2 : 1;

		if (left) Turn(delta, movMul);
		if (right) Turn(delta, movMul * -1);
		if (left || right || up || down) Move(delta, movMul);

		// make sure we don't fire when hovering over a button and clicking
		// doesn't matter if we're over a button or not when pressing space
		if (((Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && !controller.hud.hoveringOverButton)
				|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) && shotDelay <= timeSinceLastShot) {
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
		if (other instanceof Projectile) { // check the type of object passed
			Projectile p = (Projectile) other;
			if (!p.isPlayerProjectile) {
				p.killOnNextTick = true;
				if (!activePowerups.containsKey(PowerupType.INVINCIBILITY)) {
					HP -= (p.damage - defense);
				}
			}
		} else if (other instanceof College) {
			// End game if player crashes into college
			controller.gameOver();
		} else if (other instanceof Boat) {
			// Damage player if collides with boat
			if (!activePowerups.containsKey(PowerupType.INVINCIBILITY)) {
				HP -= 50;
			}
		}
	}

	@Override
	public void Shoot() {
		float dmgMul = activePowerups.containsKey(PowerupType.DAMAGE) ? 3 : 1;
		// multiply by the overall damage multiplier
		dmgMul *= projDmgMul;
		// the projectile type to shoot
		ProjectileData pd = controller.projectileHolder.stock;

		Projectile projLeft = createProjectile(pd, -90, dmgMul, projSpdMul);
		Projectile projRight = createProjectile(pd, 90, dmgMul, projSpdMul);
		// Add the projectile to the GameController's physics objects list so it
		// receives updates
		controller.NewPhysicsObject(projLeft);
		controller.NewPhysicsObject(projRight);

		if (activePowerups.containsKey(PowerupType.STARBURSTFIRE)) {
			Projectile burst1 = createProjectile(pd, -45, dmgMul, projSpdMul);
			Projectile burst2 = createProjectile(pd, -135, dmgMul, projSpdMul);
			Projectile burst3 = createProjectile(pd, 0, dmgMul, projSpdMul);
			Projectile burst4 = createProjectile(pd, 45, dmgMul, projSpdMul);
			Projectile burst5 = createProjectile(pd, 135, dmgMul, projSpdMul);
			Projectile burst6 = createProjectile(pd, 180, dmgMul, projSpdMul);
			controller.NewPhysicsObject(burst1);
			controller.NewPhysicsObject(burst2);
			controller.NewPhysicsObject(burst3);
			controller.NewPhysicsObject(burst4);
			controller.NewPhysicsObject(burst5);
			controller.NewPhysicsObject(burst6);
		}
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
