package io.github.annabeths.Boats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;

public abstract class Boat extends PhysicsObject {
	GameController controller;
    
	// Boat stats
	public int HP;
	public int maxHP;
	protected float speed;
	protected float turnSpeed;

	protected float shotDelay = 0.5f;
	protected float timeSinceLastShot = 0f;

	protected Array<Vector2> mapBounds;
	protected Vector2 mapSize;
	
    public Boat() {
        sprite = new Sprite(new Texture(Gdx.files.internal("img/boat1.png")));
        sprite.setSize(100, 50);
        sprite.setOrigin(50, 25);
        sprite.setCenter(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);

		collisionPolygon = new Polygon(new float[]{0,0,0,68,25,100,50,68,50,0});
		
		position = new Vector2();
    }
	
	public abstract void Update(float delta);
	
	void Move(float delta, int multiplier) {
		// Convention: 0 degrees means the object is pointing right, positive angles are counter clockwise
		Vector2 oldPos = position.cpy();
		position.x += Math.cos(Math.toRadians(rotation)) * speed * delta * multiplier;
		position.y += Math.sin(Math.toRadians(rotation)) * speed * delta * multiplier;
		
		sprite.setPosition(position.x, position.y);
		collisionPolygon.setPosition(position.x + GetCenterX()/2, position.y - GetCenterY()/2 - 10);
		collisionPolygon.setOrigin(25,50);

		if(!Intersector.isPointInPolygon(mapBounds, new Vector2(position.x + GetCenterX(), position.y+GetCenterY())))
		{
			position = oldPos.cpy();
		}
	}
	
	// Turn the boat, a positive multiplier will turn it anti-clockwise, negative clockwise
	void Turn(float delta, float multiplier) {
		rotation = (rotation + turnSpeed * delta * multiplier) % 360;
		sprite.setRotation(rotation);
		collisionPolygon.setRotation(rotation - 90);
	}
	
	abstract void Shoot();
	
	abstract void Destroy();
	
	// Place the boat somewhere in global space, use this when spawning boats
	void SetPosition(float x, float y) {
		position.x = x;
		position.y = y;
		sprite.setPosition(x, y);
	}

	@Override
	public void Draw(SpriteBatch batch)
	{
		sprite.draw(batch);
	}

	public float GetCenterX()
	{
		return sprite.getOriginX();
	}

	public float GetCenterY()
	{
		return sprite.getOriginY();
	}
}
