package io.github.annabeths.GeneralControl;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.Collectables.PowerupType;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Obstacles.Mine;

import java.util.*;

/**
 * @author Hector Woods
 * @since Assessment 2
 */
public abstract class SaveManager {


    /**
     * Save player powerups.
     * @param pref A libgdx preferences object
     * @param pUps A Map of PowerUpType -> Integer describing how much of each powerup the player has collected.
     */
    public static void savePowerups(Preferences pref, Map<PowerupType, Integer> pUps){
        int numSpeed = 0;
        int numRapidFire = 0;
        int numInvinc = 0;
        int numStarburst = 0;
        int numDamage = 0;

        for(Map.Entry<PowerupType, Integer> pUpInfo : pUps.entrySet()){
            switch (pUpInfo.getKey().getName()){
                case "Speed":
                    pref.putInteger("Speed", pUpInfo.getValue());
                    break;
                case "Rapid Fire":
                    pref.putInteger("Rapid Fire", pUpInfo.getValue());
                    break;
                case "Invincibility":
                    pref.putInteger("Invincibility", pUpInfo.getValue());
                    break;
                case "Burst Fire":
                    pref.putInteger("Burst Fire", pUpInfo.getValue());
                    break;
                case "Damage Buff":
                    pref.putInteger("Damage Buff", pUpInfo.getValue());
                    break;
            }
        }
    }


    public static <T> Map<String, T> listToMap(List<T> xs){
        Map<String, T> map = new HashMap<>();
        int i = 0;
        for(T obj : xs){
            map.put(Integer.toString(i), obj);
        }
        return map;
    }

    public static <T> List<T> mapToList(Map<String, T> map){
        List<T> xs = new LinkedList<>();
        xs.addAll(map.values());
        return xs;
    }


    /**
     * Save the locations of mines to a file.
     * @param pref A libgdx preferences object
     * @param gc Reference to the GameController instance of this game
     */
    public static void saveMines(Preferences pref, GameController gc){
        Map m = new HashMap<String, String>();
        int i = 0;
        for(PhysicsObject o : gc.physicsObjects){
            if(o instanceof Mine){
                m.put(Integer.toString(i),o.position.toString());
                i = i + 1;
            }
        }
        pref.put(m);
    }

    /**
     * Load mines from save file. Recreates mines by loading positions and instantiating new mines at this position.
     * @param mines A map of strings of from String -> String, where the value String is of form "(x,y)", where
     *              x is the x-position of the mine and y is the y-position of the mine.
     * @param gc Reference to the GameController instance of this game
     */
    public static void loadMines(HashMap<String, String> mines, GameController gc){
        for(String minePosAsString : mines.values()){
            Mine mine = new Mine(gc, new Vector2().fromString(minePosAsString));
            gc.physicsObjects.add(mine);
        }
    }


    /**
     * Save the game state to the given file name. In reality multiple files are created to store different aspects
     * of the game, but all files are prefixed by saveFileName. Saving, loading and storage of files is handled by
     * libgdx's preference system. See https://libgdx.com/wiki/preferences for more details.
     * @param saveFileName string referring to the fileName
     * @param gc Reference to the GameController instance of this game
     */
    public static void save(String saveFileName, GameController gc){
        /* Append 'shardsoftware' to the fileName so as not to interfere with other games that use ligbdx preferences. */
        Preferences pref = Gdx.app.getPreferences("shardsoftware_" + saveFileName);
        Preferences minePref = Gdx.app.getPreferences("shardsoftware_" + saveFileName + "_pObjs");

        savePowerups(pref, gc.playerBoat.collectedPowerups);
        saveMines(minePref, gc);
        pref.flush();
    }


    /**
     * Load the game state for the given file name. In reality multiple files are created to store different aspects
     * of the game, but all files are prefixed by saveFileName. Saving, loading and storage of files is handled by
     * libgdx's preference system. See https://libgdx.com/wiki/preferences for more details.
     * @param saveFileName string referring to the fileName
     * @param gc Reference to the GameController instance of this game
     */
    public static void load(String saveFileName, GameController gc){
        Preferences pref = Gdx.app.getPreferences("shardsoftware_"+saveFileName);
        Preferences minePref = Gdx.app.getPreferences("shardsoftware_" + saveFileName + "_pObjs");
        gc.playerBoat.loadPowerups(pref);
        loadMines((HashMap<String, String>) minePref.get(), gc);
    }

}
