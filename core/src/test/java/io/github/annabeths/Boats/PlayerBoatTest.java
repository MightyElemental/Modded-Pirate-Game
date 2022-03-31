package io.github.annabeths.Boats;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Colleges.College;
import io.github.annabeths.Colleges.EnemyCollege;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Level.GameMap;

public class PlayerBoatTest {

	public GameController gc;
	public PlayerBoat b;

	@BeforeEach
	public void setup() {
		gc = mock(GameController.class);
		gc.map = mock(GameMap.class);
		gc.colleges = new ArrayList<College>();
		initColleges();
		gc.playerBoat = new PlayerBoat(gc, new Vector2(0, 0));
		when(gc.map.getMapHeight()).thenReturn(1000f);
		when(gc.map.getMapWidth()).thenReturn(1000f);
		doCallRealMethod().when(gc.map).getRandomPointInBounds();

		b = newBoat();
	}

	private void initColleges() {
		Vector2 pos = new Vector2(10, 10);
		EnemyCollege c = mock(EnemyCollege.class);

		doCallRealMethod().when(c).getLocalCenterX();
		doCallRealMethod().when(c).getLocalCenterY();
		doCallRealMethod().when(c).getX();
		doCallRealMethod().when(c).getY();
		doCallRealMethod().when(c).setCenter(any(Vector2.class));

		c.sprite = new Sprite();
		Polygon collisionPolygon = new Polygon(new float[] { 0, 0, 100, 0, 100, 100, 0, 100 });
		collisionPolygon.setPosition(pos.x, pos.y);
		c.setCenter(pos);
		c.collisionPolygon = collisionPolygon;
		gc.colleges.add(c);
	}

	private PlayerBoat newBoat() {
		PlayerBoat result = new PlayerBoat(gc, new Vector2(0, 0));
		return result;
	}

}
