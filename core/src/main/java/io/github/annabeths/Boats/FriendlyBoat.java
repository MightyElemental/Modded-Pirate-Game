package io.github.annabeths.Boats;

import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Projectiles.Projectile;

/**
 * A type of boat that attacks enemy ships
 * 
 * @since Assessment 2
 * @author Hector Woods
 */
public class FriendlyBoat extends AttackBoat {

	public FriendlyBoat(GameController controller, Vector2 position) {
		super(controller, position, "img/entity/boat1.png");

		xpValue = 0; // friendly ships should not reward xp or plunder
		plunderValue = 0;

		this.HP = 75; // slightly weaker than enemy ships
		this.maxHP = 75;
		this.speed = 65;
		this.turnSpeed = 150;
		state = AIState.IDLE;
	}

	public EnemyBoat getNearestTarget() {
		EnemyBoat nearestTarget = null;
		float shortestDistance = 1000;
		for (PhysicsObject obj : controller.physicsObjects) {
			if (obj instanceof EnemyBoat) {
				float dst = obj.getCenter().dst(getCenter());
				if (dst < shortestDistance) {
					nearestTarget = (EnemyBoat) obj;
					shortestDistance = dst;
				}
			}
		}
		return nearestTarget;
	}

	@Override
	public void OnCollision(PhysicsObject other) {
		float dmgToInflict = 0;

		if (other instanceof Projectile) {
			Projectile p = (Projectile) other;
			if (!p.isPlayerProjectile()) {
				other.kill();
				dmgToInflict = p.getDamage();
			}
		} else if (other instanceof PlayerBoat) {
			Destroy();
		}
		damage(dmgToInflict);
	}
}
