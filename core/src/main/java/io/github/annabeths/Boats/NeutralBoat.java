package io.github.annabeths.Boats;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Collectables.Powerup;
import io.github.annabeths.Collectables.PowerupType;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Projectiles.Projectile;

public class NeutralBoat extends AIBoat {

	/**
	 * A boat that moves around but does not attack. Grants plunder and xp when killed.
	 * @author James Burnell
	 * @author Hector Woods
	 * @tt.updated Assessment 2
	 * @param controller the game controller
	 * @param initialPosition the position of the boat
	 */
	public NeutralBoat(GameController controller, Vector2 initialPosition) {
		super(controller, initialPosition, "img/entity/boat_neutral.png");

		xpValue = 20 * controller.getGameDifficulty().getPlayerXpMul();
		plunderValue = 25;

		this.HP = 100;
		this.maxHP = 100;
		this.speed = 75;
		this.turnSpeed = 150;
	}

	/**
	 * @author James Burnell
	 * @author Ben Faulker
	 * @tt.updated Assessment 2
	 */
	public void Destroy() {
		killOnNextTick = true;
		if (MathUtils.randomBoolean(0.2f)) {
			controller.NewPhysicsObject(new Powerup(PowerupType.randomPower(), getCenter()));
		}
	}

	/**
	 * Ignore, neutral boats do not shoot, but this must be defined since we are extending AIBoat
	 */
	public void Shoot() {

	}

	/**
	 * @author James Burnell
	 * @tt.updated Assessment 2
	 */
	public void OnCollision(PhysicsObject object) {
		float dmgToInflict = 0;
		// whether the object belongs to the player
		boolean objWasPlayer = false;

		if (object instanceof PlayerBoat) {
			// Hit by player, destroy
			((PlayerBoat) object).damage(50);
			Destroy();
			controller.addXp((getHealth() / getMaxHealth()) * xpValue);
			controller.addPlunder(plunderValue);
		} else if (object instanceof Projectile) {
			object.kill();
			Projectile p = (Projectile) object;
			objWasPlayer = p.isPlayerProjectile();
			dmgToInflict = p.getDamage();
		}

		if (objWasPlayer && getHealth() - dmgToInflict <= 0){
			controller.addXp(xpValue);
			controller.addPlunder(plunderValue);
		}
		damage(dmgToInflict);
	}
}
