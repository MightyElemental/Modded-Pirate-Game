package io.github.annabeths.Boats;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Collectables.Powerup;
import io.github.annabeths.Collectables.PowerupType;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Projectiles.Projectile;

public class NeutralBoat extends AIBoat {

	public NeutralBoat(GameController controller, Vector2 initialPosition) {
		super(controller, initialPosition, "img/entity/boat_neutral.png");

		xpValue = 20;
		plunderValue = 25;

		this.HP = 100;
		this.maxHP = 100;
		this.speed = 75;
		this.turnSpeed = 150;
	}

	public void Update(float delta) {
		super.Update(delta);
	}

	public void Destroy() {
		killOnNextTick = true;
		if (MathUtils.randomBoolean(0.2f)) {
			controller.NewPhysicsObject(new Powerup(PowerupType.randomPower(), getCenter()));
		}
	}

	public void Shoot() {
		// Ignore, neutral boats do not shoot, but this must be defined
	}

	public void OnCollision(PhysicsObject object) {
		float dmgToInflict = 0;
		// whether or not the object belongs to the player
		boolean objWasPlayer = false;

		if (object instanceof PlayerBoat) {
			// Hit by player, destroy and add XP
			controller.xp += xpValue;
			controller.plunder += plunderValue;
			Destroy();
		} else if (object instanceof Projectile) {
			object.kill();
			Projectile p = (Projectile) object;
			if (p.isPlayerProjectile()) objWasPlayer = true;
			dmgToInflict = p.getDamage();
		}

		if (objWasPlayer) controller.xp += (dmgToInflict / maxHP) * xpValue;
		damage(dmgToInflict);
	}
}
