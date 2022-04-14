package io.github.annabeths.UI;

import static io.github.annabeths.GeneralControl.ResourceManager.font;
import static io.github.annabeths.GeneralControl.ResourceManager.getTexture;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip.TextTooltipStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;

import io.github.annabeths.GameGenerics.GameObject;
import io.github.annabeths.GameGenerics.Upgrades;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.GeneralControl.DebugUtils;

public class HUD extends GameObject {

	Stage stage;

	/* Shop objects */

	TextButton shopButton;
	TextButtonStyle shopButtonStyle;

	TextButton upgradeButton1;
	TextButtonStyle upgradeButton1Style;

	TextButton upgradeButton2;
	TextButtonStyle upgradeButton2Style;

	Image upgradeMenuBackground;

	/* HUD objects */

	/** The image that serves as a base for the HUD */
	Image hudBg;

	Group hudGroup;

	LabelStyle lblStyle;
	Label hpText;
	Label timerText;
	Label xpText;
	Label plunderText;
	Label powerText;

	private GameController gc;

	/** Used to disable certain player behaviors when hovering over a button */
	public boolean hoveringOverButton = false;
	boolean upgradeMenuOpen = false;
	boolean upgradeMenuInitialised = false; // Set to true once initialized

	Upgrades upgrade1;
	int upgrade1cost;
	float upgrade1amount;
	Upgrades upgrade2;
	int upgrade2cost;
	float upgrade2amount;

