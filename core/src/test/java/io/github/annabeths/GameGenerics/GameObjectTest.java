package io.github.annabeths.GameGenerics;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class GameObjectTest {

	@Test
	public void testCenterNullSprite() {
		GameObject go = new GameObject() {
		};
		assertDoesNotThrow(() -> go.getLocalCenterX());
		assertDoesNotThrow(() -> go.getLocalCenterY());
	}

	@Test
	public void testCenterNullPosition() {
		GameObject go = new GameObject() {
		};
		assertDoesNotThrow(() -> go.getX());
		assertDoesNotThrow(() -> go.getY());
	}

	@Test
	public void testCenter() {
		GameObject go = new GameObject() {
		};
		go.position = Vector2.Zero;
		go.sprite = new Sprite();
		go.sprite.setBounds(0, 0, 100, 100);
		assertEquals(go.getCenter(), new Vector2(50, 50));
	}

	@Test
	public void testCenterOffset() {
		GameObject go = new GameObject() {
		};
		go.position = new Vector2(100, 100);
		go.sprite = new Sprite();
		go.sprite.setBounds(100, 100, 100, 100);
		assertEquals(go.getCenter(), new Vector2(150, 150));
	}

	@Test
	public void testSetCenter() {
		GameObject go = new GameObject() {
		};
		go.sprite = new Sprite();
		go.sprite.setSize(50, 50);

		go.setCenter(new Vector2(0, 0));
		assertEquals(0, go.getCenterX());
		assertEquals(0, go.getCenterY());
		assertEquals(-25, go.getX());
		assertEquals(-25, go.getY());

		go.setCenter(new Vector2(100, 50));
		assertEquals(100, go.getCenterX());
		assertEquals(50, go.getCenterY());
		assertEquals(75, go.getX());
		assertEquals(25, go.getY());
	}

	@Test
	public void testSetSprite() {
		GameObject go = new GameObject() {
		};
		Vector2 size = new Vector2(100, 100);
		Vector2 pos = new Vector2(50, 50);
		go.setSprite("", pos, size);
		assertEquals(size.x, go.sprite.getWidth());
		assertEquals(size.y, go.sprite.getHeight());
		assertEquals(pos.x, go.sprite.getX());
		assertEquals(pos.y, go.sprite.getY());
	}

}
