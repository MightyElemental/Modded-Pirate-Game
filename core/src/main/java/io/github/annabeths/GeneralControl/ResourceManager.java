package io.github.annabeths.GeneralControl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import io.github.annabeths.Collectables.PowerupType;

/**
 * Handles resource management of textures, audio, and fonts
 * 
 * @author James Burnell
 */
public class ResourceManager {

	public static AssetManager assets;

	public static BitmapFont font;
	public static BitmapFont debugFont;

	// Prevent instantiation
	private ResourceManager() {
	}

	/**
	 * Initialize resource manager with a specific {@link AssetManager}
	 * 
	 * @param assetMan the asset manager to use
	 */
	public static void init(AssetManager assetMan) {
		assets = assetMan;

		long time = DebugUtils.timeCodeMs(() -> {
			font = new BitmapFont(Gdx.files.internal("fonts/bobcat.fnt"), false);
			debugFont = new BitmapFont(Gdx.files.internal("fonts/cozette.fnt"), false);
			debugFont.getData().setScale(0.5f);
		});

		System.out.printf("Loaded fonts in %dms\n", time);

		time = DebugUtils.timeCodeMs(() -> {
			/* World Textures */
			loadWorldTexture("island.png");
			loadWorldTexture("grass.png");
			for (int i = 0; i < 3; i++) {
				loadWorldTexture("water/water" + (i + 1) + ".png");
			}
			for (int i = 0; i < 9; i++) {
				loadWorldTexture("castle/castle" + (i + 1) + ".png");
			}
			loadWorldTexture("castle/castle_dead.png");
			/* Entity Textures */
			loadEntityTexture("cannonball.png");
			loadEntityTexture("kraken_cannonball.png");
			loadEntityTexture("boat_neutral.png");
			loadEntityTexture("boat1.png");
			loadEntityTexture("boat2.png");
			loadEntityTexture("kraken1.png");
			loadEntityTexture("kraken2.png");
			loadEntityTexture("kraken3.png");
			/* UI Textures */
			loadUITexture("button.png");
			loadUITexture("background.png");
			loadUITexture("upgradebutton.png");
			/* Powerup Textures */
			for (PowerupType p : PowerupType.values()) {
				loadTexture(p.getTexture());
			}

			assets.finishLoading();
		});

		System.out.printf("Loaded textures in %dms\n", time);
	}

	public static void loadUITexture(String location) {
		loadTexture("ui/" + location);
	}

	public static void loadWorldTexture(String location) {
		loadTexture("img/world/" + location);
	}

	public static void loadEntityTexture(String location) {
		loadTexture("img/entity/" + location);
	}

	/**
	 * Queues a texture from a file location to be loaded
	 * 
	 * @param location the location of the texture
	 */
	public static void loadTexture(String location) {
		if (Gdx.files.internal(location).exists()) {
			assets.load(location, Texture.class);
		} else {
			Gdx.app.error("texture loader", location + " does not exist");
		}
	}

	/**
	 * Get a pre-loaded texture from a location
	 * 
	 * @param location the location of the texture
	 * @return The Texture object, or {@code null} if the location is null
	 * 
	 */
	public static Texture getTexture(String location) {
		if (location == null || assets == null) return null;
		if (assets.contains(location)) return assets.get(location);
		return assets.get(location);
	}

}
