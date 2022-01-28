package io.github.annabeths.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import io.github.annabeths.eng1game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setResizable(false);
		config.setTitle("Team Mario's Pirate Game");
		config.setWindowedMode(1280, 720);
		new Lwjgl3Application(new eng1game(), config);
	}
}
