package io.github.annabeths.GameGenerics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class GameObjectTest {
	
	@Test
	public void testCenter() {
		GameObject go = new GameObject() {};
		go.position = Vector2.Zero;
		go.sprite = new Sprite();
		go.sprite.setBounds(0, 0, 100, 100);
		assertEquals(go.getCenter(), new Vector2(50,50));
	}
	
	@Test
	public void testCenterOffset() {
		GameObject go = new GameObject() {};
		go.position = new Vector2(100,100);
		go.sprite = new Sprite();
		go.sprite.setBounds(100, 100, 100, 100);
		assertEquals(go.getCenter(), new Vector2(150,150));
	}

}
