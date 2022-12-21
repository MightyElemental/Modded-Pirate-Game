package io.github.annabeths.GameScreens;

import static com.badlogic.gdx.Gdx.input;
import static io.github.annabeths.Level.GameMap.BORDER_BRIM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.EnemyBoat;
import io.github.annabeths.Boats.NeutralBoat;
import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.Collectables.Powerup;
import io.github.annabeths.Collectables.PowerupType;
import io.github.annabeths.Colleges.College;
import io.github.annabeths.Colleges.EnemyCollege;
import io.github.annabeths.Colleges.PlayerCollege;
import io.github.annabeths.GameGenerics.GameObject;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GeneralControl.DebugUtils;
import io.github.annabeths.GeneralControl.Difficulty;
import io.github.annabeths.GeneralControl.SaveManager;
import io.github.annabeths.GeneralControl.eng1game;
import io.github.annabeths.Level.GameMap;
import io.github.annabeths.Obstacles.Kraken;
import io.github.annabeths.Obstacles.Mine;
import io.github.annabeths.Obstacles.Weather;
import io.github.annabeths.Projectiles.ProjectileData;
import io.github.annabeths.Projectiles.ProjectileRay;
import io.github.annabeths.UI.HUD;

/**
 * The main game screen. This is the game itself.
 * @author James Burnell
 * @author Hector Woods
 * @tt.updated Assessment 2
 */
public class GameController implements Screen {

	private eng1game game;
	public ArrayList<GameObject> gameObjects;
	public ArrayList<PhysicsObject> physicsObjects;
	public ArrayList<College> colleges;
	public ArrayList<ProjectileRay> rays;
	public GameMap map;
	public PlayerBoat playerBoat;
	public EnemyCollege bossCollege;
	public Powerup powerUp;

	private Difficulty gameDifficulty = Difficulty.MEDIUM;

	public static final float PLAY_TIME = 10 * 60;
	public float timer = PLAY_TIME;

	// UI Related Variables
	public OrthographicCamera camera;
	public SpriteBatch batch;
	public ShapeRenderer sr;
	public HUD hud;

	// Player Stats
	private float xp = 0;
	private float totalXp = 0;
	private int plunder = 0;
	private int totalPlunder = 0;

	float xpTick = 1f;
	float xpTickMultiplier = 1f;

	/**
	 * Constructor for GameController. Called when 'New Game' is selected
	 * @param game reference to eng1game
	 * @param diff the game's difficulty
	 */
	public GameController(eng1game game, Difficulty diff) {
		this();
		this.game = game;

		this.setDifficulty(diff);

		generateGameObjects();
	}
	/**
	 * Constructor for GameController. Called when 'Load game' is selected
	 * @param game reference to eng1game
	 * @param saveFileName save file to load from
	 */
	public GameController(eng1game game, String saveFileName){
		this();
		this.game = game;
		SaveManager.load(saveFileName, this);
	}

	/**
	 * Creates a GameController with a default difficulty of
	 * {@link Difficulty#MEDIUM}
	 * @param game the game object
	 */
	public GameController(eng1game game) {
		this(game, Difficulty.MEDIUM);
	}

	/**
	 * Constructor for GameController
	 */
	private GameController() {
		gameObjects = new ArrayList<>();
		physicsObjects = new ArrayList<>();
		colleges = new ArrayList<>();
		rays = new ArrayList<>();

		camera = new OrthographicCamera();
		camera.viewportHeight = Gdx.graphics.getHeight();
		camera.viewportWidth = Gdx.graphics.getWidth();

		// Create the player boat and place it in the center of the screen
		playerBoat = new PlayerBoat(this, new Vector2(500, 500));
		physicsObjects.add(playerBoat);
	}


	/**
	 * Called when GameController is created.
	 */
	@Override
	public void show() {
		hud = new HUD(this);
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		map = new GameMap(this);
	}

	final int weatherPerGeneration = 5;
	float timeBetweenWeatherGeneration = 5;
	float timeSinceLastWeather = 0;

