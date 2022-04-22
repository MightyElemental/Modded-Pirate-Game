package io.github.annabeths.GeneralControl;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Obstacles.Kraken;
import io.github.annabeths.Obstacles.Mine;

public class DifficultyTest {

	public static GameController easyG;
	public GameController mediumG;
	public GameController hardG;
	public static eng1game game;

	@BeforeAll
	public static void init() {
		TestHelper.setupEnv();
		game = new eng1game();
	}

	@BeforeEach
	public void setup() {
		easyG = new GameController(game, Difficulty.EASY);
		mediumG = new GameController(game, Difficulty.MEDIUM);
		hardG = new GameController(game, Difficulty.HARD);
	}

	public long countMines(GameController g) {
		return g.physicsObjects.stream().filter(o -> o instanceof Mine).count();
	}

	public boolean isKrakenPresent(GameController g) {
		for (PhysicsObject obj : g.physicsObjects) {
			if (obj instanceof Kraken) {
				return true;
			}
		}
		return false;
	}

	@Test
	public void testNumMinesCorrect() {
		assertEquals(Difficulty.EASY.getNumMines(), countMines(easyG));
		assertEquals(Difficulty.MEDIUM.getNumMines(), countMines(mediumG));
		assertEquals(Difficulty.HARD.getNumMines(), countMines(hardG));
	}

	@Test
	public void testIfKrakenIsPresent() {
		assertEquals(isKrakenPresent(easyG), false);
		assertEquals(isKrakenPresent(mediumG), true);
		assertEquals(isKrakenPresent(hardG), true);
	}

}
