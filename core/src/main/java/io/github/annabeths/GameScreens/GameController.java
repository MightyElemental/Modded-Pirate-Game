package io.github.annabeths.GameScreens;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.AIBoat;
import io.github.annabeths.Boats.NeutralBoat;
import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.Collectables.Powerup;
import io.github.annabeths.Collectables.PowerupType;
import io.github.annabeths.Colleges.College;
import io.github.annabeths.Colleges.EnemyCollege;
import io.github.annabeths.Colleges.PlayerCollege;
import io.github.annabeths.GameGenerics.GameObject;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GeneralControl.eng1game;
import io.github.annabeths.Level.GameMap;
import io.github.annabeths.Projectiles.ProjectileDataHolder;
import io.github.annabeths.UI.HUD;

public class GameController implements Screen {

	eng1game game;
	ArrayList<GameObject> gameObjects;
	ArrayList<PhysicsObject> physicsObjects;
	public ArrayList<College> colleges;
	public GameMap map;
	private Vector2 mapSize;
	public PlayerBoat playerBoat;
	private EnemyCollege bossCollege;
	public Powerup powerUp;

	public float timer = 600;

	// UI Related Variables
	private SpriteBatch batch;
	private ShapeRenderer sr;
	BitmapFont font;
	public HUD hud;

	// Player Stats
	public int xp = 0;
	public int plunder = 0;

	float xpTick = 1f;
	float xpTickMultiplier = 1f;

	// projectile variables
	public ProjectileDataHolder projectileHolder;

	// passes the game class so that we can change scene back later
	public GameController(eng1game game) {
		this.game = game;
		gameObjects = new ArrayList<GameObject>();
		physicsObjects = new ArrayList<PhysicsObject>();
		colleges = new ArrayList<College>();
		projectileHolder = new ProjectileDataHolder();
		hud = new HUD(this);
		mapSize = new Vector2(1500, 1500);
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
	}

	@Override
	public void show() {
		// Create the player boat and place it in the center of the screen
		playerBoat = new PlayerBoat(this, new Vector2(200, 200), mapSize.cpy());
		physicsObjects.add(playerBoat);

		// this section creates a array of textures for the colleges, shuffles it and
		// assigns to the created colleges
		Texture[] collegeTextures = new Texture[10];
		for (int i = 0; i < 9; i++) {
			collegeTextures[i] = new Texture("img/castle" + (i + 1) + ".png");
		} // load the textures

		for (int i = 0; i < 9; i++) {
			Texture tmp = collegeTextures[i];
			int randomInt = MathUtils.random.nextInt(9);
			collegeTextures[i] = collegeTextures[randomInt];
			collegeTextures[randomInt] = tmp;
		} // shuffle the array of textures

		// get the texture for colleges to sit on
		Texture islandTexture = new Texture("img/island.png");
		PlayerCollege p = new PlayerCollege(new Vector2(50, 50), collegeTextures[0], islandTexture);
		physicsObjects.add(p); // add college to physics object, for updates
		colleges.add(p); // also add a reference to the colleges list

		EnemyCollege e = new EnemyCollege(new Vector2(50, 1350), collegeTextures[1], islandTexture,
				this, projectileHolder.stock, 200);
		physicsObjects.add(e);
		colleges.add(e);

		e = (new EnemyCollege(new Vector2(1350, 50), collegeTextures[2], islandTexture, this,
				projectileHolder.stock, 200));

		physicsObjects.add(e);
		colleges.add(e);

		e = (new EnemyCollege(new Vector2(1350, 1350), collegeTextures[3], islandTexture, this,
				projectileHolder.stock, 200));

		physicsObjects.add(e);
		colleges.add(e);

		bossCollege = new EnemyCollege(new Vector2(700, 700), collegeTextures[4], islandTexture,
				this, projectileHolder.boss, 200);

		bossCollege.invulnerable = true;
		physicsObjects.add(bossCollege);
		colleges.add(bossCollege);
		// create the moving camera/map borders

		// create some neutral boats (could be extended to a proper spawner at some
		// point)
		physicsObjects.add(new NeutralBoat(this, new Vector2(400, 400), mapSize));
		physicsObjects.add(new NeutralBoat(this, new Vector2(800, 400), mapSize));
		physicsObjects.add(new NeutralBoat(this, new Vector2(400, 800), mapSize));
		physicsObjects.add(new NeutralBoat(this, new Vector2(800, 800), mapSize));

		physicsObjects.add(new Powerup(PowerupType.RAPIDFIRE, new Vector2(300, 600)));
		
		map = new GameMap(Gdx.graphics.getHeight(), Gdx.graphics.getWidth(),
				(PlayerBoat) playerBoat, batch, (int) mapSize.x, (int) mapSize.y);
	}

