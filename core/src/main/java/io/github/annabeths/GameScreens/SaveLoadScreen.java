package io.github.annabeths.GameScreens;

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

import io.github.annabeths.GeneralControl.ResourceManager;
import io.github.annabeths.GeneralControl.SaveManager;
import io.github.annabeths.GeneralControl.eng1game;

/**
 * Menu that allows you to load from save files.
 * @since  Assessment 2
 * @author Hector Woods
 */
public class SaveLoadScreen implements Screen {

    private Stage stage;

    /** The game object used to change screens */
    private final eng1game game;

    /** Array of the menu buttons */
    private final TextButton[] buttons;

    /** Array of the save slot buttons */
    private final TextButton[] saveSlotButtons;

    /** A collection of actions to perform when the associated key is pressed */
    private final Map<Integer, Consumer<InputEvent>> keyActions;

    private final boolean showLoadScreenImmediately;

    /**
     * Constructor for SaveLoadScreen.
     * @param g reference to eng1game
     * @param showLoadScreenImmediately whether the save slot buttons should be shown immediately, or not.
     */
    public SaveLoadScreen(eng1game g, boolean showLoadScreenImmediately) {
        game = g;
        keyActions = new HashMap<>();
        buttons = new TextButton[3];
        saveSlotButtons = new TextButton[4];
        this.showLoadScreenImmediately = showLoadScreenImmediately;
    }

    /**
     * Set up the save slot buttons.
     */
    public void setUpSaveSlotButtons(){
        stage.clear();
        // Size of each button
        Vector2 btnSize = new Vector2(250, 100);
        int[] buttonKeys = { Keys.NUM_1, Keys.NUM_2, Keys.NUM_3, Keys.NUM_4};
        // y position of the buttons
        float btnY = (Gdx.graphics.getHeight() - btnSize.y) / 2;
        // horizontal margin of each button
        float btnXMarg = 25;
        // Total width of buttons
        float menuWidth = (btnSize.x + btnXMarg) * 4 - btnXMarg;
        // left-most x position of menu
        float menuXPos = (Gdx.graphics.getWidth() - menuWidth) / 2 + (btnXMarg);

        // Define style for the buttons
        TextButtonStyle style = new TextButtonStyle();
        style.font = font;
        style.fontColor = Color.BLACK;
        style.up = new TextureRegionDrawable(getTexture("ui/upgradebutton.png"));

        // Create buttons
        for(int i = 0; i < saveSlotButtons.length; i++){
            // Define the button
            if(i < 3) {
                String buttonText = "Slot " + (i+1);
                boolean saveFileExists = SaveManager.doesSaveFileExist("save" + i);
                if(!saveFileExists){
                    buttonText = buttonText + " [Empty]";
                }
                saveSlotButtons[i] = new TextButton(buttonText, style);
                if(saveFileExists){
                    loadGameBtn(buttonKeys[i], saveSlotButtons[i],"save" + i);
                }
            }else{
                saveSlotButtons[i] = new TextButton("<- Back",style);
                newBackButton(buttonKeys[i], saveSlotButtons[i]);
            }
            saveSlotButtons[i].setSize(btnSize.x, btnSize.y);
            // Position the button
            float x = menuXPos + i * (btnSize.x + btnXMarg);
            saveSlotButtons[i].setPosition(x, btnY);
            // Add button to stage
            stage.addActor(saveSlotButtons[i]);
        }

    }

    /**
     * Set up the 'New Game', 'Load game' and 'Return to menu' buttons.
     */
    public void setupInitialButtons() {
        stage.clear();
        // Size of each button
        Vector2 btnSize = new Vector2(250, 250);
        // Array of colors the text should be
        Color[] textColors = { Color.GREEN, Color.ORANGE, Color.BLACK };
        // Array of text to display on the buttons
        String[] buttonText = { "New game", "Load game", "RETURN\nTO MENU" };
        int[] buttonKeys = { Keys.N, Keys.L, Keys.H };

        // y position of the buttons
        float btnY = (Gdx.graphics.getHeight() - btnSize.y) / 2;
        // horizontal margin of each button
        float btnXMarg = 25;
        // Total width of buttons
        float menuWidth = (btnSize.x + btnXMarg) * 4 - btnXMarg;
        // left-most x position of menu
        float menuXPos = (Gdx.graphics.getWidth() - menuWidth) / 2 + (btnXMarg*5);


        // Create buttons
        for(int i = 0; i < buttons.length; i++){
            // Define style for the button
            TextButtonStyle style = new TextButtonStyle();
            style.font = font;
            style.fontColor = textColors[i];
            style.up = new TextureRegionDrawable(getTexture("ui/upgradebutton.png"));

            // Define the button
            buttons[i] = new TextButton(buttonText[i], style);
            buttons[i].setSize(btnSize.x, btnSize.y);

            // Position the button
            float x = menuXPos + i * (btnSize.x + btnXMarg);
            buttons[i].setPosition(x, btnY);

            // Add button to stage
            stage.addActor(buttons[i]);
        }

        // Define behaviour for new game button
        newGameBtn(buttonKeys[0], buttons[0]);
        // Now for load game button
        loadGameMenuBtn(buttonKeys[1], buttons[1]);
        // Finally, for return to menu button
        clickListener(buttons[2], event -> game.gotoScreen(Screens.menuScreen));
        keyActions.put(Keys.ESCAPE, event -> game.gotoScreen(Screens.menuScreen));
    }

    /**
     * A button that loads a save slot.
     * @param key keyboard key that also triggers the button
     * @param btn a TextButton
     * @param saveFileName the name of the save file to be loaded.
     */
    public void loadGameBtn(int key, TextButton btn, String saveFileName){
        Consumer<InputEvent> actions = event -> game.loadSaveGame(saveFileName);
        clickListener(btn, actions);
        keyActions.put(key, actions);
    }

    /**
     * Button that starts a new game; opens Difficulty menu.
     * @param key keyboard key that also triggers the button
     * @param btn a TextButton
     */
    public void newGameBtn(int key, TextButton btn) {
        Consumer<InputEvent> actions = event -> game.gotoScreen(Screens.gameDifScreen);
        clickListener(btn, actions);
        keyActions.put(key, actions);
    }

    /**
     * Button that returns to the menu.
     * @param key keyboard key that also triggers the button
     * @param btn a TextButton
     */
    public void newBackButton(int key, TextButton btn){
        Consumer<InputEvent> actions = event -> setupInitialButtons();
        clickListener(btn, actions);
        keyActions.put(key, actions);
    }
    /**
     * Button that shows the save slot buttons.
     * @param key keyboard key that also triggers the button
     * @param btn a TextButton
     */
    public void loadGameMenuBtn(int key, TextButton btn) {
        Consumer<InputEvent> actions = event -> setUpSaveSlotButtons();
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
     * Called when the screen is created.
     */
    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        setupInitialButtons();
        if(showLoadScreenImmediately){
            setUpSaveSlotButtons();
        }
        setupLabel();
    }

    /**
     * Set up and show a label.
     */
    public void setupLabel() {
        LabelStyle style = new LabelStyle();
        style.font = ResourceManager.font;
        style.fontColor = Color.WHITE;
        Label l = new Label("Start a new game, or load from a save?", style);
        l.setPosition(10, 10);

        stage.addActor(l);
    }

    /**
     * Render all buttons and other text.
     * @param delta time since last frame.
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

}
