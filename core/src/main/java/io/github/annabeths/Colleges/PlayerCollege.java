package io.github.annabeths.Colleges;

import static io.github.annabeths.GeneralControl.ResourceManager.font;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.FriendlyBoat;
import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;

/**
 * A friendly College that heals the player and allows them to buy upgrades with plunder.
 * When an EnemyCollege is destroyed it is "captured" and becomes a PlayerCollege.
 * @author James Burnell
 * @author Hector Woods
 * @tt.updated Assessment 2
 */
public class PlayerCollege extends College {

	/**
	 * The amount to heal the player's boat
	 * @see PlayerBoat#Heal(int, float)
	 */
	public int healAmount;
	/** Spawn a boat every n seconds */
	public float boatSpawnTime = 10;
	public float timeSinceLastSpawn = 0;

	public float splashTextCounter = 0;
	public float splashTextTime = 3;
	public GlyphLayout splashText;

	/**
	 * Creates a PlayerCollege based on an EnemyCollege. Used for when an enemy
	 * college is captured and converted into a player college.
	 * @param oldCol the enemy college to use as a base
	 */
	public PlayerCollege(EnemyCollege oldCol) {
		this(oldCol.position, oldCol.aliveTextureFile, oldCol.islandTextureFile, oldCol.gc, true);
	}

	/**
	 * Constructor for PlayerCollege.
	 * @param position position for the new college
	 * @param aliveTexture texture of the college
	 * @param islandTexture texture of the island beneath the college
	 * @param gc instance of GameController
	 * @param showSplashText boolean that determines whether to display the text "College Captured!"
	 */
	public PlayerCollege(Vector2 position, String aliveTexture, String islandTexture,
			GameController gc, boolean showSplashText) {
		super(position, aliveTexture, islandTexture, gc);
		healAmount = 15;
		range = 400;
		if (showSplashText) {
			splashText = new GlyphLayout(font, "College Captured!");
		}
	}

	/**
	 * Update the text beneath the college
	 * @param text the new text
	 */
	public void updateSplashText(String text) {
		if (splashText == null) return;
		splashText.setText(font, text);
	}

	/**
	 * PlayerCollege doesn't need to handle any collisions itself. The case of the
	 * PlayerBoat crashing into it is handled in the PlayerBoat class.
	 * @param ignored n/a
	 * @see PlayerBoat#OnCollision(PhysicsObject)
	 */
	@Override
	public void OnCollision(PhysicsObject ignored) {

	}

	/**
	 * Called once per frame
	 * @param delta time since last frame
	 */
	@Override
	public void Update(float delta) {
		PlayerBoat boat = gc.playerBoat;
		// if the player boat is in range, heal it
		if (isInRange(boat)) {
			boat.Heal(healAmount, delta);
		}
		checkForSpawnFriendlyBoat(delta);
		if (splashText != null) {
			splashTextCounter = splashTextCounter + delta;
			if (splashTextCounter > splashTextTime) {
				splashText = null;
			}
		}
	}

	/**
	 * Checks if a friendly boat should spawn. If it should spawn a friendly boat, a
	 * new {@link FriendlyBoat} object will be created in the GameController.
	 * 
	 * @param delta the time elapsed since the last update
	 */
	public void checkForSpawnFriendlyBoat(float delta) {
		timeSinceLastSpawn = timeSinceLastSpawn + delta;
		if (timeSinceLastSpawn > boatSpawnTime) {
			gc.NewPhysicsObject(
					new FriendlyBoat(gc, new Vector2(position.x + 150, position.y + 150)));
			timeSinceLastSpawn = 0;
		}
	}

	/**
	 * Draw the College's sprite.
	 * @param batch the SpriteBatch to draw the college's sprite
	 */
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
