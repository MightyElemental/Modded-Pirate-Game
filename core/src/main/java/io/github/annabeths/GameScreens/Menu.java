package io.github.annabeths.GameScreens;

import static io.github.annabeths.GeneralControl.ResourceManager.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.annabeths.GeneralControl.ResourceManager;
import io.github.annabeths.GeneralControl.eng1game;
import io.github.annabeths.UI.HUD;

/**
 * The main menu.
 * @author James Burnell
 * @tt.updated Assessment 2
 */
public class Menu implements Screen {
	private SpriteBatch batch;
	GlyphLayout menuTextLayout;
	eng1game game;

	Texture instructions;
	/** whether the instructions should be rendered */
	boolean showInstructions;
	/** whether the instructions have been shown at least once */
	boolean instructionsBeenShown;
	private HUD hud;

	/**
	 * Constructor for Menu
	 * @param g reference to eng1game
	 */
	public Menu(eng1game g) {
		game = g;
	}

	/**
	 * Called when the screen is created.
	 */
	@Override
	public void show() {
		// layouts can be used to manage text to allow it to be centered
		menuTextLayout = new GlyphLayout();
		menuTextLayout.setText(font,
				"press ENTER to PLAY\n"
					+ "press I to toggle INSTRUCTIONS\n"
					+ "press F for fullscreen\n"
					+ "press C for credits\n"
					+ "press ESCAPE to QUIT");
		instructions = ResourceManager.getTexture("ui/instructions.png");
		reset();
	}

	/**
	 * Draw text to the screen and handle logic. Called once per frame
	 * @param delta time since last frame
	 */
	@Override
	public void render(float delta) {
		// do updates

		if (Gdx.input.isKeyJustPressed(Keys.I)) {
			toggleInstructions();
		} else if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			// Ensure the player has seen the instructions
			if (instructionsBeenShown) {
				// if the ENTER key is pressed, switch to the difficulty screen
				showInstructions = false;
				game.gotoScreen(Screens.saveLoadScreen);
			} else {
				toggleInstructions();
			}
		}
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			if (showInstructions)
				showInstructions = false;
			else
				Gdx.app.exit();
		}
		if (Gdx.input.isKeyJustPressed(Keys.C)) {
			game.gotoScreen(Screens.credits);
		}
		if (Gdx.input.isKeyJustPressed(Keys.F)) {
			game.setFullscreen();
		}

		// do draws
		ScreenUtils.clear(Color.BLACK);

		batch.begin(); // start batch
		font.getData().setScale(1);
		// the below line centers the text in the center of the screen
		font.draw(batch, menuTextLayout, Gdx.graphics.getWidth() / 2f - menuTextLayout.width / 2f,
				Gdx.graphics.getHeight() / 2f + menuTextLayout.height / 2f);
		if (showInstructions)
			batch.draw(instructions, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		batch.end(); // end batch
		if (showInstructions) hud.Draw(batch);
	}

	/**
	 * toggle whether the instructions should be shown or not.
	 */
	public void toggleInstructions() {
		showInstructions = !showInstructions;
		instructionsBeenShown = true;
	}

	/** Resets the menu graphics objects. Used for changing resolutions */
	public void reset() {
		batch = new SpriteBatch();
		// create example HUD
		hud = new HUD(GameController.getMockForHUD());
		hud.Update(1f);
	}

	/**
	 * resize the window
	 * @param width new width
	 * @param height new height
	 */
	@Override
	public void resize(int width, int height) {
		reset();
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

}
