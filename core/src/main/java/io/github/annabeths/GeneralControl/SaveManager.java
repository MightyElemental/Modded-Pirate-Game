package io.github.annabeths.GeneralControl;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import io.github.annabeths.Boats.*;
import io.github.annabeths.Collectables.PowerupType;
import io.github.annabeths.Colleges.College;
import io.github.annabeths.Colleges.EnemyCollege;
import io.github.annabeths.Colleges.PlayerCollege;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Obstacles.Kraken;
import io.github.annabeths.Obstacles.Mine;
import io.github.annabeths.Obstacles.Weather;
import io.github.annabeths.Projectiles.ProjectileData;

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



    /**
     * Save the locations of mines to a file.
     * @param pref A libgdx preferences object
     * @param gc Reference to the GameController instance of this game
     */
    public static void saveMines(Preferences pref, GameController gc){
        pref.clear(); // clear out any old references to entities that no longer exist
        Map<String, String> m = new HashMap<>();
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
     * Load mines from save file. Recreate mines by loading positions and instantiating new mines at this position.
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
     * Save colleges to save file.
     * @param pref A libgdx preferences object
     * @param gc Reference to the GameController instance of this game
     */
    public static void saveColleges(Preferences pref, GameController gc){
        pref.clear(); // clear out any old references to entities that no longer exist
        List<College> colleges = gc.colleges;
        Map<String, String> m = new HashMap<>();
        int i = 0;
        for(College c : colleges){
            String data = c.position.toString() + ":" + c.aliveTextureFile + ":";
            if(c instanceof PlayerCollege){
                data = data + "player";
            }else{
                String projectileDataName = "STOCK";
                if(((EnemyCollege) c).getProjectileType() == ProjectileData.BOSS){
                    projectileDataName = "BOSS";
                }
                data = data + "enemy:" + c.getHealth() + ':' + c.getMaxHealth() + ':' + projectileDataName;
            }
            m.put(Integer.toString(i),data);
            i = i + 1;
        }
        pref.put(m);
    }

    public static void loadColleges(HashMap<String, String> cols, GameController gc){
        for(String s : cols.values()){
            String[] collegeData = s.split(":");
            Vector2 cPos = new Vector2().fromString(collegeData[0]);
            String texture = collegeData[1];

            College c;
            if(collegeData[2].equals("player")){
                c = new PlayerCollege(cPos, texture,
                        "img/world/island.png", gc, false);
            }else{
                float health = Float.parseFloat(collegeData[3]);
                float maxHealth = Float.parseFloat(collegeData[4]);

                ProjectileData pd = collegeData[5].equals("BOSS")? ProjectileData.BOSS : ProjectileData.STOCK;

                c = new EnemyCollege(cPos, texture,
                        "img/world/island.png", gc, pd, maxHealth);
                c.setHealth(health);
                if(collegeData[5].equals("BOSS")){
                    gc.bossCollege = (EnemyCollege) c;
                }
            }
            gc.physicsObjects.add(c);
            gc.colleges.add(c);
        }
    }

    public static void savePlayerBoat(Preferences pref, PlayerBoat playerBoat){
        String boatInfo = playerBoat.position.toString() + ":" + playerBoat.getHealth() + ":" +
                playerBoat.getMaxHealth() + ":" + playerBoat.getDefense() + ":" + playerBoat.getSpeed() + ":" +
                playerBoat.getTurnSpeed() + ":" + playerBoat.getProjDmgMul() + ":" + playerBoat.getProjSpdMul();
        pref.putString("playerBoat",boatInfo);
    }

    public static void loadPlayerBoat(String s, GameController gc){
        String[] boatInfo = s.split(":");
        Vector2 pos = new Vector2().fromString(boatInfo[0]);
        gc.playerBoat.setHealth(Float.parseFloat(boatInfo[1]));
        gc.playerBoat.setMaxHealth(Float.parseFloat(boatInfo[2]));
        gc.playerBoat.setDefense(Integer.parseInt(boatInfo[3]));
        gc.playerBoat.setSpeed(Float.parseFloat(boatInfo[4]));
        gc.playerBoat.setTurnSpeed(Float.parseFloat(boatInfo[5]));
        gc.playerBoat.setProjDmgMul(Float.parseFloat(boatInfo[6]));
        gc.playerBoat.setProjSpdMul(Float.parseFloat(boatInfo[7]));
        gc.playerBoat.setCenter(pos);
    }

    public static void saveEntities(Preferences pref, List<PhysicsObject> physicsObjects){
        pref.clear(); // clear out any old references to entities that no longer exist
        Map<String, String> m = new HashMap<>();
        int i = 0;
        for(PhysicsObject o : physicsObjects) {
            if (o instanceof NeutralBoat) {
                String entityInfo = o.position.toString() + ":" + "NeutralBoat:" + ((NeutralBoat) o).getHealth() + ":" +
                        ((NeutralBoat) o).getMaxHealth();
                m.put(Integer.toString(i), entityInfo);
                i = i + 1;
            } else if (o instanceof FriendlyBoat) {
                String entityInfo = o.position.toString() + ":" + "FriendlyBoat:" + ((FriendlyBoat) o).getHealth() + ":" +
                        ((FriendlyBoat) o).getMaxHealth();
                m.put(Integer.toString(i), entityInfo);
                i = i + 1;
            }else if (o instanceof EnemyBoat){
                String entityInfo = o.position.toString() + ":" + "EnemyBoat:" + ((EnemyBoat) o).getHealth() + ":" +
                        ((EnemyBoat) o).getMaxHealth();
                m.put(Integer.toString(i), entityInfo);
                i = i + 1;
            }else if(o instanceof Kraken){
                String entityInfo = o.position.toString() + ":" + "Kraken:" + ((Kraken) o).getHealth() + ":" +
                        ((Kraken) o).getMaxHealth();
                m.put(Integer.toString(i), entityInfo);
                i = i + 1;
            }else if(o instanceof Weather){
                String entityInfo = o.position.toString() + ":" + "Weather:" + ((Weather) o).directionTrend;
                m.put(Integer.toString(i), entityInfo);
                i = i + 1;
            }
        }
        pref.put(m);
    }

    private static void loadEntities(HashMap<String, String> entities, GameController gc) {
        for(String s : entities.values()){
            String[] entityData = s.split(":");
            Vector2 ePos = new Vector2().fromString(entityData[0]);
            switch (entityData[1]){
                case "NeutralBoat":
                    NeutralBoat nBoat = new NeutralBoat(gc, ePos);
                    nBoat.setHealth(Float.parseFloat(entityData[2]));
                    nBoat.setMaxHealth(Float.parseFloat(entityData[3]));
                    gc.physicsObjects.add(nBoat);
                    break;
                case "EnemyBoat":
                    EnemyBoat eBoat = new EnemyBoat(gc, ePos);
                    eBoat.setHealth(Float.parseFloat(entityData[2]));
                    eBoat.setMaxHealth(Float.parseFloat(entityData[3]));
                    gc.physicsObjects.add(eBoat);
                    break;
                case "FriendlyBoat":
                    FriendlyBoat fBoat = new FriendlyBoat(gc, ePos);
                    fBoat.setHealth(Float.parseFloat(entityData[2]));
                    fBoat.setMaxHealth(Float.parseFloat(entityData[3]));
                    gc.physicsObjects.add(fBoat);
                    break;
                case "Kraken":
                    Kraken kraken = new Kraken(gc, ePos);
                    kraken.setHealth(Float.parseFloat(entityData[2]));
                    kraken.setMaxHealth(Float.parseFloat(entityData[3]));
                    gc.physicsObjects.add(kraken);
                    break;
                case "Weather":
                    Weather weather = new Weather(gc, ePos, Integer.parseInt(entityData[2]));
                    gc.physicsObjects.add(weather);
                    break;
            }
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
        Preferences collegePref = Gdx.app.getPreferences("shardsoftware_" + saveFileName + "_cols");
        Preferences entityPref = Gdx.app.getPreferences("shardsoftware_" + saveFileName + "_entities");

        pref.putFloat("xp", gc.getXp());
        pref.putInteger("plunder", gc.getPlunder());

        Difficulty dif = gc.getGameDifficulty();
        if(dif == Difficulty.EASY){
            pref.putString("difficulty", "easy");
        }else if(dif == Difficulty.MEDIUM){
            pref.putString("difficulty", "medium");
        }else{
            pref.putString("difficulty", "hard");
        }

        savePlayerBoat(pref,gc.playerBoat);
        saveColleges(collegePref, gc);
        savePowerups(pref, gc.playerBoat.collectedPowerups);
        saveMines(minePref, gc);
       saveEntities(entityPref, gc.physicsObjects);
        pref.flush();
        minePref.flush();
        collegePref.flush();
        entityPref.flush();
    }


    /**
     * Preferences.get() returns Hashmap<String, ?> so we need to cast it safely to Hashmap<String,String>, this
     * method does so.
     * @param pref a libgdx Preferences object
     * @return Hashmap<String,String> contained in the Preferences object.
     */
    public static HashMap<String, String> loadPref(Preferences pref){
        Map<String, ?> m1 = pref.get();
        HashMap<String, String> m2 = new HashMap<>();
        for(Map.Entry<String, ?> entry : m1.entrySet()){
            String key = entry.getKey();
            String value = (String) entry.getValue();
            m2.put(key, value);
        }
        return m2;
    }

    /**
     * Load the game state for the given file name. In reality multiple files are created to store different aspects
     * of the game, but all files are prefixed by saveFileName. Saving, loading and storage of files is handled by
     * libgdx's preference system. See https://libgdx.com/wiki/preferences for more details.
     * @param saveFileName string referring to the fileName
     */
    public static void load(String saveFileName, GameController gc){
        Preferences pref = Gdx.app.getPreferences("shardsoftware_" + saveFileName);
        Preferences minePref = Gdx.app.getPreferences("shardsoftware_" + saveFileName + "_pObjs");
        Preferences collegePref = Gdx.app.getPreferences("shardsoftware_" + saveFileName + "_cols");
        Preferences entityPref = Gdx.app.getPreferences("shardsoftware_" + saveFileName + "_entities");


        gc.setXp(pref.getFloat("xp"));
        gc.setPlunder(pref.getInteger("plunder"));



        switch (pref.getString("difficulty")){
            case "easy":
                gc.setDifficulty(Difficulty.EASY);
                break;
            case "medium":
                gc.setDifficulty(Difficulty.MEDIUM);
                break;
            case "hard":
                gc.setDifficulty(Difficulty.HARD);
        }

        gc.playerBoat.loadPowerups(pref);
        loadPlayerBoat(pref.getString("playerBoat"), gc);
        loadMines(loadPref(minePref), gc);
        loadColleges(loadPref(collegePref), gc);
        loadEntities(loadPref(entityPref), gc);
    }



    /**
     * Returns whether a given save file exists for the saveFileName, or not.
     * @param saveFileName string referring to the fileName
     * @return boolean, true if exists, false otherwise
     */
    public static boolean doesSaveFileExist(String saveFileName){
        Preferences pref = Gdx.app.getPreferences("shardsoftware_"+saveFileName);
        return !pref.get().isEmpty();
    }

}
