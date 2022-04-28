package io.github.annabeths.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.GameGenerics.GameObject;
import io.github.annabeths.GameScreens.GameController;

/**
 * @author James Burnell
 * @tt.updated Assessment 2
 */
public class GameMap extends GameObject {

	/**
	 * How many units should there be between the colleges and the edge of the world
	 */
	public static final float BORDER_BRIM = 300;

	private GameController gc;

	public WaterBackground bg;

	public static int MAP_WIDTH = 3000, MAP_HEIGHT = 3000;

	/** Defines the bounds of the map */
	private static Rectangle mapBounds = new Rectangle(0, 0, MAP_WIDTH, MAP_HEIGHT);

	public GameMap(GameController gc) {
		this.gc = gc;

		bg = new WaterBackground(MAP_WIDTH, MAP_HEIGHT);
	}

	@Override
	public void Update(float delta) {
		// center the camera on the player
		Vector2 camPos = gc.playerBoat.getCenter();

		// if the screen is wider than the map, then just center on the map
		if (Gdx.graphics.getWidth() >= mapBounds.width) {
			camPos.x = mapBounds.width / 2;
		} else {
			// else clamp the camera to only show the map, and try to center on the player
			camPos.x = MathUtils.clamp(camPos.x, gc.camera.viewportWidth / 2,
					mapBounds.width - gc.camera.viewportWidth / 2);

		}
		camPos.y = MathUtils.clamp(camPos.y, gc.camera.viewportHeight / 2,
				mapBounds.height - gc.camera.viewportHeight / 2);
		gc.camera.position.set(camPos.x, camPos.y, 0);
		gc.camera.update();
		bg.Update(delta);
	}

	/**
	 * Test if a point is within the bounds of the map.
	 * 
	 * @param point the point to test
	 * @return {@code true} if the point is within bounds, {@code false} otherwise
	 * @author James Burnell
	 */
	public static boolean isPointInBounds(Vector2 point) {
		return mapBounds.contains(point);
	}

	public static float getMapWidth() {
		return mapBounds.width;
	}

	public static float getMapHeight() {
		return mapBounds.height;
	}

	public static Vector2 getRandomPointInBounds() {
		return new Vector2(MathUtils.random((int) getMapHeight()),
				MathUtils.random((int) getMapWidth()));
	}

	@Override
	public void Draw(SpriteBatch batch) {
		bg.Draw(batch);
	}

}
