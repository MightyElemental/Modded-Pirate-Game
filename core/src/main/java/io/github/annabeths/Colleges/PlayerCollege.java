package io.github.annabeths.Colleges;

import static io.github.annabeths.GeneralControl.ResourceManager.font;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.FriendlyBoat;
import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;

public class PlayerCollege extends College {

	/**
	 * The amount to heal the player's boat
	 * 
	 * @see PlayerBoat#Heal(int, float)
	 */
	public int healAmount;
	/** Spawn a boat every n seconds */
	public float boatSpawnTime = 25;
	public float timeSinceLastSpawn = 0;

	public float splashTextCounter = 0;
	public float splashTextTime = 3;
	public GlyphLayout splashText;

	public PlayerCollege(Vector2 position, String aliveTexture, String islandTexture,
			GameController gc, boolean showSplashText) {
		super(position, aliveTexture, islandTexture, gc);
		healAmount = 15;
		range = 400;
		if (showSplashText) {
			splashText = new GlyphLayout();
			splashText.setText(font, "College Captured!");
		}
	}

	public void updateSplashText(String text) {
		splashText.setText(font, text);
	}

	/**
	 * PlayerCollege doesn't need to handle any collisions itself. The case of the
	 * PlayerBoat crashing into it is handled in the PlayerBoat class.
	 * 
	 * @param other n/a
	 * @see PlayerBoat#OnCollision(PhysicsObject)
	 */
	@Override
	public void OnCollision(PhysicsObject other) {
	}

	@Override
	public void Update(float delta) {
		PlayerBoat boat = gc.playerBoat;
		// if the player boat is in range, heal it
		if (isInRange(boat)) {
			boat.Heal(healAmount, delta);
		} else { // Don't spawn while the player is too close to prevent collisions
			checkForSpawnFriendlyBoat(delta);
		}
		if (splashText != null) {
			splashTextCounter = splashTextCounter + delta;
			if (splashTextCounter > splashTextTime) {
				splashText = null;
			}
		}
	}

	public void checkForSpawnFriendlyBoat(float delta) {
		timeSinceLastSpawn = timeSinceLastSpawn + delta;
		if (timeSinceLastSpawn > boatSpawnTime) {
			gc.physicsObjects
					.add(new FriendlyBoat(gc, new Vector2(position.x + 150, position.y + 150)));
			timeSinceLastSpawn = 0;
		}
	}

	@Override
	public void Draw(SpriteBatch batch) {
		islandSprite.draw(batch);
		sprite.draw(batch);
		if (splashText != null) {
			font.draw(batch, splashText, getCenterX() - splashText.width / 2,
					position.y - splashText.height / 2);
		}
	}

}
