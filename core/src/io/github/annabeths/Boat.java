package io.github.annabeths;

public abstract class Boat implements PhysicsObject {
	int id;
	// Boat stats
	int HP;
	float speed;
	float turnSpeed;
	
	void Move() {
		
	}
	
	void Turn() {
		
	}
	
	void Shoot() {
		
	}
	
	boolean isColliding(PhysicsObject object) {
		return false;
	}
}
