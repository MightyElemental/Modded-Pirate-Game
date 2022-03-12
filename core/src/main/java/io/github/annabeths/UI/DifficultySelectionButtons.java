package io.github.annabeths.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

public class DifficultySelectionButtons extends GameObject {

	Stage stage;

	TextButton menuButton;
	TextButtonStyle menuButtonStyle;

	TextButton diffButton1;
	TextButtonStyle diffButton1Style;

	TextButton diffButton2;
	TextButtonStyle diffButton2Style;

	TextButton diffButton3;
	TextButtonStyle diffButton3Style;

	TextButton diffButton4;
	TextButtonStyle diffButton4Style;
	Image diffMenuBackground;

	BitmapFont font;

	/** Used to disable certain player behaviors when hovering over a button */
	public boolean hoveringOverButton = false;
	boolean diffMenuOpen = false;
	boolean diffMenuInitialised = false; // Set to true once initialized
	boolean isEasy_clicked = false;
	boolean isMiddle_clicked = false;
	boolean isHard_clicked = false;
	boolean isEsc_clicked = false;

	public DifficultySelectionButtons() {
		stage = new Stage(); // Lets us implement interactable UI elements
		font = new BitmapFont(Gdx.files.internal("fonts/bobcat.fnt"), false);
		Gdx.input.setInputProcessor(stage);
		DrawdiffButton();
	}

	// UI & diff Functions
	@Override
	public void Draw(SpriteBatch batch) {
		stage.draw();
	}

	public void DrawdiffButton() {
		// Create the diff button and add it to the UI stage
		menuButtonStyle = new TextButtonStyle();
		menuButtonStyle.font = font;
		menuButtonStyle.fontColor = Color.BLACK;
		menuButtonStyle.up = new TextureRegionDrawable(new Texture("ui/button.png"));
		menuButton = new TextButton("Game Difficulty Selection", menuButtonStyle);

		menuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				diffMenuOpen = !diffMenuOpen;
				ToggleMenu();
			}
		});
		menuButton.setScale(1f, 1f);
		menuButton.setPosition(Gdx.graphics.getWidth() / 2 - menuButton.getWidth() / 2,
				Gdx.graphics.getHeight() - menuButton.getHeight());

		stage.addActor(menuButton);
	}

	public void ToggleMenu() {
		if (!diffMenuInitialised) InitialiseMenu();
		// Add/re-add the UI elements back to the stage
		if (diffMenuOpen) {
			UpdateMenu();
			stage.addActor(diffMenuBackground);
			stage.addActor(diffButton1);
			stage.addActor(diffButton2);
			stage.addActor(diffButton3);
			stage.addActor(diffButton4);
		} else {
			// Remove all menu elements from the stage
			diffMenuBackground.remove();
			diffButton1.remove();
			diffButton2.remove();
			diffButton3.remove();
			diffButton4.remove();
		}
	}

	public void InitialiseMenu() {
		// Create the background
		diffMenuBackground = new Image(new Texture("ui/background.png"));
		diffMenuBackground.setPosition(
				Gdx.graphics.getWidth() / 2 - diffMenuBackground.getWidth() / 2,
				Gdx.graphics.getHeight() / 2 - diffMenuBackground.getHeight() / 2);

		diffMenuBackground.addListener(new ClickListener() {

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

		// Create the diff buttons
		diffButton1Style = new TextButtonStyle();
		diffButton1Style.font = font;
		diffButton1Style.fontColor = Color.BLACK;
		diffButton1Style.up = new TextureRegionDrawable(new Texture("ui/upgradebutton.png"));

		diffButton2Style = new TextButtonStyle();
		diffButton2Style.font = font;
		diffButton2Style.fontColor = Color.BLACK;
		diffButton2Style.up = new TextureRegionDrawable(new Texture("ui/upgradebutton.png"));

		diffButton3Style = new TextButtonStyle();
		diffButton3Style.font = font;
		diffButton3Style.fontColor = Color.BLACK;
		diffButton3Style.up = new TextureRegionDrawable(new Texture("ui/upgradebutton.png"));

		diffButton4Style = new TextButtonStyle();
		diffButton4Style.font = font;
		diffButton4Style.fontColor = Color.RED;
		diffButton4Style.up = new TextureRegionDrawable(new Texture("ui/upgradebutton.png"));

		diffButton1 = new TextButton("", diffButton1Style);
		diffButton2 = new TextButton("", diffButton2Style);
		diffButton3 = new TextButton("", diffButton3Style);
		diffButton4 = new TextButton("", diffButton4Style);

		diffButton1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (diffButton1.isChecked()) {
					isEasy_clicked = true;
				}
			}
		});

		diffButton2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (diffButton2.isChecked()) {
					isMiddle_clicked = true;
				}
			}
		});

		diffButton3.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (diffButton3.isChecked()) {
					isHard_clicked = true;
				}
			}
		});

		diffButton4.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (diffButton4.isChecked()) {
					isEsc_clicked = true;
				}
			}
		});

		diffMenuInitialised = true;
		stage.addActor(diffMenuBackground);
		stage.addActor(diffButton1);
		stage.addActor(diffButton2);
		stage.addActor(diffButton3);
		stage.addActor(diffButton4);
	}

	public void UpdateMenu() {
		diffButton1.setText("EASY");
		diffButton1.setSize(150, 400);
		diffButton1.setScale(1f, 1f);
		diffButton1.setPosition(310, 150);

		diffButton2.setText("Middle");
		diffButton2.setSize(150, 400);
		diffButton2.setScale(1f, 1f);
		diffButton2.setPosition(470, 150);

		diffButton3.setText("Hard");
		diffButton3.setSize(150, 400);
		diffButton3.setScale(1f, 1f);
		diffButton3.setPosition(630, 150);

		diffButton4.setText("Return\n" + " to\n" + " Begin");
		diffButton4.setSize(150, 400);
		diffButton4.setScale(1f, 1f);
		diffButton4.setPosition(790, 150);
	}

	public boolean is_easy() {
		return isEasy_clicked;
	}

	public boolean is_hard() {
		return isHard_clicked;
	}

	public boolean is_middle() {
		return isMiddle_clicked;
	}

	public boolean is_esc() {
		return isEsc_clicked;
	}

}
