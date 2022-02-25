package io.github.annabeths.Boats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.annabeths.Colleges.College;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameGenerics.Upgrades;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Projectiles.Projectile;

public class PlayerBoat extends Boat {
	float projectileDamageMultiplier = 1;
	float projectileSpeedMultiplier = 1;
	float powerTimer = 0;
	int powerID = 0;

	/**
	 * The higher the defense, the stronger the player, this is subtracted from the
	 * damage
	 */
	int defense = 1;

	float timeSinceLastHeal = 0;

	public PlayerBoat(GameController controller, Vector2 initialPosition, Vector2 mapSize) {
		this.controller = controller;

		this.HP = 100;
		this.maxHP = 100;
		this.speed = 200;
		this.turnSpeed = 150;
		position = initialPosition;

		collisionPolygon.setPosition(initialPosition.x + GetCenterX() / 2,
				initialPosition.y - GetCenterY() / 2 - 10);
		collisionPolygon.setOrigin(25, 50);
		collisionPolygon.setRotation(rotation - 90);

		sprite.setPosition(initialPosition.x, initialPosition.y);

		this.mapSize = mapSize;
		mapBounds = new Array<Vector2>(true, 4);
		mapBounds.add(new Vector2(0, 0));
		mapBounds.add(new Vector2(mapSize.x, 0));
		mapBounds.add(new Vector2(mapSize.x, mapSize.y));
		mapBounds.add(new Vector2(0, mapSize.y));
	}

	@Override
	public void Update(float delta) {
		timeSinceLastShot += delta;
		
		if (powerTimer > 0) {
			powerTimer -= delta;
		} else {
			powerID = 0;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			Move(delta, 1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			Move(delta, -1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			Turn(delta, -1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			Turn(delta, 1);
		}

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
		if (other instanceof Projectile) { // check the type of object passed
			Projectile p = (Projectile) other;
			if (!p.isPlayerProjectile) {
				p.killOnNextTick = true;
				if (powerID != 1) {
					HP -= (p.damage - defense);
				}
			}
		} else if (other instanceof College) {
			// End game if player crashes into college
			controller.gameOver();
		} else if (other instanceof NeutralBoat) {
			// Damage player if collides with boat
			if (powerID != 1) {
				HP -= 50;
			}
		}
	}

	@Override
	public void Shoot() {
		Projectile proj = new Projectile(
				new Vector2(GetCenterX() + position.x, GetCenterY() + position.y), rotation,
				controller.projectileHolder.stock, true, projectileDamageMultiplier,
				projectileSpeedMultiplier);
		// Add the projectile to the GameController's physics objects list so it
		// receives updates
		controller.NewPhysicsObject(proj);
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
			HP = (int) Math.min(maxHP, HP + amount); // Keeps HP from exceeding max
			break;
		case maxhealth:
			maxHP += amount;
			HP += amount; // Also heal the player, we're feeling generous.
			break;
		case projectiledamage:
			projectileDamageMultiplier += amount;
			break;
		case projectilespeed:
			projectileSpeedMultiplier += amount;
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
			if (HP > maxHP) {
				HP = maxHP;
			}
		}
	}
	
	public void ReceivePower(int power) {
		powerID = power;
		switch (powerID) {
		case 1:
			//invincibility
			powerTimer = 10;
			break;	
		}
	}
}
