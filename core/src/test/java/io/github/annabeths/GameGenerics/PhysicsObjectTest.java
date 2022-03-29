package io.github.annabeths.GameGenerics;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.NeutralBoat;
import io.github.annabeths.Colleges.College;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Level.GameMap;

public class PhysicsObjectTest {

	public static GameController gc;

	@BeforeAll
	public static void init() {
		gc = mock(GameController.class);
		gc.map = mock(GameMap.class);
		gc.colleges = new ArrayList<College>();
	}

	@Test
	public void testCollision() {
		NeutralBoat nb = new NeutralBoat(gc, new Vector2(100, 100));
		NeutralBoat nb2 = new NeutralBoat(gc, new Vector2(0, 0));
		NeutralBoat nb3 = new NeutralBoat(gc, new Vector2(110, 110));

		assertTrue(nb.CheckCollisionWith(nb3));
		assertFalse(nb.CheckCollisionWith(nb2));
	}

}
