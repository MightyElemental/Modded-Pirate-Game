package io.github.annabeths;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

import io.github.annabeths.Projectiles.ProjectileDataHolder;

public class GameController implements Screen {

    eng1game game;
    ArrayList<GameObject> gameObjects;
    ArrayList<PhysicsObject> physicsObjects;
    float testRot = 0;

    // UI Related Variables
    public boolean hoveringOverButton = false; // Disable certain player behaviours when hovering over a button
    boolean upgradeMenuOpen = false;
    boolean upgradeMenuInitialised = false; // Set to true once initialised
    private SpriteBatch batch;
    
    BitmapFont font;
    GlyphLayout hpTextLayout;
    WaterBackground bg;

    Stage stage;

    TextButton menuButton;
    TextButtonStyle menuButtonStyle;

    TextButton upgradeButton1;
    TextButtonStyle upgradeButton1Style;

    TextButton upgradeButton2;
    TextButtonStyle upgradeButton2Style;

    Image upgradeMenuBackground;

    // Player Stats
    int xp = 0;
    int plunder = 0;
    GlyphLayout xpTextLayout;
    GlyphLayout plunderTextLayout;

    float xpTick = 1f;
    float xpTickMultiplier = 1f;

    // Upgrade Variables
    Upgrades upgrade1;
    int upgrade1cost;
    float upgrade1amount;
    Upgrades upgrade2;
    int upgrade2cost;
    float upgrade2amount;

    ProjectileDataHolder projectileHolder;

    EnemyCollege testCollege;

    private PlayerBoat playerBoat;