	@Override
	public void render(float delta) {
		// do updates here
		timer -= delta;
		if (timer <= 0) gameOver();

		// give the player XP and Plunder each frame, normalized using delta
		xpTick -= delta * xpTickMultiplier;
		if (xpTick <= 0) {
			xp += 1;
			plunder += 1;
			xpTick += 1;
		}

		hud.Update(delta);
		map.Update(delta);

		UpdateObjects(delta); // update all physicsobjects
		ClearKilledObjects(); // clear any 'killed' objects

		if (bossCollege.HP <= 0) { // if the boss college is dead, the game is won
			game.gotoScreen(Screens.gameWinScreen);
		}

		// do draws here
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// set the sprite batch to use the correct camera
		batch.setProjectionMatrix(map.camera.combined);

		// begin the sprite batch
		batch.begin();

		map.Draw(batch);

		// draw all the physics objects
		for (PhysicsObject physicsObject : physicsObjects) {
			physicsObject.Draw(batch);
		}

		hud.Draw(batch);
		// end the sprite batch
		batch.end();

		// begin debug sprite batch
		boolean debugCollisions = false;

		// this should be off during normal gameplay, but can be on to debug collisions
		if (debugCollisions) {
			sr.setProjectionMatrix(map.camera.combined);
			sr.begin(ShapeType.Line);
			for (int i = 0; i < physicsObjects.size(); i++) {
				PhysicsObject o = physicsObjects.get(i);
				sr.setColor(Color.WHITE);
				sr.polygon(o.collisionPolygon.getTransformedVertices());
				// Draw boat destinations
				if (o instanceof AIBoat) {
					AIBoat aib = (AIBoat) o;
					float dt = aib.getDestinationThreshold();
					sr.setColor(Color.GRAY);
					sr.circle(aib.GetDestination().x, aib.GetDestination().y, dt);

					sr.line(aib.getCenter(), aib.GetDestination());
				}
			}
			sr.end();
		}
	}

	/**
	 * Updates all physics objects in the {@link #physicsObjects} list
	 * 
	 * @param delta time since last frame
	 */
	public void UpdateObjects(float delta) {
		for (int i = 0; i < physicsObjects.size(); i++) {
			PhysicsObject current = physicsObjects.get(i);
			/*
			 * Colleges need a slightly different update method signature, so use that
			 * specifically for them
			 */
			if (current instanceof College) {
				current.Update(delta, playerBoat);
			} else { // other physics objects update
				current.Update(delta);
			}
			for (int j = 0; j < physicsObjects.size(); j++) {
				PhysicsObject other = physicsObjects.get(j);
				if (i == j) continue;

				if (current.CheckCollisionWith(other)) {
					current.OnCollision(other);
				}
			}
		}

		// XP is increased if player is in dangerous position
		xpTickMultiplier = isPlayerInDanger() ? 2f : 1f;
	}

	/**
	 * Tests if player is in danger. The player is in danger if it is in range of an
	 * {@link EnemyCollege}.
	 * 
	 * @return {@code true} if within range of an enemy college, {@code false}
	 *         otherwise.
	 * @author James Burnell
	 */
	public boolean isPlayerInDanger() {
		return colleges.stream()
				.anyMatch(c -> c instanceof EnemyCollege && c.isInRange(playerBoat));
	}

	/**
	 * Called when a college is destroyed Makes sure the boss college will be made
	 * vulnerable after the rest of the colleges are destroyed
	 */
	public void CollegeDestroyed() {
		AddXP(100);

		boolean foundCollege = physicsObjects.stream().filter(c -> c instanceof EnemyCollege)
				.anyMatch(c -> {
					EnemyCollege e = (EnemyCollege) c;
					// there is still a normal college alive
					return e.HP > 0 && !e.invulnerable;
				});

		if (!foundCollege) {
			bossCollege.becomeVulnerable();
		}
	}

	/**
	 * Goes through all the {@link PhysicsObject} and removes ones from the list
	 * that have had the flag set ({@link GameObject#killOnNextTick}) in a safe
	 * manner
	 */
	public void ClearKilledObjects() {
		Iterator<PhysicsObject> p = physicsObjects.iterator();
		while (p.hasNext()) {
			PhysicsObject current = p.next();
			if (current.killOnNextTick) {
				p.remove();
			}
		}
	}

	@Override
	public void resize(int width, int height) {
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

	public void gameOver() {
		game.timeUp = timer <= 0;
		game.gotoScreen(Screens.gameOverScreen);
	}

	/**
	 * Called to give a reference to a new {@link PhysicsObject} to the
	 * {@link #physicsObjects} list
	 * 
	 * @param obj the object to add
	 */
	public void NewPhysicsObject(PhysicsObject obj) {
		// A new PhysicsObject has been created, add it to the list so it receives
		// updates
		physicsObjects.add(obj);
	}

	/**
	 * Add XP to the player's amount. Gives the player an equal amount of gold and
	 * XP
	 * 
	 * @param amount the amount of XP to add
	 */
	public void AddXP(int amount) {
		xp += amount;
		plunder += amount;
	}

}
