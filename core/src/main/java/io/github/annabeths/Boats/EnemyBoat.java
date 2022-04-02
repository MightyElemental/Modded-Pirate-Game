package io.github.annabeths.Boats;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Collectables.Powerup;
import io.github.annabeths.Collectables.PowerupType;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Projectiles.Projectile;

/**
 * A boat that attacks the player
 * 
 * @author James Burnell
 */
public class EnemyBoat extends AttackBoat {

	public EnemyBoat(GameController controller, Vector2 position) {
		super(controller, position, "img/entity/boat2.png");

		xpValue = 150;
		plunderValue = 100;

		this.HP = 100;
		this.maxHP = 100;
		this.speed = 75;
		this.turnSpeed = 150;
		// uncomment for fun
		// this.shotDelay = 0.01f;
	}

	public Boat getNearestTarget() {
		if (controller.playerBoat.getCenter().dst(getCenter()) < approachRange) {
			return controller.playerBoat;
		}

		FriendlyBoat nearestTarget = null;
		float shortestDistance = 1000;
		for (PhysicsObject obj : controller.physicsObjects) {
			if (obj instanceof FriendlyBoat) {
				float dst = obj.getCenter().dst(getCenter());
				if (dst < shortestDistance) {
					nearestTarget = (FriendlyBoat) obj;
					shortestDistance = dst;
				}
			}
		}
		return nearestTarget;
	}

	@Override
	public void Destroy() {
		killOnNextTick = true;
		if (MathUtils.randomBoolean(0.8f)) {
			controller.NewPhysicsObject(new Powerup(PowerupType.randomPower(), getCenter()));
		}
	}

	@Override
	public void OnCollision(PhysicsObject other) {
		float dmgToInflict = 0;
		// whether or not the object belongs to the player
		boolean objWasPlayer = false;

		if (other instanceof Projectile) {
			Projectile p = (Projectile) other;
			if (p.isPlayerProjectile()) {
				objWasPlayer = true;
				other.kill();
				dmgToInflict = p.getDamage();
			}
		} else if (other instanceof PlayerBoat) {
			// Hit by player, destroy and add XP
			controller.xp += xpValue;
			controller.plunder += plunderValue;
			Destroy();
		}

		if (objWasPlayer) controller.xp += (dmgToInflict / maxHP) * xpValue;
		damage(dmgToInflict);
	}

}
