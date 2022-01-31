package io.github.annabeths.UI;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import io.github.annabeths.GameGenerics.GameObject;
import io.github.annabeths.GameGenerics.Upgrades;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Level.WaterBackground;

public class HUD extends GameObject{


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

    GlyphLayout xpTextLayout;
    GlyphLayout plunderTextLayout;

    BitmapFont font;
    GameController gc;

    public boolean hoveringOverButton = false; // Disable certain player behaviours when hovering over a button
    boolean upgradeMenuOpen = false;
    boolean upgradeMenuInitialised = false; // Set to true once initialised

    Upgrades upgrade1;
    int upgrade1cost;
    float upgrade1amount;
    Upgrades upgrade2;
    int upgrade2cost;
    float upgrade2amount;

    public HUD(GameController gameController)
    {
        gc = gameController;
        stage = new Stage();
        font = new BitmapFont(Gdx.files.internal("fonts/bobcat.fnt"), false);
		hpTextLayout = new GlyphLayout();
        xpTextLayout = new GlyphLayout();
		plunderTextLayout = new GlyphLayout();
        Gdx.input.setInputProcessor(stage);
        DrawUpgradeButton(); // put this in its own function to make this function look a bit cleaner
    }

    @Override
    public void Update(float delta)
    {
        hpTextLayout.setText(font, "HP: " + gc.playerBoat.HP + "/" + gc.playerBoat.maxHP);
        xpTextLayout.setText(font, "XP: " + Integer.toString(gc.xp));
        plunderTextLayout.setText(font, "Plunder: " + Integer.toString(gc.plunder));
        font.getData().setScale(1);
    }

    @Override
    public void Draw(SpriteBatch batch)
    {
        // Draw the text showing the player's stats
        font.draw(batch, hpTextLayout, 5, gc.map.camera.viewportHeight - 10);
        font.draw(batch, xpTextLayout, gc.map.camera.viewportWidth - xpTextLayout.width - 5, gc.map.camera.viewportHeight - 50);
        font.draw(batch, plunderTextLayout, gc.map.camera.viewportWidth - plunderTextLayout.width - 5, gc.map.camera.viewportHeight - 10);

        stage.draw();
    }
    
    // UI & Upgrade Functions

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
                upgradeMenuOpen = !upgradeMenuOpen;
                ToggleMenu();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(pointer == -1){
                    hoveringOverButton = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(pointer == -1){
                    hoveringOverButton = false;
                }
            }
        });

        menuButton.setScale(1f, 1f);
        menuButton.setPosition(Gdx.graphics.getWidth()/2 - menuButton.getWidth()/2, Gdx.graphics.getHeight() - menuButton.getHeight());

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
                    hoveringOverButton = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(pointer == -1){
                    hoveringOverButton = false;
                }
            }
        });

        // Create the upgrade buttons and add it to the UI stage
        upgradeButton1Style = new TextButtonStyle();   
        upgradeButton1Style.font = font;
        upgradeButton1Style.fontColor = Color.BLACK;
        upgradeButton1Style.up = new TextureRegionDrawable(new Texture("ui/upgradebutton.png"));

        upgradeButton2Style = new TextButtonStyle();
        upgradeButton2Style.font = font;
        upgradeButton2Style.fontColor = Color.BLACK;
        upgradeButton2Style.up = new TextureRegionDrawable(new Texture("ui/upgradebutton.png"));

        upgradeButton1 = new TextButton("", upgradeButton1Style);
        upgradeButton2 = new TextButton("", upgradeButton2Style);

        upgradeButton1.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // TODO Auto-generated method stub
                // do some actions
                if(gc.xp >= upgrade1cost){
                    gc.xp -= upgrade1cost;
                    BuyUpgrade(1);
                    RandomiseUpgrades();
                }
                return true;
            }
            
            

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(pointer == -1){
                    hoveringOverButton = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(pointer == -1){
                    hoveringOverButton = false;
                }
            }
        });

        upgradeButton2.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // TODO Auto-generated method stub
                // do some actions
                if(gc.xp >= upgrade2cost){
                    gc.xp -= upgrade2cost;
                    BuyUpgrade(2);
                    RandomiseUpgrades();
                }
                return true;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(pointer == -1){
                    hoveringOverButton = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(pointer == -1){
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
        
        upgradeButton1.setText(!(upgrade1 == Upgrades.projectiledamage || upgrade1 == Upgrades.projectilespeed) ?
        "Upgrade:\n" + upgrade1.label + " + " + upgrade1amount + "\nCost:\n" + upgrade1cost + " XP" : 
        "Upgrade:\n" + upgrade1.label + " + " + upgrade1amount * 100 + "%\nCost:\n" + upgrade1cost + " XP");
        

        upgradeButton1.setScale(1f, 1f);
        upgradeButton1.setPosition(Gdx.graphics.getWidth()/2 - upgradeMenuBackground.getWidth()/2 + 15, Gdx.graphics.getHeight()/2 + upgradeMenuBackground.getHeight()/2 - upgradeButton1.getHeight() - 15);

        upgradeButton2.setText(!(upgrade2 == Upgrades.projectiledamage || upgrade2 == Upgrades.projectilespeed) ?
            "Upgrade:\n" + upgrade2.label + " + " + upgrade2amount + "\nCost:\n" + upgrade2cost + " XP" : 
            "Upgrade:\n" + upgrade2.label + " + " + upgrade2amount * 100 + "%\nCost:\n" + upgrade2cost + " XP");


        upgradeButton2.setScale(1f, 1f);
        upgradeButton2.setPosition(Gdx.graphics.getWidth()/2 + 35, Gdx.graphics.getHeight()/2 + upgradeMenuBackground.getHeight()/2 - upgradeButton2.getHeight() - 15);
    }

    void BuyUpgrade(int upgrade){
        switch(upgrade){
            case 1:
                gc.playerBoat.Upgrade(upgrade1, upgrade1amount);
                break;
            case 2:
                gc.playerBoat.Upgrade(upgrade2, upgrade2amount);
                break;
        }
    }

    void RandomiseUpgrades(){
        Random r = new Random();
        switch(r.nextInt(6)){
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
            case 3: // Damage
                upgrade1 = Upgrades.projectiledamage;
                upgrade1amount = 0.1f;
                upgrade1cost = 25;
                break;
            case 4: // Speed
                upgrade1 = Upgrades.projectilespeed;
                upgrade1amount = 0.05f;
                upgrade1cost = 25;
                break;
            case 5: // Defense
                upgrade1 = Upgrades.defense;
                upgrade1amount = 1f;
                upgrade1cost = 35;
                break;
        }

        switch(r.nextInt(6)){
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
            case 3: // Damage
                upgrade2 = Upgrades.projectiledamage;
                upgrade2amount = 0.1f;
                upgrade2cost = 25;
                break;
            case 4: // Speed
                upgrade2 = Upgrades.projectilespeed;
                upgrade2amount = 0.05f;
                upgrade2cost = 25;
                break;
            case 5: // Defense
                upgrade2 = Upgrades.defense;
                upgrade2amount = 1f;
                upgrade2cost = 35;
                break;
        }

        UpdateMenu();
    }
}
