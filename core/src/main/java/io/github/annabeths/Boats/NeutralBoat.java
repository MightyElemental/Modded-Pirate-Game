package io.github.annabeths.Boats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Projectiles.Projectile;

public class NeutralBoat extends AIBoat {

	public NeutralBoat(GameController controller, Vector2 initialPosition, Vector2 mapSize) {
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
		position = initialPosition.cpy();
		// Force the boat to set a new destination on initialization
		destination = getNewRandomValidTarget();

		sprite = new Sprite(new Texture(Gdx.files.internal("img/boat_neutral.png")));
		sprite.setSize(100, 50);
		sprite.setOrigin(50, 25);

		collisionPolygon.setPosition(position.x + GetCenterX() / 2,
				position.y - GetCenterY() / 2 - 10);
		collisionPolygon.setOrigin(25, 50);
		collisionPolygon.setRotation(rotation - 90);

		sprite.setPosition(initialPosition.x, initialPosition.y);

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
