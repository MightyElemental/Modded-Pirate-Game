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
		this.speed = 125;
		this.turnSpeed = 150;
	}
	
	@Override
	public void Update(float delta) {
		// TODO Auto-generated method stub
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
			super.Move(delta, 1);
		}
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
			super.Move(delta, -1);
		}
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
			super.Turn(delta, -1);
		}
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
			super.Turn(delta, 1);
		}

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
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
        Projectile proj = new Projectile(new Vector2(super.GetCenterX() + super.x, super.GetCenterY() + super.y),
        		super.rotation, 
        		controller.projectileHolder.stock);
        controller.NewPhysicsObject(proj); // Add the projectile to the GameController's physics objects list so it receives updates
	}

	@Override
	void Destroy(){
		controller.gameOver();
	}

    public void Upgrade(Upgrades upgrade, float amount){

    }

	@Override
	public void Draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}
}
