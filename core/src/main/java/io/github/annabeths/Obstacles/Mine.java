package io.github.annabeths.Obstacles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.Boat;
import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;

/**
 * An obstacle that explodes when it collides with a Boat; dealing damage
 * @since Assessment 2
 * @author Hector Woods
 */
public class Mine extends ObstacleEntity {

	/**
	 * Constructor for Mine
	 * @param controller GameController
	 * @param position position of the mine
	 */
	public Mine(GameController controller, Vector2 position) {
		super(controller, position, "img/entity/mine.png", new Vector2(50, 50));
		Polygon poly = new Polygon(new float[] { 0, 25, 25, 50, 50, 25, 25, 0 });
		poly.setPosition(position.x - getLocalCenterX(), position.y + 25);
		poly.setOrigin(0, 0);
		poly.setRotation(rotation - 90);
		setCenter(position);
		this.collisionPolygon = poly;
	}

	/**
	 * Called when the Mine collides with another object.
	 * @param other the object collided with
	 */
	@Override
	public void OnCollision(PhysicsObject other) {
		boolean shouldDamage = true;
		if (other instanceof PlayerBoat) {
			shouldDamage = !((PlayerBoat) other).isInvincible();
			if (shouldDamage) {
				Gdx.audio.newSound(Gdx.files.internal("audio/misc/mine.mp3")).play(1f,
						1f + MathUtils.random(-0.1f, 0.1f), 0f);
			}
		}
		if (other instanceof Boat) {
			if (shouldDamage) ((Boat) other).damage(50);
			kill();
		}
	}

	@Override
	public void Update( float delta ) {

	}

}
