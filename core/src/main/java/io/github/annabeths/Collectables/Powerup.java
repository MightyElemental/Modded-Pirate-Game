package io.github.annabeths.Collectables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.GameGenerics.PhysicsObject;

public class Powerup extends PhysicsObject {
	
	//position
	//sprite while on screen
	//id of powerup given

	
	public Powerup(int Power) {
		position = new Vector2(300, 600);
		int PowerID = Power;
		sprite = new Sprite(new Texture(Gdx.files.internal("img/powerup.png")));
		sprite.setPosition(300, 600);
		sprite.setSize(50, 50);
		collisionPolygon = new Polygon(new float[] { 8, 0, 16, 8, 8, 16, 0, 8 });
		collisionPolygon.setOrigin(8, 8);
		collisionPolygon.setPosition(300, 600);
	}
	
	@Override
	public void OnCollision(PhysicsObject other) {
		if (other instanceof PlayerBoat) {
			//give powerup based on PowerID
			//then disappear
			System.out.println("Collected a powerup");
			killOnNextTick = true;
		}
	}
	
	@Override
	public void Draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
	
}
