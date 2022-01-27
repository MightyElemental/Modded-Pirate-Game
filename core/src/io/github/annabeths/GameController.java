package io.github.annabeths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

import io.github.annabeths.Projectiles.ProjectileDataHolder;

public class GameController implements Screen {

    eng1game game;
    ArrayList<GameObject> gameObjects;
    ArrayList<PhysicsObject> physicsObjects;
    public ArrayList<College> colleges;
    float testRot = 0;

    // UI Related Variables
    private SpriteBatch batch;
    GameMap map;
    
    BitmapFont font;
    HUD hud;


    // Player Stats
    int xp = 0;
    int plunder = 0;

    float xpTick = 1f;
    float xpTickMultiplier = 1f;

    // Upgrade Variables

    ProjectileDataHolder projectileHolder;

    public ProjectileDataHolder projectileDataHolder;

    public PlayerBoat playerBoat;

    private EnemyCollege bossCollege;

    public GameController(eng1game game){ //passes the game class so that we can change scene back later
        this.game = game;
        gameObjects = new ArrayList<GameObject>();
        physicsObjects = new ArrayList<PhysicsObject>();
        colleges = new ArrayList<College>();
        projectileHolder = new ProjectileDataHolder();
        hud = new HUD(this);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        
        // Create UI


        // Create the player boat and place it in the centre of the screen
        playerBoat = new PlayerBoat(this, new Vector2(200,200), new Vector2(1500,1500));
        physicsObjects.add(playerBoat);

        Texture[] collegeTextures = new Texture[10];
        Random rd = new Random();
        for(int i=0; i < 9; i++)
        {
            collegeTextures[i] = new Texture("img/castle" + (i+1) + ".png");
        }
        for(int i=0; i < 9; i++)
        {
            Texture tmp = collegeTextures[i];
            int randomInt = rd.nextInt(9);
            collegeTextures[i] = collegeTextures[randomInt];
            collegeTextures[randomInt] = tmp;
        }

        Texture islandTexture = new Texture("img/island.png");
        PlayerCollege p = new PlayerCollege(new Vector2(50,50), collegeTextures[0], islandTexture);
        physicsObjects.add(p);
        colleges.add(p);

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

        //create a test AI boat
        physicsObjects.add(new NeutralBoat(this, new Vector2(400, 400), new Vector2(2000, 2000)));
        physicsObjects.add(new NeutralBoat(this, new Vector2(800, 400), new Vector2(2000, 2000)));
        physicsObjects.add(new NeutralBoat(this, new Vector2(400, 800), new Vector2(2000, 2000)));
        physicsObjects.add(new NeutralBoat(this, new Vector2(800, 800), new Vector2(2000, 2000)));

        map = new GameMap(Gdx.graphics.getHeight(), Gdx.graphics.getWidth(),
        (PlayerBoat) playerBoat, batch, 1500, 1500);
    }

    @Override
    public void render(float delta) {
        // do updates here

        xpTick -= delta * xpTickMultiplier;
        if(xpTick <= 0){
            xp += 1;
            plunder += 1;
            xpTick = 1;
        }
    	
        hud.Update(delta);
    	map.Update(delta);

        UpdateObjects(delta);
        ClearKilledObjects();

        if(bossCollege.HP <= 0)
        {
            game.gotoScreen(Screens.gameWinScreen);
        }

        // do draws here
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(map.camera.combined);

        batch.begin(); //begin the sprite batch
        
        map.Draw(batch);

        if (physicsObjects.size() > 0)
        {
            for (PhysicsObject physicsObject : physicsObjects) {
                physicsObject.Draw(batch);
            }
        }


        hud.Draw(batch);
        batch.end(); //end the sprite batch

        //begin debug sprite batch
        boolean debugCollisions = false;
        if(debugCollisions)
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
    
    public void NewPhysicsObject(PhysicsObject obj) {
    	// A new PhysicsObject has been created, add it to the list so it receives updates
    	physicsObjects.add(obj);
    }

    public void RemovePhysicsObject(PhysicsObject obj){
        physicsObjects.remove(obj);
    }
    public void AddXP(int amount){
        // Give the player an equal amount of gold and XP
        xp += amount;
        plunder += amount;
    }

}
