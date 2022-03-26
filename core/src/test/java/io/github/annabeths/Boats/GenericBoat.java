package io.github.annabeths.Boats;

import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.GameGenerics.PhysicsObject;

/**
 * A non-abstract version of Boat to be used for testing
 * 
 * @author James Burnell
 */
public class GenericBoat extends Boat {

	public GenericBoat(Vector2 position) {
		super(null, position, null);
	}

	@Override
	public void Update(float delta) {
	}

	@Override
	void Shoot() {
	}

	@Override
	void Destroy() {
	}

	@Override
	public void OnCollision(PhysicsObject other) {
	}

}