    public GameController(eng1game game){ //passes the game class so that we can change scene back later
        this.game = game;
        gameObjects = new ArrayList<GameObject>();
        physicsObjects = new ArrayList<PhysicsObject>();
        bg = new WaterBackground(Gdx.graphics.getWidth(),
                                 Gdx.graphics.getHeight());
        projectileHolder = new ProjectileDataHolder();
        testCollege = new EnemyCollege(new Vector2(50,50), new Texture("img/castle1.png"), this, projectileHolder.stock);
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();

        Gdx.input.setInputProcessor(stage);
        
        // Create UI
        font = new BitmapFont(Gdx.files.internal("fonts/bobcat.fnt"), false);
		hpTextLayout = new GlyphLayout();
        xpTextLayout = new GlyphLayout();
		plunderTextLayout = new GlyphLayout();

        DrawUpgradeButton(); // put this in its own function to make this function look a bit cleaner

        // Create the player boat and place it in the centre of the screen
        playerBoat = new PlayerBoat(this);
        playerBoat.SetPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight()/2); // place the player 
    }

    @Override
    public void render(float delta) {
        // do updates here

        stage.act();

        xpTick -= delta * xpTickMultiplier;
        if(xpTick <= 0){
            xp += 1;
            plunder += 1;
            xpTick = 1;
        }
    	
        bg.Update(delta);
    	playerBoat.Update(delta);
        testCollege.Update(delta, playerBoat);

        if (physicsObjects.size() > 0)
        {
            Iterator<PhysicsObject> i = physicsObjects.iterator(); // use an iterator to safely remove objects from 
            // the list whilst traversing it
            while(i.hasNext())
            {
                PhysicsObject p = i.next();
                p.Update(delta); // update the current physicsobject
                if(playerBoat.CheckCollisionWith(p))
                {
                    if(playerBoat.OnCollision(p)) //if it returns true, then remove other
                        i.remove();
                }
            }
        }
        
        // do draws here
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin(); //begin the sprite batch
        
        bg.Draw(batch);
        testCollege.Draw(batch);
        playerBoat.Draw(batch);

        if (physicsObjects.size() > 0)
        {
            for (PhysicsObject physicsObject : physicsObjects) {
                physicsObject.Draw(batch);
            }
        }

        // Draw the text showing the player's stats
        hpTextLayout.setText(font, "HP: " + playerBoat.HP + "/" + playerBoat.maxHP);
        xpTextLayout.setText(font, "XP: " + Integer.toString(xp));
        plunderTextLayout.setText(font, "Plunder: " + Integer.toString(plunder));
        font.getData().setScale(1);

        font.draw(batch, hpTextLayout, 5, Gdx.graphics.getHeight() - 10);
        font.draw(batch, xpTextLayout, Gdx.graphics.getWidth() - xpTextLayout.width - 5, Gdx.graphics.getHeight() - 50);
        font.draw(batch, plunderTextLayout, Gdx.graphics.getWidth() - plunderTextLayout.width - 5, Gdx.graphics.getHeight() - 10);

        stage.draw();

        batch.end(); //end the sprite batch

        //begin debug sprite batch
        ShapeRenderer sr = new ShapeRenderer();
        sr.begin(ShapeType.Line);
        sr.polygon(playerBoat.collisionPolygon.getTransformedVertices());
        sr.circle(playerBoat.collisionPolygon.getX()+playerBoat.collisionPolygon.getOriginX(),
        playerBoat.collisionPolygon.getY()+playerBoat.collisionPolygon.getOriginY(), 5);
        sr.end();
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

    public void gameOver(){

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

    public void DrawUpgradeButton(){
        // Create the upgrade button and add it to the UI stage
        menuButtonStyle = new TextButtonStyle();
        menuButtonStyle.font = font;
        menuButtonStyle.fontColor = Color.BLACK;
        menuButtonStyle.up = new TextureRegionDrawable(new Texture("ui/button.png"));
        menuButton = new TextButton("Upgrade", menuButtonStyle);
        
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO Auto-generated method stub
                // do some actions
                System.out.println("Button Pressed");
                upgradeMenuOpen = !upgradeMenuOpen;
                ToggleMenu();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(pointer == -1){
                    System.out.println("Button hovering over");
                    hoveringOverButton = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(pointer == -1){
                    System.out.println("Button left");
                    hoveringOverButton = false;
                }
            }
        });

        menuButton.setScale(1f, 1f);
        menuButton.setPosition(Gdx.graphics.getWidth() - 5 - menuButton.getWidth(), Gdx.graphics.getHeight() - 90 - menuButton.getHeight());

        stage.addActor(menuButton);
    }

    public void ToggleMenu(){
        // Put the XP menu drawing calls in its own function so that render doesn't get too cluttered
        if(!upgradeMenuInitialised) InitialiseMenu();
        
        if(upgradeMenuInitialised){
            if(upgradeMenuOpen){
                UpdateMenu();
                stage.addActor(upgradeMenuBackground);
                stage.addActor(upgradeButton1);
                stage.addActor(upgradeButton2);
            } else{ // Remove all menu elements from the stage
                upgradeMenuBackground.remove();
                upgradeButton1.remove();
                upgradeButton2.remove();
            }
        }
    }

    public void InitialiseMenu(){
        // Create the background
        upgradeMenuBackground = new Image(new Texture("ui/background.png"));
        upgradeMenuBackground.setPosition(Gdx.graphics.getWidth()/2 - upgradeMenuBackground.getWidth()/2, Gdx.graphics.getHeight()/2 - upgradeMenuBackground.getHeight()/2);

        upgradeMenuBackground.addListener(new ClickListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(pointer == -1){
                    System.out.println("Menu hovering over");
                    hoveringOverButton = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(pointer == -1){
                    System.out.println("Menu left");
                    hoveringOverButton = false;
                }
            }
        });

        // Create the upgrade buttons and add it to the UI stage
        upgradeButton1Style = new TextButtonStyle();
        upgradeButton2Style = new TextButtonStyle();
        upgradeButton1Style.font = font;
        upgradeButton1Style.fontColor = Color.BLACK;
        upgradeButton1Style.up = new TextureRegionDrawable(new Texture("ui/upgradebutton.png"));

        upgradeButton2Style.font = font;
        upgradeButton2Style.fontColor = Color.BLACK;
        upgradeButton2Style.up = new TextureRegionDrawable(new Texture("ui/upgradebutton.png"));

        upgradeButton1 = new TextButton("", upgradeButton1Style);
        upgradeButton2 = new TextButton("", upgradeButton2Style);

        upgradeButton1.addListener(new ClickListener() {
            boolean clicked = false;
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // TODO Auto-generated method stub
                // do some actions
                if(clicked == false){
                    clicked = true;
                    if(xp >= upgrade1cost){
                        xp -= upgrade1cost;
                        BuyUpgrade(1);
                        RandomiseUpgrades();
                    }
                    
                }
                System.out.println("Button Pressed, clicked is " + clicked);
                return true;
            }
            
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // TODO Auto-generated method stub
                // do some actions
                clicked = false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(pointer == -1){
                    System.out.println("Button hovering over");
                    hoveringOverButton = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(pointer == -1){
                    System.out.println("Button left");
                    hoveringOverButton = false;
                }
            }
        });

        upgradeButton2.addListener(new ClickListener() {
            boolean clicked = false;
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // TODO Auto-generated method stub
                // do some actions
                if(clicked == false){
                    clicked = true;
                    if(xp >= upgrade2cost){
                        xp -= upgrade2cost;
                        BuyUpgrade(2);
                        RandomiseUpgrades();
                    }
                    System.out.println("Button Pressed");
                }
                return true;
            }
            
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // TODO Auto-generated method stub
                // do some actions
                clicked = false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(pointer == -1){
                    System.out.println("Button hovering over");
                    hoveringOverButton = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(pointer == -1){
                    System.out.println("Button left");
                    hoveringOverButton = false;
                }
            }
        });

        upgradeMenuInitialised = true;

        RandomiseUpgrades();

        stage.addActor(upgradeMenuBackground);
        stage.addActor(upgradeButton1);
        stage.addActor(upgradeButton2);
    }

    public void UpdateMenu(){
        // Update the upgrade buttons
        
        upgradeButton1.setText("Upgrade:\n" + upgrade1.label + " + " + upgrade1amount + "\nCost:\n" + upgrade1cost + " XP");
        

        upgradeButton1.setScale(1f, 1f);
        upgradeButton1.setPosition(Gdx.graphics.getWidth()/2 - upgradeMenuBackground.getWidth()/2 + 15, Gdx.graphics.getHeight()/2 + upgradeMenuBackground.getHeight()/2 - upgradeButton1.getHeight() - 15);

        upgradeButton2.setText("Upgrade:\n" + upgrade2.label + " + " + upgrade2amount + "\nCost:\n" + upgrade2cost + " XP");


        upgradeButton2.setScale(1f, 1f);
        upgradeButton2.setPosition(Gdx.graphics.getWidth()/2 + 35, Gdx.graphics.getHeight()/2 + upgradeMenuBackground.getHeight()/2 - upgradeButton2.getHeight() - 15);

        
    }

    void BuyUpgrade(int upgrade){
        System.out.println("upgrading");
        switch(upgrade){
            case 1:
                playerBoat.Upgrade(upgrade1, upgrade1amount);
                break;
            case 2:
                playerBoat.Upgrade(upgrade2, upgrade2amount);
                break;
        }
    }

    void RandomiseUpgrades(){
        Random r = new Random();
        switch(r.nextInt(3)){
            case 0: // Max Health
                upgrade1 = Upgrades.maxhealth;
                upgrade1amount = 10;
                upgrade1cost = 25;
                break;
            case 1: // Speed
                upgrade1 = Upgrades.speed;
                upgrade1amount = 6.25f;
                upgrade1cost = 25;
                break;
            case 2: // Turn Speed
                upgrade1 = Upgrades.turnspeed;
                upgrade1amount = 7.5f;
                upgrade1cost = 25;
                break;
        }

        switch(r.nextInt(3)){
            case 0: // Max Health
                upgrade2 = Upgrades.maxhealth;
                upgrade2amount = 10;
                upgrade2cost = 25;
                break;
            case 1: // Speed
                upgrade2 = Upgrades.speed;
                upgrade2amount = 6.25f;
                upgrade2cost = 25;
                break;
            case 2: // Turn Speed
                upgrade2 = Upgrades.turnspeed;
                upgrade2amount = 7.5f;
                upgrade2cost = 25;
                break;
        }

        UpdateMenu();
    }
}
