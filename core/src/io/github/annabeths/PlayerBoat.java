package io.github.annabeths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class PlayerBoat extends Boat{
    public PlayerBoat(GameController controller) {
        this.controller = controller;

		this.HP = 100;
		this.speed = 125;
		this.turnSpeed = 75;
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
	public void Draw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	void Shoot(){
        sprite.setSize(sprite.getWidth() + 25, sprite.getHeight() + 25);
	}

	@Override
	void Destroy(){
		controller.gameOver();
	}

    public void Upgrade(Upgrades upgrade, float amount){

    }
}
