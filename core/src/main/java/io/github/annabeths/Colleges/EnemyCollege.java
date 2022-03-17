package io.github.annabeths.Colleges;

import static io.github.annabeths.GeneralControl.ResourceManager.font;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.GeneralControl.DebugUtils;
import io.github.annabeths.Projectiles.Projectile;
import io.github.annabeths.Projectiles.ProjectileData;

public class EnemyCollege extends College {

	/**
	 * The inaccuracy when firing cannonballs. Measured in degrees and reflected
	 * about the center. i.e. the cannon could be fired anywhere between -x and x
	 * where x is the inaccuracy.
	 */
	public float shootingInaccuracy = 10f;
	public float timeSinceLastShot = 0;

	public ProjectileData projectileType;
	public GlyphLayout hpText;

	public EnemyCollege(Vector2 position, String aliveTexture, String islandTexture,
			GameController controller, ProjectileData projectileData, int maxHP) {
		super(position, aliveTexture, islandTexture, controller);

		deadSprite = initSprite("img/world/castle/castle_dead.png", position,
				new Vector2(100, 100));

		this.maxHP = maxHP;
		HP = maxHP;
		range = 500;
		fireRate = 1.5f;
		projectileType = projectileData;
		hpText = new GlyphLayout();
		// updateHpText();
	}

	public void updateHpText() {
		hpText.setText(font, String.format("%.0f/%.0f", HP, maxHP));
	}

	@Override
	public void OnCollision(PhysicsObject other) {
		// if the enemy college is hit by a projectile
		if (other instanceof Projectile && HP > 0) {
			Projectile p = (Projectile) other;
			if (p.isPlayerProjectile) { // if its a player projectile
				p.killOnNextTick = true;
				if (!invulnerable) {
					HP -= p.damage;
					updateHpText();
					if (HP <= 0) gc.CollegeDestroyed();
				} else {
					hpText.setText(font, "RESISTED, destroy other colleges first!");
				}
			}
		}
	}

	public void Update(float delta) {
		if (HP > 0) {
			if (timeSinceLastShot < fireRate) {
				timeSinceLastShot += delta;
			} // increase the time on the timer to allow for fire rate calculation

			PlayerBoat boat = gc.playerBoat;
			// is the player boat in range
			if (isInRange(boat)) {
				if (timeSinceLastShot >= fireRate) {
					ShootAt(boat.getCenter());
					timeSinceLastShot = 0;
				}
			}
		}
	}

	public void Draw(SpriteBatch batch) {
		islandSprite.draw(batch);
		if (HP > 0) {
			sprite.draw(batch);
			font.draw(batch, hpText, getCenterX() - hpText.width / 2,
					position.y - hpText.height / 2);
		} else {
			deadSprite.draw(batch);
		}
	}

	void ShootAt(Vector2 target) {
		// If fire is disabled, skip calculation.
		if (!DebugUtils.ENEMY_COLLEGE_FIRE) return;
		/*
		 * calculate the shot angle by getting a vector from the center of the college
		 * to the target. Convert to degrees for the inaccuracy calculation.
		 */
		Vector2 directionVec = target.cpy().sub(getCenter());
		float shotAngle = directionVec.angleDeg();

		shotAngle += MathUtils.random(-shootingInaccuracy, shootingInaccuracy);

		/*
		 * instantiate a new bullet and pass a reference to the gamecontroller so it can
		 * be updated and drawn
		 */
		gc.NewPhysicsObject(new Projectile(getCenter(), shotAngle, projectileType, false));

	}

	public void becomeVulnerable() {
		invulnerable = false;
		updateHpText();
	}

}
