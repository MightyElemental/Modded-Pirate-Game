package io.github.annabeths;

import com.badlogic.gdx.Input;

public class TestBoat extends Boat {

	public TestBoat(GameController controller) {
		this.controller = controller;

		this.HP = 100;
		this.speed = 125;
		this.turnSpeed = 75;
	}
	
	@Override
	public void Update(float delta) {
		// TODO Auto-generated method stub
		super.Move(delta, 1);
		super.Turn(delta, 1);
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
		
	}

	@Override
	void Destroy(){

	}
}
