package io.github.annabeths.GeneralControl;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import io.github.annabeths.Boats.AIBoat;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;

/** @author James Burnell */
public class DebugUtils {

	private static BitmapFont debugFont;

	static {
		debugFont = new BitmapFont(Gdx.files.internal("fonts/cozette.fnt"), false);
		debugFont.getData().setScale(0.5f);
	}

	public static boolean DRAW_DEBUG_COLLISIONS = true;
	public static boolean DRAW_DEBUG_TEXT = true;

	public static void drawDebugText(GameController gc, SpriteBatch batch) {
		List<String> debugText = generateDebugText(gc);
		for (int i = 0; i < debugText.size(); i++) {
			debugFont.draw(batch, debugText.get(i), 10, 30 + 15 * i);
		}
	}

	private static List<String> generateDebugText(GameController gc) {
		return Arrays.asList("PhysObj Count = " + gc.physicsObjects.size());
		// , "Living College Count = " + gc.colleges.stream().filter(c->c.)
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

}