	/**
	 * Generate weather in the world. Called every timeBetweenWeatherGeneration seconds.
	 */
	public void generateWeather() {

		float width = GameMap.getMapWidth();
		float height = GameMap.getMapHeight();

		double a = MathUtils.random();
		Vector2 position;
		int direction = 0;
		if (a > 0.75) {
			direction = 3;
			position = new Vector2(width, MathUtils.random(height));
		} else if (a > 0.5) {
			direction = 2;
			position = new Vector2(0, MathUtils.random(height));
		} else if (a > 0.25) {
			direction = 1;
			position = new Vector2(MathUtils.random(width), 0);
		} else {
			position = new Vector2(MathUtils.random(width), height);
		}

		for (int i = 0; i < weatherPerGeneration; i++) {
			physicsObjects.add(new Weather(this, new Vector2(position.x + MathUtils.random(500),
					position.y + MathUtils.random(500)), direction));
		}
	}


	/**
	 * Generate game objects. Called when 'New game' is selected.
	 */
	private void generateGameObjects() {
		// Generate a list of random college textures
		// TODO: Make textures unique
		List<String> collegeTextures = MathUtils.random.ints(5, 0, 9)
				.mapToObj(tn -> String.format("img/world/castle/castle%d.png", (tn + 1)))
				.collect(Collectors.toList());

		Vector2 collegePlayer = new Vector2(BORDER_BRIM, BORDER_BRIM);
		Vector2 college1 = new Vector2(BORDER_BRIM, GameMap.getMapHeight() - 100 - BORDER_BRIM);
		Vector2 college2 = new Vector2(GameMap.getMapWidth() - 100 - BORDER_BRIM, BORDER_BRIM);
		Vector2 college3 = new Vector2(GameMap.getMapWidth() - 100 - BORDER_BRIM,
				GameMap.getMapHeight() - 100 - BORDER_BRIM);
		List<Vector2> collegePos = Arrays.asList(college1, college2, college3);
		Vector2 collegeBoss = new Vector2((GameMap.getMapWidth() - 100) / 2,
				(GameMap.getMapHeight() - 100) / 2);

		// get the texture for colleges to sit on
		String islandTexture = "img/world/island.png";
		PlayerCollege p = new PlayerCollege(collegePlayer, collegeTextures.get(0), islandTexture,
				this, false);
		physicsObjects.add(p); // add college to physics object, for updates
		colleges.add(p); // also add a reference to the colleges list

		// create the boss college
		bossCollege = new EnemyCollege(collegeBoss, collegeTextures.get(1), islandTexture, this,
				ProjectileData.BOSS, 3200);

		bossCollege.setInvulnerable(true);
		physicsObjects.add(bossCollege);
		colleges.add(bossCollege);

		// create some enemy colleges
		for (int i = 0; i < 3; i++) {
			EnemyCollege e = new EnemyCollege(collegePos.get(i), collegeTextures.get(i + 2),
					islandTexture, this, ProjectileData.STOCK, 800);
			physicsObjects.add(e);
			colleges.add(e);
		}

		// create some powerups
		for (int i = 0; i < 50; i++) {
			Powerup pup;
			do {
				pup = new Powerup(PowerupType.randomPower(), GameMap.getRandomPointInBounds());
			} while (isObjectOverlappingAnyObj(pup)); // world is big enough to prevent blocking

			physicsObjects.add(pup);
		}

		// create some boats
		physicsObjects.add(new NeutralBoat(this,
				new Vector2(GameMap.getMapWidth() / 3, GameMap.getMapHeight() / 3)));
		physicsObjects.add(new NeutralBoat(this,
				new Vector2(2 * GameMap.getMapWidth() / 3, GameMap.getMapHeight() / 3)));
		physicsObjects.add(new NeutralBoat(this,
				new Vector2(GameMap.getMapWidth() / 3, 2 * GameMap.getMapHeight() / 3)));
		physicsObjects.add(new EnemyBoat(this,
				new Vector2(2 * GameMap.getMapWidth() / 3, 2 * GameMap.getMapHeight() / 3)));

		// add a kraken
		if (getGameDifficulty().doesKrakenSpawn())
			physicsObjects.add(new Kraken(this, new Vector2(1500, 1500)));

		// generate mines
		for (int i = 0; i < getGameDifficulty().getNumMines(); i++) {
			Mine m;
			do { // ensure mine is not overlapping any object
				m = new Mine(this, GameMap.getRandomPointInBounds());
			} while (isObjectOverlappingAnyObj(m)); // world is big enough to prevent blocking

			physicsObjects.add(m);
		}

	}

