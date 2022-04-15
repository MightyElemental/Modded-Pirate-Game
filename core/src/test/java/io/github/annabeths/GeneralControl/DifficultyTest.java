package io.github.annabeths.GeneralControl;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import io.github.annabeths.Boats.NeutralBoat;
import io.github.annabeths.Colleges.College;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.GameScreens.Screens;
import io.github.annabeths.Level.GameMap;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import io.github.annabeths.Obstacles.Kraken;
import io.github.annabeths.Obstacles.Mine;
import com.badlogic.gdx.Graphics;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DifficultyTest {

    public static GameController easyG;
    public GameController mediumG;
    public GameController hardG;
    public static eng1game game;

    @BeforeAll
    public static void init(){
        game = new eng1game();
        Gdx.graphics = mock(Graphics.class);
    }

    @BeforeEach
    public void setup() {
        easyG = new GameController(game);
        mediumG = new GameController(game);
        hardG = new GameController(game);


        easyG.setDifficulty(Difficulty.EASY);
        mediumG.setDifficulty(Difficulty.MEDIUM);
        hardG.setDifficulty(Difficulty.HARD);

    }

    public int countMines(GameController g){
        int n = 0;
        for(PhysicsObject obj : g.physicsObjects){
            if(obj instanceof Mine){
                n += 0;
            }
        }
        return n;
    }

    public boolean isKraken(GameController g){
        for(PhysicsObject obj : g.physicsObjects){
            if(obj instanceof Kraken){
                return true;
            }
        }
        return false;
    }

    @Test
    public void testNumMinesCorrect(){
        assertEquals(countMines(easyG), 40);
        assertEquals(countMines(mediumG), 50);
        assertEquals(countMines(hardG), 75);
    }

    @Test
    public void testIfKrakenIsPresent(){
        assertEquals(isKraken(easyG), false);
        assertEquals(isKraken(mediumG), true);
        assertEquals(isKraken(hardG), true);
    }

}
