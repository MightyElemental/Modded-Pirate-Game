package io.github.annabeths.GeneralControl;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Preferences;
import io.github.annabeths.Collectables.PowerupType;
import io.github.annabeths.GameScreens.GameController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hector Woods
 * @since Assessment 2
 */
public abstract class SaveManager {


    public static Preferences savePowerups(Preferences pref, Map<PowerupType, Integer> pUps){
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

        return pref;
    }



    public static void save(String saveFileName, GameController gc){
        /* Append 'shardsoftware' to the fileName so as not to interefere with other games that use ligbdx preferences. */
        Preferences pref = Gdx.app.getPreferences("shardsoftware_" + saveFileName);

        pref = savePowerups(pref, gc.playerBoat.collectedPowerups);
        pref.flush();
    }



    public static void load(String saveFileName, GameController gc){
        Preferences pref = Gdx.app.getPreferences("shardsoftware_"+saveFileName);
        gc.playerBoat.loadPowerups(pref);


    }

}
