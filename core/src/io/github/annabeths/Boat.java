package io.github.annabeths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Boat implements PhysicsObject {
	int id;
    
	// GameObject stats
	float x;
	float y;
	float rotation;
	Sprite sprite = null;
	
	// Boat stats
	protected int HP;
	protected float speed;
	protected float turnSpeed;
	
    public Boat() {
        sprite = new Sprite(new Texture(Gdx.files.internal("mario/yanderedev.jpg")));
        sprite.setSize(50, 50);
        sprite.setOrigin(25, 25);
        
        sprite.setCenter(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
    }
	
	public abstract void Update(float delta);
	
	void Move(float delta) {
		x += Math.sin(Math.toRadians(rotation)) * speed * delta;
		y += Math.cos(Math.toRadians(rotation)) * speed * delta;
		
		sprite.setPosition(x, y);
	}
	
	// Turn the boat, a multiplier of 1 will turn it clockwise, -1 anti-clockwise
	void Turn(float delta, float multiplier) {
		rotation += turnSpeed * delta * multiplier;
		sprite.setRotation(-rotation);
	}
	
	void Shoot() {
		
	}
	
	boolean isColliding(PhysicsObject object) {
		return false;
	}
	
	void Destroy() {
		
	}
	
	void SetPosition(float x, float y) {
		this.x = x;
		this.y = y;
		sprite.setPosition(x, y);
	}
}