	/**
	 * Test if an object is overlapping any existing object in the world. Useful to
	 * prevent objects being generated on top of another object
	 * 
	 * @param obj the object to test
	 * @return {@code true} if the object is overlapping with any object in the
	 *         world, {@code false} otherwise
	 */
	public boolean isObjectOverlappingAnyObj(PhysicsObject obj) {
		return physicsObjects.stream().anyMatch(p -> p.CheckCollisionWith(obj));
	}

	/**
	 * Called once per frame. Updates PhysicsObjects and Obstacles, increments xp.
	 * @param delta time since the last frame
	 */
	public void logic(float delta) {
		timer -= delta;
		if (timer <= 0) gameOver();

		// give the player XP and Plunder each frame, normalized using delta
		xpTick -= delta * xpTickMultiplier;
		if (xpTick <= 0) {
			addXp(1);
			// plunder += 1;
			xpTick += 1;
		}

		hud.Update(delta);
		map.Update(delta);

		UpdateObjects(delta); // update all physics objects
		ClearKilledObjects(); // clear any 'killed' objects

		// if the boss college is dead, the game is won
		if (bossCollege.isDead()) {
			game.gameScore = (int) getGameScore();
			game.gotoScreen(Screens.gameWinScreen);
		}
		timeSinceLastWeather += delta;
		if (timeSinceLastWeather >= timeBetweenWeatherGeneration) {
			generateWeather();
			timeSinceLastWeather = 0;
		}
	}

	/**
	 * Draw sprites of all PhysicsObjects. Called once per frame
	 * @param delta time since the last frame
	 */
	@Override
	public void render(float delta) {
		// do update here
		logic(delta);

		// do draw here
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// set the sprite batch to use the correct camera
		batch.setProjectionMatrix(camera.combined);

		// begin the sprite batch
		batch.begin();

		map.Draw(batch);

		// draw all the physics objects
		for (PhysicsObject physicsObject : physicsObjects) {
			physicsObject.Draw(batch);
		}

		if (DebugUtils.DRAW_DEBUG_TEXT) DebugUtils.drawEntityDebugText(this, batch);

		// end the sprite batch
		batch.end();

		hud.Draw(batch);

		sr.setProjectionMatrix(camera.combined);
		renderRays();
		// this should be off during normal gameplay, but can be on to debug
		// collisions
		if (DebugUtils.DRAW_DEBUG_COLLISIONS) DebugUtils.drawDebugCollisions(this, sr);
	}

	/** Renders ProjectileRay objects */
	public void renderRays() {
		sr.begin(ShapeType.Filled);

		Random rCol = new Random();

		rays.forEach(r -> {
			float ratio = r.getRemainShowTime() / r.getShowTime();
			sr.setColor(Color.BLACK);
			sr.rectLine(r.getOrigin(), r.getFarthestHitPoint(), 6 * ratio);

			rCol.setSeed(r.hashCode()); // unique color for each ray
			sr.setColor(rCol.nextFloat(), rCol.nextFloat(), rCol.nextFloat(), 1);
			sr.rectLine(r.getOrigin(), r.getFarthestHitPoint(), 4 * ratio);
		});

		sr.end();
	}

	/**
	 * Updates all physics objects in the {@link #physicsObjects} list
	 * @param delta time since last frame
	 */
	public void UpdateObjects(float delta) {
		for (int i = 0; i < physicsObjects.size(); i++) {
			PhysicsObject current = physicsObjects.get(i);

			current.Update(delta);

			for (int j = 0; j < physicsObjects.size(); j++) {
				PhysicsObject other = physicsObjects.get(j);
				if (i == j) continue;

				if (current.CheckCollisionWith(other)) {
					current.OnCollision(other);
				}
			}
		}

		// Update rays
		rays.forEach(r -> r.Update(delta));

		// XP is increased if player is in dangerous position
		xpTickMultiplier = isPlayerInDanger() ? 2f : 1f;
	}

