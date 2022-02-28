package io.github.annabeths.Colleges;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
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
	public BitmapFont font;
	public GlyphLayout hpText;

	public EnemyCollege(Vector2 position, Texture aliveTexture, Texture islandTexture,
			GameController controller, ProjectileData projectileData, int maxHP) {
		super(position, aliveTexture, islandTexture, controller);

		deadSprite = new Sprite(new Texture(Gdx.files.internal("img/castle10.png")));
		deadSprite.setPosition(position.x, position.y);
		deadSprite.setSize(100, 100);

		this.maxHP = maxHP;
		HP = maxHP;
		range = 500;
		fireRate = 1.5f;
		projectileType = projectileData;
		collisionPolygon = new Polygon(new float[] { 0, 0, 100, 0, 100, 100, 0, 100 });
		collisionPolygon.setPosition(position.x, position.y);
		font = new BitmapFont(Gdx.files.internal("fonts/bobcat.fnt"), false);
		hpText = new GlyphLayout();
		updateHpText();
	}

	public void updateHpText() {
		hpText.setText(font, HP + "/" + maxHP);
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
