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
 * The menu where the game difficulty is selected
 *
 * @tt.updated Assessment 2
 * @author Hector Woods
 */
public class SaveLoadScreen implements Screen {

    private Stage stage;

    /** The game object used to change screens */
    private eng1game game;

    /** Array of the menu buttons */
    private TextButton[] buttons;

    /** Array of the save slot buttons */
    private TextButton[] saveSlotButtons;

    /** A collection of actions to perform when the associated key is pressed */
    private Map<Integer, Consumer<InputEvent>> keyActions;

    public SaveLoadScreen(eng1game g) {
        game = g;
        keyActions = new HashMap<Integer, Consumer<InputEvent>>();
        buttons = new TextButton[3];
        saveSlotButtons = new TextButton[4];
    }

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
        float menuXPos = (Gdx.graphics.getWidth() - menuWidth) / 2 + (btnXMarg*5);

        // Define style for the buttons
        TextButtonStyle style = new TextButtonStyle();
        style.font = font;
        style.fontColor = Color.BLACK;
        style.up = new TextureRegionDrawable(getTexture("ui/upgradebutton.png"));

        // Create buttons
        for(int i = 0; i < saveSlotButtons.length; i++){
            // Define the button
            if(i < 3) {
                String buttonText = "Slot " + i;
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
        // Finally for return to menu button
        clickListener(buttons[2], event -> game.gotoScreen(Screens.menuScreen));
        keyActions.put(Keys.ESCAPE, event -> game.gotoScreen(Screens.menuScreen));
    }

    public void loadGameBtn(int key, TextButton btn, String saveFileName){
        Consumer<InputEvent> actions = event -> {
            game.gotoScreen(Screens.loadedGameScreen, saveFileName);
        };
        clickListener(btn, actions);
        keyActions.put(key, actions);
    }

    public void newGameBtn(int key, TextButton btn) {
        Consumer<InputEvent> actions = event -> {
            game.gotoScreen(Screens.gameDifScreen);
        };
        clickListener(btn, actions);
        keyActions.put(key, actions);
    }

    public void newBackButton(int key, TextButton btn){
        Consumer<InputEvent> actions = event -> {
            setupInitialButtons();
        };
        clickListener(btn, actions);
        keyActions.put(key, actions);
    }

    public void loadGameMenuBtn(int key, TextButton btn) {
        Consumer<InputEvent> actions = event -> {
            setUpSaveSlotButtons();
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

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        setupInitialButtons();
        setupLabel();
    }

    public void setupLabel() {
        LabelStyle style = new LabelStyle();
        style.font = ResourceManager.font;
        style.fontColor = Color.WHITE;
        Label l = new Label("Start a new game, or load from a save?", style);
        l.setPosition(10, 10);

        stage.addActor(l);
    }

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
