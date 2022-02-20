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

	int damage;
	float shootingInaccuracy = 10f; // in degrees (each side)
	float fireRate = 1.5f;
	float timeSinceLastShot = 0;
	GameController gc;
	ProjectileData projectileType;
	BitmapFont font;
	GlyphLayout hpText;
	int maxHP;
	public int HP;
	public boolean invulnerable;

	public EnemyCollege(Vector2 position, Texture aliveTexture, Texture islandTexture,
			GameController controller, ProjectileData projectileData, int maxHP) {
		aliveSprite = new Sprite(aliveTexture);
		aliveSprite.setPosition(position.x, position.y);
		aliveSprite.setSize(100, 100);
		deadSprite = new Sprite(new Texture(Gdx.files.internal("img/castle10.png")));
		deadSprite.setPosition(position.x, position.y);
		deadSprite.setSize(100, 100);
		islandSprite = new Sprite(islandTexture);
		islandSprite.setCenter(aliveSprite.getX() + 5, aliveSprite.getY() + 5);
		islandSprite.setSize(120, 120);
		this.position = position;
		range = 500;
		gc = controller;
		projectileType = projectileData;
		collisionPolygon = new Polygon(new float[] { 0, 0, 100, 0, 100, 100, 0, 100 });
		collisionPolygon.setPosition(position.x, position.y);
		this.maxHP = maxHP;
		HP = maxHP;
		font = new BitmapFont(Gdx.files.internal("fonts/bobcat.fnt"), false);
		hpText = new GlyphLayout();
		hpText.setText(font, HP + "/" + maxHP);
	}

	@Override
	public void OnCollision(PhysicsObject other) {
		// if the enemy college is hit by a projectile
		if (other.getClass() == Projectile.class && HP > 0) {
			Projectile p = (Projectile) other;
			if (p.isPlayerProjectile) { // if its a player projectile
				p.killOnNextTick = true;
				if (!invulnerable) {
					HP -= p.damage;
					hpText.setText(font, HP + "/" + maxHP);
					if (HP <= 0) gc.CollegeDestroyed();
				} else
					hpText.setText(font, "RESISTED, destroy other colleges first!");
			}
		}
	}

	public void Update(float delta, PhysicsObject playerBoat) {
		if (HP > 0) {
			if (timeSinceLastShot < fireRate) {
				timeSinceLastShot += delta;
			} // increase the time on the timer to allow for fire rate calculation

			PlayerBoat boat = (PlayerBoat) playerBoat;
			// is the player boat in range
			if (isInRange(boat)) {
				if (timeSinceLastShot >= fireRate) {
					ShootAt(new Vector2(boat.position.x, boat.position.y));
					timeSinceLastShot = 0;
				}
			}
		}
	}

	public void Draw(SpriteBatch batch) {
		islandSprite.draw(batch);
		if (HP > 0) {
			aliveSprite.draw(batch);
			font.draw(batch, hpText, aliveSprite.getWidth() / 2 + position.x - hpText.width / 2,
					position.y - hpText.height / 2);
		} else {
			deadSprite.draw(batch);
		}
	}

	void ShootAt(Vector2 target) {
		/*
		 * calculate the shot angle by getting a vector from the centre of the college
		 * to the target. Convert to degrees for the inaccuracy calculation.
		 */
		float shotAngle = (float) Math
				.toDegrees(Math.atan2(target.y - (position.y + aliveSprite.getHeight() / 2),
						target.x - (position.x + aliveSprite.getWidth() / 2)));

		/*
		 * Inaccuracy calculation works by rd.nextfloat() gets a pseudorandom float from
		 * 0-1. We multiply it by 2* the shooting inaccuracy to get the right width of
		 * distribution then - the shooting inaccuracy to center the distribution on 0
		 */
		shotAngle += (MathUtils.random.nextFloat() * shootingInaccuracy * 2) - (shootingInaccuracy);

		/*
		 * instantiate a new bullet and pass a reference to the gamecontroller so it can
		 * be updated and drawn
		 */
		gc.NewPhysicsObject(new Projectile(
				new Vector2(position.x + aliveSprite.getWidth() / 2,
						position.y + aliveSprite.getHeight() / 2),
				shotAngle, projectileType, false));

	}

	public void becomeVulnerable() {
		invulnerable = false;
		hpText.setText(font, HP + "/" + maxHP);
	}

}
