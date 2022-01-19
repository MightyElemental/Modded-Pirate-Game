package io.github.annabeths;

public class TestBoat extends Boat {

	public TestBoat() {
		this.HP = 100;
		this.speed = 25;
		this.turnSpeed = 25;
	}
	
	@Override
	public void Update(float delta) {
		// TODO Auto-generated method stub
		super.Move(delta);
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
}
