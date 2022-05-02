package io.github.annabeths.GameScreens;

import static io.github.annabeths.GeneralControl.Difficulty.EASY;
import static io.github.annabeths.GeneralControl.Difficulty.HARD;
import static io.github.annabeths.GeneralControl.Difficulty.MEDIUM;
import static io.github.annabeths.GeneralControl.ResourceManager.font;
import static io.github.annabeths.GeneralControl.ResourceManager.getTexture;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.annabeths.GeneralControl.Difficulty;
import io.github.annabeths.GeneralControl.ResourceManager;
import io.github.annabeths.GeneralControl.eng1game;

/**
 * The menu where the game difficulty is selected
 * 
 * @tt.updated Assessment 2
 * @author James Burnell
 * @author Hector Woods
 */
public class GameDifScreen implements Screen {

	private Stage stage;

	/** The game object used to change screens */
	private final eng1game game;

	/** Array of the menu buttons */
	private final TextButton[] buttons;

	/** A collection of actions to perform when the associated key is pressed */
	private final Map<Integer, Consumer<InputEvent>> keyActions;

	/**
	 * Constructor for GameDifScreen
	 * @param g reference to an eng1game instance
	 */
	public GameDifScreen(eng1game g) {
		game = g;
		keyActions = new HashMap<>();
		buttons = new TextButton[4];
	}

	/**
	 * Method that sets up the difficulty option buttons.
	 */
	public void setupButtons() {
		// Size of each button
		Vector2 btnSize = new Vector2(250, 250);
		// Array of colors the text should be
		Color[] textColors = { Color.GREEN, Color.ORANGE, Color.RED, Color.BLACK };
		// Array of text to display on the buttons
		String[] buttonText = { "EASY", "MEDIUM", "HARD", "Back" };
		Difficulty[] buttonDiff = { EASY, MEDIUM, HARD };
		int[] buttonKeys = { Keys.E, Keys.M, Keys.H };

		// y position of the buttons
		float btnY = (Gdx.graphics.getHeight() - btnSize.y) / 2;
		// horizontal margin of each button
		float btnXMarg = 50;
		// Total width of buttons
		float menuWidth = (btnSize.x + btnXMarg) * 4 - btnXMarg;
		// left-most x position of menu
		float menuXPos = (Gdx.graphics.getWidth() - menuWidth) / 2;

		// Set up the styles of the buttons
		for (int i = 0; i < 4; i++) {
			// Define style for the button
			TextButtonStyle style = new TextButtonStyle();
			style.font = font;
			style.fontColor = textColors[i];
			style.up = new TextureRegionDrawable(getTexture("ui/upgradebutton.png"));

			// Define the button
			buttons[i] = new TextButton(buttonText[i], style);
			buttons[i].setSize(btnSize.x, btnSize.y);
			float x = menuXPos + i * (btnSize.x + btnXMarg);
			buttons[i].setPosition(x, btnY);

			// Define button click actions
			if (i < buttonDiff.length) {
				assignDifficultyToBtn(buttonKeys[i], buttons[i], buttonDiff[i]);
			}

			// Add button to stage
			stage.addActor(buttons[i]);
		}

		// Return to menu
		clickListener(buttons[3], event -> game.gotoScreen(Screens.saveLoadScreen));
		keyActions.put(Keys.ESCAPE, event -> game.gotoScreen(Screens.saveLoadScreen));
	}

	/**
	 * Maps a button and a key to an action that changes the game difficulty
	 * 
	 * @param key the key to press to activate the action
	 * @param btn the button to click to activate the action
	 * @param dif the difficulty to change the game to
	 * @see Keys
	 * @see #keyActions
	 */
	public void assignDifficultyToBtn(int key, TextButton btn, Difficulty dif) {
		Consumer<InputEvent> actions = event -> {
			game.setDifficulty(dif);
			game.gotoScreen(Screens.gameScreen);
		};
		clickListener(btn, actions);
		keyActions.put(key, actions);
	}

	/**
	 * Created a new click listener based on the action given and assigns it as a
	 * new listener to the actor. Used to make code cleaner elsewhere.
	 * 
	 * @param act the actor to apply the listener to
	 * @param actions the actions to run upon clicking the actor
	 * @author James Burnell
	 */
	public void clickListener(Actor act, Consumer<InputEvent> actions) {
		act.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				actions.accept(event);
			}
		});
	}

	/**
	 * Called when the screen is created
	 */
	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		setupButtons();
		setupLabel();
	}

	/**
	 * Initialize a new label
	 */
	public void setupLabel() {
		LabelStyle style = new LabelStyle();
		style.font = ResourceManager.font;
		style.fontColor = Color.WHITE;
		Label l = new Label("Click a button or press E/M/H to select difficulty", style);
		l.setPosition(10, 10);

		stage.addActor(l);
	}

	/**
	 * Called once per frame. Draw all buttons and other sprites.
	 * @param delta time since last frame
	 */
	@Override
	public void render(float delta) {
		// test for any pressed keys
		keyActions.forEach((key, action) -> {
			if (Gdx.input.isKeyJustPressed(key)) action.accept(null);
		});

		ScreenUtils.clear(Color.DARK_GRAY);
		stage.act();
		stage.draw();
	}

	/**
	 * resize the window
	 * @param width new width
	 * @param height new height
	 */
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
		stage.dispose();
	}

	/**
	 * @return the buttons
	 */
	public TextButton[] getButtons() {
		return buttons;
	}

}