	public HUD(GameController gameController) {
		gc = gameController;

		lblStyle = new LabelStyle(font, new Color(0, 0, 0, 1));

		stage = new Stage(new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		Gdx.input.setInputProcessor(stage);

		hudGroup = new Group();

		hudBg = new Image(getTexture("ui/hud-ui.png"));
		hudBg.setScaling(Scaling.fit);

		hudGroup.addActor(hudBg);

		hpText = new Label("#", lblStyle);
		timerText = new Label("#", lblStyle);
		xpText = new Label("#", lblStyle);
		plunderText = new Label("#", lblStyle);
		powerText = new Label("#", lblStyle);

		// TODO: Fix scaling
		timerText.setPosition(1920 - 158, 172);
		timerText.setFontScale(1920f / stage.getViewport().getScreenWidth());
		plunderText.setPosition(1920 - 180, 112);
		plunderText.setFontScale(1920f / stage.getViewport().getScreenWidth());

		// hudGroup.addActor(hpText);
		hudGroup.addActor(timerText);
		// hudGroup.addActor(xpText);
		hudGroup.addActor(plunderText);
		hudGroup.addActor(powerText);

		hudGroup.setWidth(stage.getViewport().getScreenWidth());
		hudGroup.setScale(stage.getViewport().getScreenWidth() / 1920f);
		stage.addActor(hudGroup);

		DrawUpgradeButton();
	}

	public void updateLabelPositions() {
		float y = stage.getViewport().getScreenHeight();
		hpText.setPosition(5, y -= hpText.getPrefHeight());
		powerText.setPosition(5, y -= powerText.getPrefHeight());

		y = stage.getViewport().getScreenHeight();
		xpText.setPosition(stage.getViewport().getScreenWidth() - xpText.getPrefWidth(),
				y -= xpText.getPrefHeight());
	}

	@Override
	public void Update(float delta) {
		hpText.setText(String.format("HP: %.0f/%.0f", gc.playerBoat.getHealth(),
				gc.playerBoat.getMaxHealth()));
		xpText.setText("XP: " + Integer.toString((int) gc.xp));
		timerText.setText(generateTimeString((int) gc.timer));
		plunderText.setText("$" + Integer.toString(gc.plunder));

		StringBuilder powerupText = new StringBuilder();
		gc.playerBoat.activePowerups.forEach((k, v) -> {
			String line = String.format("%s %.1fs\n", k.getName(), v);
			powerupText.append(line);
		});
		powerText.setText(powerupText.toString());

		updateLabelPositions();

		// font.getData().setScale(1);
	}

	public static String generateTimeString(int seconds) {
		int sec = seconds % 60;
		int min = seconds / 60;
		return min > 0 ? String.format("%d\'%02d\"", min, sec) : String.format("%02d seconds", sec);
	}

	@Override
	public void Draw(SpriteBatch batch) {
		stage.draw();

		Camera camera = stage.getViewport().getCamera();

		gc.batch.setProjectionMatrix(camera.combined);
		gc.batch.begin();
		if (DebugUtils.DRAW_DEBUG_TEXT) DebugUtils.drawDebugText(gc, batch);
		gc.batch.end();

		// reset view so it doesn't move
		gc.sr.setProjectionMatrix(camera.combined);
		// draw
		gc.sr.begin(ShapeType.Filled);

		drawHealth();
		drawXp();

		gc.sr.end();
	}

	private void drawHealth() {
		float hr = gc.playerBoat.getHealth() / gc.playerBoat.getMaxHealth();
		gc.sr.setColor(gc.playerBoat.isInvincible() ? Color.ROYAL : Color.SCARLET);
		drawHudScaledBar(812, 92, 377 * hr, 35);
	}

	private void drawXp() {
		float xpr = gc.getXpInLevel() / gc.getXpRequiredForNextLevel();
		gc.sr.setColor(Color.LIME);
		drawHudScaledBar(812, 24, 377 * xpr, 35);
	}

	public void drawHudScaledBar(float x, float y, float width, float height) {
		Vector2 pos = hudBg.localToStageCoordinates(new Vector2(x, y));
		Vector2 size = hudBg.localToStageCoordinates(new Vector2(width, height));
		drawBar(pos.x, pos.y, size.x, size.y);
	}

	public void drawBar(float x, float y, float width, float height) {
		gc.sr.rect(x, y, width, height);
	}

	/* UI & Upgrade Functions */

	public void DrawUpgradeButton() {
		Drawable buttonBg = new TextureRegionDrawable(getTexture("ui/button.png"));
		// Create the upgrade button and add it to the UI stage
		shopButtonStyle = new TextButtonStyle();
		shopButtonStyle.font = font;
		shopButtonStyle.fontColor = Color.BLACK;
		shopButtonStyle.up = buttonBg;
		shopButton = new TextButton("Shop", shopButtonStyle);

		shopButton.addListener(
				new TextTooltip("This is some text", new TextTooltipStyle(lblStyle, buttonBg)));

		shopButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				upgradeMenuOpen = !upgradeMenuOpen;
				ToggleMenu();
			}

			// Giving them enter and exit functions so that the player can't fire with left
			// click while hovering over a button.
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				if (pointer == -1) {
					hoveringOverButton = true;
					System.out.println("Hovering over");
				}
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				if (pointer == -1) {
					hoveringOverButton = false;
				}
			}
		});

		shopButton.setScale(1.5f);
		shopButton.setPosition(1920 - 160, 10);

		hudGroup.addActor(shopButton);
	}

	public void ToggleMenu() {
		// Put the XP menu drawing calls in its own function so that render doesn't get
		// too cluttered

		// Initialize the menu if it hasn't been, this avoids repeatedly creating new
		// buttons.
		if (!upgradeMenuInitialised) InitialiseMenu();

		// Add/re-add the UI elements back to the stage
		if (upgradeMenuOpen) {
			UpdateMenu();
			stage.addActor(upgradeMenuBackground);
			stage.addActor(upgradeButton1);
			stage.addActor(upgradeButton2);
		} else {
			// Remove all menu elements from the stage
			upgradeMenuBackground.remove();
			upgradeButton1.remove();
			upgradeButton2.remove();
		}
	}

	/**
	 * Creates the menu for the first time, and also generates the first set of
	 * upgrades.
	 */
	public void InitialiseMenu() {
		// Create the background
		upgradeMenuBackground = new Image(getTexture("ui/background.png"));
		upgradeMenuBackground.setPosition(
				Gdx.graphics.getWidth() / 2 - upgradeMenuBackground.getWidth() / 2,
				Gdx.graphics.getHeight() / 2 - upgradeMenuBackground.getHeight() / 2);

		upgradeMenuBackground.addListener(new ClickListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				if (pointer == -1) {
					hoveringOverButton = true;
				}
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				if (pointer == -1) {
					hoveringOverButton = false;
				}
			}
		});

		// Create the upgrade buttons
		upgradeButton1Style = new TextButtonStyle();
		upgradeButton1Style.font = font;
		upgradeButton1Style.fontColor = Color.BLACK;
		upgradeButton1Style.up = new TextureRegionDrawable(getTexture("ui/upgradebutton.png"));

		upgradeButton2Style = new TextButtonStyle();
		upgradeButton2Style.font = font;
		upgradeButton2Style.fontColor = Color.BLACK;
		upgradeButton2Style.up = new TextureRegionDrawable(getTexture("ui/upgradebutton.png"));

		upgradeButton1 = new TextButton("", upgradeButton1Style);
		upgradeButton2 = new TextButton("", upgradeButton2Style);

		upgradeButton1.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (gc.xp >= upgrade1cost) {
					gc.xp -= upgrade1cost;
					BuyUpgrade(1);
					RandomiseUpgrades();
				}
				return true;
			}

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				if (pointer == -1) {
					hoveringOverButton = true;
				}
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				if (pointer == -1) {
					hoveringOverButton = false;
				}
			}
		});

		upgradeButton2.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (gc.xp >= upgrade2cost) {
					gc.xp -= upgrade2cost;
					BuyUpgrade(2);
					RandomiseUpgrades();
				}
				return true;
			}

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				if (pointer == -1) {
					hoveringOverButton = true;
				}
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				if (pointer == -1) {
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

	public void UpdateMenu() {
		// Update the upgrade buttons

		upgradeButton1.setText(
				!(upgrade1 == Upgrades.projectiledamage || upgrade1 == Upgrades.projectilespeed)
						? "Upgrade:\n" + upgrade1.label + " + " + upgrade1amount + "\nCost:\n"
								+ upgrade1cost + " XP"
						: "Upgrade:\n" + upgrade1.label + " + " + upgrade1amount * 100
								+ "%\nCost:\n" + upgrade1cost + " XP");

		upgradeButton1.setScale(1f, 1f);
		upgradeButton1.setPosition(
				Gdx.graphics.getWidth() / 2 - upgradeMenuBackground.getWidth() / 2 + 15,
				Gdx.graphics.getHeight() / 2 + upgradeMenuBackground.getHeight() / 2
						- upgradeButton1.getHeight() - 15);

		upgradeButton2.setText(
				!(upgrade2 == Upgrades.projectiledamage || upgrade2 == Upgrades.projectilespeed)
						? "Upgrade:\n" + upgrade2.label + " + " + upgrade2amount + "\nCost:\n"
								+ upgrade2cost + " XP"
						: "Upgrade:\n" + upgrade2.label + " + " + upgrade2amount * 100
								+ "%\nCost:\n" + upgrade2cost + " XP");

		upgradeButton2.setScale(1f, 1f);
		upgradeButton2.setPosition(Gdx.graphics.getWidth() / 2 + 35, Gdx.graphics.getHeight() / 2
				+ upgradeMenuBackground.getHeight() / 2 - upgradeButton2.getHeight() - 15);
	}

	void BuyUpgrade(int upgrade) {
		switch (upgrade) {
		case 1:
			gc.playerBoat.Upgrade(upgrade1, upgrade1amount);
			break;
		case 2:
			gc.playerBoat.Upgrade(upgrade2, upgrade2amount);
			break;
		}
	}

	void RandomiseUpgrades() {
		Random r = new Random();
		switch (r.nextInt(6)) {
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

		switch (r.nextInt(6)) {
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

	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
}
