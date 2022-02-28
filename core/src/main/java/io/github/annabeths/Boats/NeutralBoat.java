package io.github.annabeths.Boats;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Projectiles.Projectile;

public class NeutralBoat extends AIBoat {

	public NeutralBoat(GameController controller, Vector2 initialPosition, Vector2 mapSize) {
		super(initialPosition, "img/boat_neutral.png");
		// copy the array so we don't modify the original
		this.mapSize = mapSize.cpy();
		this.controller = controller;

		xpValue = 25;
		plunderValue = 25;

		this.HP = 100;
		this.maxHP = 100;
		this.speed = 75;
		this.turnSpeed = 150;

		this.initialPosition = initialPosition.cpy();
		// Force the boat to set a new destination on initialization
		destination = getNewRandomValidTarget();

		// use a libgdx array of vectors because its an easy way to check point x box
		// collision
		mapBounds = new Array<Vector2>(true, 4);
		mapBounds.add(new Vector2(0, 0));
		mapBounds.add(new Vector2(mapSize.x, 0));
		mapBounds.add(new Vector2(mapSize.x, mapSize.y));
		mapBounds.add(new Vector2(0, mapSize.y));
	}

	public void Update(float delta) {
		MoveToDestination(delta);
	}

	public void Destroy() {
		killOnNextTick = true;
	}

	public void Shoot() {
		// Ignore, neutral boats do not shoot, but this must be defined
	}

	public void OnCollision(PhysicsObject object) {
		if (object instanceof PlayerBoat) {
			// Hit by player, destroy and add XP
			controller.xp += xpValue;
			controller.plunder += plunderValue;
			Destroy();
		} else if (object instanceof Projectile) {
			object.killOnNextTick = true;
			Projectile p = (Projectile) object;
			if (p.isPlayerProjectile) controller.xp += xpValue;
			Destroy();
		}
	}
}
