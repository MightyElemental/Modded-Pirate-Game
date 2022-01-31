package io.github.annabeths.GameScreens;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.*;
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

    // UI Related Variables
    private SpriteBatch batch;
    BitmapFont font;
    public HUD hud;


    // Player Stats
    public int xp = 0;
    public int plunder = 0;

    float xpTick = 1f;
    float xpTickMultiplier = 1f;


    // projectile variables
    public ProjectileDataHolder projectileHolder;
    public ProjectileDataHolder projectileDataHolder;


    public GameController(eng1game game){ //passes the game class so that we can change scene back later
        this.game = game;
        gameObjects = new ArrayList<GameObject>();
        physicsObjects = new ArrayList<PhysicsObject>();
        colleges = new ArrayList<College>();
        projectileHolder = new ProjectileDataHolder();
        hud = new HUD(this);
        mapSize = new Vector2(1500, 1500);
    }

    @Override
    public void show() { //this function is called when the screen is shown
        batch = new SpriteBatch();

        // Create the player boat and place it in the centre of the screen
        playerBoat = new PlayerBoat(this, new Vector2(200,200), mapSize.cpy());
        physicsObjects.add(playerBoat);

        // this section creates a array of textures for the colleges, shuffles it and assigns to 
        // the created colleges
        Texture[] collegeTextures = new Texture[10];
        Random rd = new Random();
        for(int i=0; i < 9; i++)
        {
            collegeTextures[i] = new Texture("img/castle" + (i+1) + ".png");
        } //load the textures

        for(int i=0; i < 9; i++)
        {
            Texture tmp = collegeTextures[i];
            int randomInt = rd.nextInt(9);
            collegeTextures[i] = collegeTextures[randomInt];
            collegeTextures[randomInt] = tmp;
        } //shuffle the array of textures

        Texture islandTexture = new Texture("img/island.png"); // get the texture for colleges to sit on
        PlayerCollege p = new PlayerCollege(new Vector2(50,50), collegeTextures[0], islandTexture);
        physicsObjects.add(p); //add college to physics object, for updates
        colleges.add(p); //also add a reference to the colleges list

        EnemyCollege e = new EnemyCollege(new Vector2(50,1350), collegeTextures[1], islandTexture,
                           this, projectileHolder.stock, 200);
        physicsObjects.add(e);
        colleges.add(e);
        
        e = (new EnemyCollege(new Vector2(1350,50), collegeTextures[2], islandTexture,
                           this, projectileHolder.stock, 200));

        physicsObjects.add(e);
        colleges.add(e);

        e = (new EnemyCollege(new Vector2(1350,1350), collegeTextures[3], islandTexture,
                           this, projectileHolder.stock, 200));

        physicsObjects.add(e);
        colleges.add(e);

        bossCollege = new EnemyCollege(new Vector2(600,600), collegeTextures[4], islandTexture,
                           this, projectileHolder.boss, 200);

        bossCollege.invulnerable = true;
        physicsObjects.add(bossCollege);
        colleges.add(bossCollege);
        //create the moving camera/map borders

        //create some neutral boats (could be extended to a proper spawner at some point)
        physicsObjects.add(new NeutralBoat(this, new Vector2(400, 400), mapSize));
        physicsObjects.add(new NeutralBoat(this, new Vector2(800, 400), mapSize));
        physicsObjects.add(new NeutralBoat(this, new Vector2(400, 800), mapSize));
        physicsObjects.add(new NeutralBoat(this, new Vector2(800, 800), mapSize));

        map = new GameMap(Gdx.graphics.getHeight(), Gdx.graphics.getWidth(),
        (PlayerBoat) playerBoat, batch, (int) mapSize.x, (int) mapSize.y);
    }

    @Override
    public void render(float delta) {
        // do updates here

        // give the player XP and Plunder each frame, normalised using delta
        xpTick -= delta * xpTickMultiplier;
        if(xpTick <= 0){
            xp += 1;
            plunder += 1;
            xpTick = 1;
        }
    	
        hud.Update(delta);
    	map.Update(delta);

        UpdateObjects(delta); //update all physicsobjects
        ClearKilledObjects(); //clear any 'killed' objects

        if(bossCollege.HP <= 0)
        { //if the boss college is dead, the game is won
            game.gotoScreen(Screens.gameWinScreen);
        }

        // do draws here
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(map.camera.combined); //set the sprite batch to use the correct camera

        batch.begin(); //begin the sprite batch
        
        map.Draw(batch);

        if (physicsObjects.size() > 0) //draw all the physics objects
        {
            for (PhysicsObject physicsObject : physicsObjects) {
                physicsObject.Draw(batch);
            }
        }


        hud.Draw(batch);
        batch.end(); //end the sprite batch

        //begin debug sprite batch
        boolean debugCollisions = false;
        if(debugCollisions) //this should be off during normal gameplay, but can be on to debug collisions
        {
            ShapeRenderer sr = new ShapeRenderer();
            sr.setProjectionMatrix(map.camera.combined);
            sr.begin(ShapeType.Line);
            for(int i=0; i < physicsObjects.size(); i++)
            {
                sr.polygon(physicsObjects.get(i).collisionPolygon.getTransformedVertices());
            }
            sr.end();
        }
    }

    /*
        Returns Nothing

        Updates all physics objects in the PhysicsObjects array

        @param  delta   time since last frame
        @return nothing
    */
    public void UpdateObjects(float delta){
        for(int i=0; i < physicsObjects.size(); i++)
        {
            PhysicsObject current = physicsObjects.get(i);
            if(current instanceof EnemyCollege || current instanceof PlayerCollege)
            { //colleges need a slightly different update method signature, so use that specifically for them
                current.Update(delta, playerBoat);
            }
            else
            { //other physics objects update
                current.Update(delta);
            }
            for(int j=0; j < physicsObjects.size(); j++)
            {
                PhysicsObject other = physicsObjects.get(j);
                if(i==j)
                    continue;

                if(current.CheckCollisionWith(other))
                {
                    current.OnCollision(other);
                }
            }
        }
    }

    /*
        Called when a college is destroyed
        Makes sure the boss college will be made vulnerable after the rest of the 
        colleges are destroyed

        @return nothing
    */
    public void CollegeDestroyed()
    {
        boolean foundCollege = false;
        for(int i=0; i < physicsObjects.size(); i++)
        {
            PhysicsObject current = physicsObjects.get(i);
            if(current.getClass() == EnemyCollege.class)
            {
                EnemyCollege e = (EnemyCollege) current;
                if(e.HP > 0 && !e.invulnerable) // there is still a normal college alive
                {
                    foundCollege = true;
                    break;
                }
            }
        }
        if (!foundCollege)
        {
            bossCollege.becomeVulnerable();
        }
    }

    /*
        Goes through all the physicsobjects and removes ones from the list
        that have had the flag set (killOnNextTick) in a safe manner
    */
    public void ClearKilledObjects(){
        Iterator<PhysicsObject> p = physicsObjects.iterator();
        while(p.hasNext())
        {
            PhysicsObject current = p.next();
            if(current.killOnNextTick)
            {
                p.remove();
            }
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}

    public void gameOver(){
        game.gotoScreen(Screens.gameOverScreen);
    }

    /*
        Called to give a reference to a new physicsobject to the physicsobjects list
        @param  obj     the object to add
    */    
    public void NewPhysicsObject(PhysicsObject obj) {
    	// A new PhysicsObject has been created, add it to the list so it receives updates
    	physicsObjects.add(obj);
    }

    /*
        add xp to the player's amount
    */
    public void AddXP(int amount){
        // Give the player an equal amount of gold and XP
        xp += amount;
        plunder += amount;
    }

}