	/**
	 * Tests if player is in danger. The player is in danger if it is in range of an
	 * {@link EnemyCollege}, {@link EnemyBoat}, or {@link Kraken}.
	 * @return {@code true} if within range of a danger, {@code false} otherwise or
	 *         player is invincible.
	 * @author James Burnell
	 */
	public boolean isPlayerInDanger() {
		return !playerBoat.isInvincible()
				&& (isPlayerNearEnemyCollege() || isPlayerNearEnemyBoat() || isPlayerNearKraken());
	}

	/**
	 * Tests if player is in range of an {@link EnemyBoat}.
	 * @return {@code true} if within range of an enemy boat, {@code false}
	 *         otherwise.
	 * @author James Burnell
	 */
	public boolean isPlayerNearEnemyBoat() {
		return physicsObjects.stream().filter(p -> p instanceof EnemyBoat)
				.anyMatch(p -> p.getCenter().dst2(playerBoat.getCenter()) < 500 * 500);
	}

	/**
	 * Tests if player is in range of an {@link EnemyCollege}.
	 * 
	 * @return {@code true} if within range of an enemy college, {@code false}
	 *         otherwise.
	 * @author James Burnell
	 */
	public boolean isPlayerNearEnemyCollege() {
		return colleges.stream().filter(c -> c instanceof EnemyCollege)
				.anyMatch(c -> c.isInRange(playerBoat));
	}

	/**
	 * Tests if player is in range of a {@link Kraken}.
	 * 
	 * @return {@code true} if within range of a kraken, {@code false} otherwise.
	 * @author James Burnell
	 */
	public boolean isPlayerNearKraken() {
		return physicsObjects.stream().filter(p -> p instanceof Kraken)
				.anyMatch(p -> ((Kraken) p).isInRange(playerBoat));
	}

	/**
	 * Called when a college is destroyed Makes sure the boss college will be made
	 * vulnerable after the rest of the colleges are destroyed, and spawns a
	 * friendly college in the place of the enemy college.
	 * 
	 * @param oldCollege the college that was destroyed
	 */
	public void CollegeDestroyed(EnemyCollege oldCollege) {
		addXp(250);
		addPlunder(250);

		boolean foundCollege = colleges.stream().filter(c -> c instanceof EnemyCollege)
				.anyMatch(c -> {
					EnemyCollege e = (EnemyCollege) c;
					// there is still a normal college alive
					return e.getHealth() > 0 && !e.isInvulnerable();
				});

		if (!foundCollege) {
			bossCollege.becomeVulnerable();
		}

		PlayerCollege newFriendlyCollege = new PlayerCollege(oldCollege);
		physicsObjects.remove(oldCollege);
		physicsObjects.add(newFriendlyCollege);
		colleges.remove(oldCollege);
		colleges.add(newFriendlyCollege);
	}

	/**
	 * Goes through {@link #physicsObjects} and {@link #rays} to remove ones from
	 * the lists that have had the flag set ({@link GameObject#removeOnNextTick()})
	 */
	public void ClearKilledObjects() {
		// Clean up objects
		physicsObjects.removeIf(GameObject::removeOnNextTick);
		// Clean up rays
		rays.removeIf(ProjectileRay::removeOnNextTick);
	}

