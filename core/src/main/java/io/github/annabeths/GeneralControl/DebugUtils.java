package io.github.annabeths.GeneralControl;

import static io.github.annabeths.GeneralControl.ResourceManager.debugFont;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import io.github.annabeths.Boats.AIBoat;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;

/**
 * @since Assessment 2
 * @author James Burnell
 */
public class DebugUtils {

	public static boolean SKIP_SPLASH = false;
	public static boolean DRAW_DEBUG_COLLISIONS = false;
	public static boolean DRAW_DEBUG_TEXT = false;
	public static boolean ENEMY_COLLEGE_FIRE = true;
	public static boolean FORCE_POWERUP = false;

	/**
	 * Load the default debug settings from the xml file. This is needed in the
	 * event a debug field is modified and built into the final game.
	 */
	public static void initDebugSettings() {
		String path = String.format("%s/debugsettings.xml",
				DebugUtils.class.getPackageName().replaceAll("\\.", "/"));
		try {
			FileHandle file = Gdx.files.classpath(path);
			if (!file.exists()) {
				System.err.println("Debug settings file is missing");
				return;
			}
			XmlReader xReader = new XmlReader();
			Element e = xReader.parse(file);

			SKIP_SPLASH = getSavedValue(e, "SKIP_SPLASH");
			DRAW_DEBUG_COLLISIONS = getSavedValue(e, "DRAW_DEBUG_COLLISIONS");
			DRAW_DEBUG_TEXT = getSavedValue(e, "DRAW_DEBUG_TEXT");
			ENEMY_COLLEGE_FIRE = getSavedValue(e, "ENEMY_COLLEGE_FIRE");
			FORCE_POWERUP = getSavedValue(e, "FORCE_POWERUP");
		} catch (NullPointerException e) {
			System.err.println("Failed to load debug values");
		}
	}

	private static boolean getSavedValue(Element e, String var) {
		return Boolean.parseBoolean(e.getChildByName(var).get("enabled"));
	}

	public static void drawDebugText(GameController gc, SpriteBatch batch) {
		List<String> debugText = generateDebugText(gc);
		for (int i = 0; i < debugText.size(); i++) {
			debugFont.draw(batch, debugText.get(i), 10, Gdx.graphics.getHeight() - 15 - 15 * i);
		}
	}

	public static void drawEntityDebugText(GameController gc, SpriteBatch batch) {
		for (int i = 0; i < gc.physicsObjects.size(); i++) {
			PhysicsObject o = gc.physicsObjects.get(i);
			if (o instanceof AIBoat) {
				AIBoat aib = (AIBoat) o;

				debugFont.draw(batch,
						String.format("d%.1f'\nr%.1f'", aib.getAngleToDest(), aib.rotation),
						aib.getCenterX() + 50, aib.getCenterY() + 50);
			}
		}
	}

	private static List<String> generateDebugText(GameController gc) {
		return Arrays.asList("PhysObj Count = " + gc.physicsObjects.size(),
				"Living College Count = "
						+ gc.colleges.stream().filter(c -> c.getHealth() > 0).count(),
				"FPS: " + Gdx.graphics.getFramesPerSecond(),
				"Player in danger? " + gc.isPlayerInDanger());
	}

	public static void drawDebugCollisions(GameController gc, ShapeRenderer sr) {
		sr.begin(ShapeType.Line);

		for (int i = 0; i < gc.physicsObjects.size(); i++) {
			PhysicsObject o = gc.physicsObjects.get(i);
			sr.setColor(Color.WHITE);
			// Draw collision polygons
			sr.polygon(o.collisionPolygon.getTransformedVertices());
			// Draw boat destinations
			if (o instanceof AIBoat) {
				AIBoat aib = (AIBoat) o;
				// skip null destinations
				if (aib.GetDestination() == null) continue;
				float dt = aib.getDestinationThreshold();
				sr.setColor(Color.GRAY);
				// Draw destination
				sr.circle(aib.GetDestination().x, aib.GetDestination().y, dt);
				// Draw line from ship to destination
				sr.line(aib.getCenter(), aib.GetDestination());
			}
		}

		sr.end();
	}

	/**
	 * Time how long a piece of code takes to run.
	 * 
	 * @param r the code to time
	 * @return the duration in nanoseconds
	 */
	public static long timeCodeNano(Runnable r) {
		long t = System.nanoTime();
		r.run();
		return System.nanoTime() - t;
	}

	/**
	 * Time how long a piece of code takes to run.
	 * 
	 * @param r the code to time
	 * @return the duration in milliseconds
	 */
	public static long timeCodeMs(Runnable r) {
		return timeCodeNano(r) / 1000000;
	}

}
