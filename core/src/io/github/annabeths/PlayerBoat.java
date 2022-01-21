package io.github.annabeths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Projectiles.DefaultProjectile;

public class PlayerBoat extends Boat{
    public PlayerBoat(GameController controller) {
        this.controller = controller;

		this.HP = 100;
		this.maxHP = 100;
		this.speed = 125;
		this.turnSpeed = 150;
	}
	
	@Override
	public void Update(float delta) {
		// TODO Auto-generated method stub
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
			Move(delta, 1);
		}
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
			Move(delta, -1);
		}
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
			Turn(delta, -1);
		}
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
			Turn(delta, 1);
		}

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            Shoot();
        }
	}
	
	@Override
	public boolean CollidesWith(PhysicsObject other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void OnCollision(PhysicsObject other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void Shoot(){
        Projectile proj = new Projectile(new Vector2(GetCenterX() + x, GetCenterY() + y),
        		rotation, 
        		controller.projectileHolder.stock);
        controller.NewPhysicsObject(proj); // Add the projectile to the GameController's physics objects list so it receives updates
	}

	@Override
	void Destroy(){
		controller.gameOver();
	}

    public void Upgrade(Upgrades upgrade, float amount){
    	if(upgrade == Upgrades.health) {
    		HP = (int) Math.max(0, Math.min(maxHP, HP + amount)); // I don't like this formula. Keeps HP from exceeding max
    	} else if(upgrade == Upgrades.maxhealth) {
    		maxHP += amount;
    		HP += amount; // Also heal the player, we're feeling generous.
    	} else if(upgrade == Upgrades.speed) {
    		speed += amount;
    	} else if(upgrade == Upgrades.turnspeed) {
    		turnSpeed += amount;
    	}
    }

	@Override
	public void Draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
}
