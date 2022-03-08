package io.github.annabeths.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.GameGenerics.GameObject;

public class GameMap extends GameObject {

	/**
	 * How many units should there be between the colleges and the edge of the world
	 */
	public static final float BORDER_BRIM = 300;

	public OrthographicCamera camera;
	public PlayerBoat boat;
	public WaterBackground bg;

	/** Defines the bounds of the map */
	private Rectangle mapBounds;

	public GameMap(int screenHeight, int screenWidth, PlayerBoat boat, SpriteBatch batch,
			int mapWidth, int mapHeight) {
		camera = new OrthographicCamera();
		camera.viewportHeight = screenHeight;
		camera.viewportWidth = screenWidth;
		this.boat = boat;
		bg = new WaterBackground(mapWidth, mapHeight);

		mapBounds = new Rectangle(0, 0, mapWidth, mapHeight);
	}

	@Override
	public void Update(float delta) {
		// center the camera on the player
		Vector2 camPos = boat.getCenter();

		// if the screen is wider than the map, then just center on the map
		if (Gdx.graphics.getWidth() >= mapBounds.width) {
			camPos.x = mapBounds.width / 2;
		} else {
			// else clamp the camera to only show the map, and try to center on the player
			camPos.x = MathUtils.clamp(camPos.x, camera.viewportWidth / 2,
					mapBounds.width - camera.viewportWidth / 2);

		}
		camPos.y = MathUtils.clamp(camPos.y, camera.viewportHeight / 2,
				mapBounds.height - camera.viewportHeight / 2);
		camera.position.set(camPos.x, camPos.y, 0);
		camera.update();
		bg.Update(delta);
	}

	/**
	 * Test if a point is within the bounds of the map.
	 * 
	 * @param point the point to test
	 * @return {@code true} if the point is within bounds, {@code false} otherwise
	 * @author James Burnell
	 */
	public boolean isPointInBounds(Vector2 point) {
		return mapBounds.contains(point);
	}

	public float getMapWidth() {
		return mapBounds.width;
	}

	public float getMapHeight() {
		return mapBounds.height;
	}

	public Vector2 getRandomPointInBounds() {
		return new Vector2(MathUtils.random((int) getMapHeight()),
				MathUtils.random((int) getMapWidth()));
	}

	@Override
	public void Draw(SpriteBatch batch) {
		bg.Draw(batch);
	}

}