	/**
	 * resize the window
	 * @param width new width
	 * @param height new height
	 */
	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, 1280, 720);
		hud.resize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}

	/**
	 * Test if the player is within any friendly college
	 * @return {@code true} if player is in range, {@code false} otherwise
	 */
	public boolean isPlayerInRangeOfFriendlyCollege() {
		return colleges.stream().filter(c -> c instanceof PlayerCollege)
				.anyMatch(c -> c.isInRange(playerBoat));
	}

	/**
	 * dispose of the game screen and go to the game over screen.
	 */
	public void gameOver() {
		game.timeUp = timer <= 0;
		game.gameScore = (int) getGameScore();
		game.gotoScreen(Screens.gameOverScreen);
	}

	/**
	 * Calculates the overall game score to be presented to the player at the end of
	 * the game.
	 * 
	 * @return the game score
	 */
	public float getGameScore() {
		float powerupScore = playerBoat.collectedPowerups.values().stream().reduce(0, Integer::sum);
		float healthScore = playerBoat.getHealth() - 100; // penalty for losing
															// health
		float timeScore = (timer - PLAY_TIME); // the shorter the play, the more
												// points
		return getTotalPlunder() * 5 + getTotalXp() + timeScore + healthScore + powerupScore * 25;
	}

	/**
	 * Called to give a reference to a new {@link PhysicsObject} to the
	 * {@link #physicsObjects} list
	 * 
	 * @param obj the object to add
	 */
	public void NewPhysicsObject(PhysicsObject obj) {
		// A new PhysicsObject has been created, add it to the list, so it
		// receives
		// updates
		physicsObjects.add(obj);
	}

	/**
	 * Add XP to the player's amount.
	 * 
	 * @param amount the amount of XP to add
	 */
	public void addXp(float amount) {
		xp += amount * getGameDifficulty().getPlayerXpMul();
		totalXp += amount * getGameDifficulty().getPlayerXpMul();
	}

	/**
	 * Add plunder to the player's amount.
	 * 
	 * @param amount the amount of plunder to add
	 */
	public void addPlunder(float amount) {
		plunder += amount * getGameDifficulty().getPlayerXpMul();
		totalPlunder += amount * getGameDifficulty().getPlayerXpMul();
	}

	/**
	 * Remove plunder from the player's amount.
	 * 
	 * @param amount the amount of plunder to remove
	 */
	public void subtractPlunder(float amount) {
		plunder -= amount;
	}

	/**
	 * Get the current XP level the player is at
	 * 
	 * @return the level
	 */
	public int getXpLevel() {
		return (int) (Math.sqrt(xp + 9) - 3);
	}

	/**
	 * Get how much XP the player has in this current level. i.e. the amount of XP
	 * excluding the XP contributing to whole levels
	 * 
	 * @return the xp in the current level
	 * @see #getXpLevel()
	 */
	public float getXpInLevel() {
		int level = getXpLevel();
		return xp - (level * (level + 6));
	}

	/**
	 * Subtract a number of levels from the xp
	 * 
	 * @param levels the number of levels to remove
	 */
	public void subtractXpLevels(int levels) {
		int curr = getXpLevel();
		int diff = curr - levels;
		// find the difference between xp levels and subtract that amount of xp
		xp -= (curr * (curr + 6)) - (diff * (diff + 6));
	}

	/**
	 * The XP required to go from {@code level-1} to {@code level}.
	 * 
	 * @param level the target level
	 * @return the XP difference between previous level and this one
	 */
	public static int getXpRequiredForLevel(int level) {
		return 2 * (level - 1) + 7;
	}

	/**
	 * The total XP the needs to level up. Note this is NOT the remaining amount,
	 * but the total amount to be able to level up.
	 * 
	 * @return the xp required to level up
	 */
	public float getXpRequiredForNextLevel() {
		return getXpRequiredForLevel(getXpLevel() + 1);
	}

	/**
	 * @return the xp
	 */
	public float getXp() {
		return xp;
	}

	/**
	 * @param xp the xp to set
	 */
	public void setXp(float xp) {
		this.xp = xp;
	}

	/**
	 * @return the plunder
	 */
	public int getPlunder() {
		return plunder;
	}

	/**
	 * @param plunder the plunder to set
	 */
	public void setPlunder(int plunder) {
		this.plunder = plunder;
	}

	/**
	 * @return the totalXp
	 */
	public float getTotalXp() {
		return totalXp;
	}

	/**
	 * @return the totalPlunder
	 */
	public int getTotalPlunder() {
		return totalPlunder;
	}

	/**
	 * Returns a game controller instance with minimal data filled out to allow the
	 * HUD to function without taking up a lot of data.
	 * 
	 * @return The new GameController instance
	 */
	public static GameController getMockForHUD() {
		GameController gc = new GameController();
		gc.playerBoat.damage(13);
		gc.addXp(50);
		return gc;
	}

	/**
	 * setter method for the game's difficulty
	 * @param difficulty the new game difficulty
	 */
	public void setDifficulty(Difficulty difficulty) {
		this.gameDifficulty = difficulty;
	}

	/**
	 * @return the gameDifficulty
	 */
	public Difficulty getGameDifficulty() {
		return gameDifficulty;
	}

}
