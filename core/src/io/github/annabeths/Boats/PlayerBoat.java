package io.github.annabeths.Boats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.annabeths.Colleges.EnemyCollege;
import io.github.annabeths.Colleges.PlayerCollege;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameGenerics.Upgrades;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Projectiles.Projectile;

public class PlayerBoat extends Boat{
	float projectileDamageMultiplier = 1;
	float projectileSpeedMultiplier = 1;

	// The higher the defense, the stronger the player, this is subtracted from the damage
	int defense = 1;

	float timeSinceLastHeal = 0;

    public PlayerBoat(GameController controller, Vector2 initialPosition, Vector2 mapSize) {
        this.controller = controller;

		this.HP = 100;
		this.maxHP = 100;
		this.speed = 200;
		this.turnSpeed = 150;
		position = initialPosition;

		collisionPolygon.setPosition(initialPosition.x + GetCenterX()/2, initialPosition.y - GetCenterY()/2 - 10);
		collisionPolygon.setOrigin(25,50);
		collisionPolygon.setRotation(rotation - 90);

		sprite.setPosition(initialPosition.x, initialPosition.y);

		this.mapSize = mapSize;
		mapBounds = new Array<Vector2>(true, 4);
		mapBounds.add(new Vector2(0,0));
		mapBounds.add(new Vector2(mapSize.x, 0));
		mapBounds.add(new Vector2(mapSize.x, mapSize.y));
		mapBounds.add(new Vector2(0, mapSize.y));
	}
	
	@Override
	public void Update(float delta) {
		timeSinceLastShot += delta;

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

        if(((Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && !controller.hud.hoveringOverButton) // make sure we don't fire when hovering over a button and clicking
		|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) // doesn't matter if we're over a button or not when pressing space
		&& shotDelay <= timeSinceLastShot){
            Shoot();
			timeSinceLastShot = 0;
        }

		if(HP <= 0)
		{
			//the player is dead
			controller.gameOver();
		}

	}
	
	/*
		Method that executes when a collision is detected

		@param	other	the other object, as a PhysicsObject to be generic
	*/
	@Override
	public void OnCollision(PhysicsObject other) {
		if(other instanceof Projectile){ //check the type of object passed
			Projectile p = (Projectile) other;
			if(! p.isPlayerProjectile)
			{
				p.killOnNextTick = true;
				HP -= p.damage;
			}
		}
		else if(other.getClass() == EnemyCollege.class || other.getClass() == PlayerCollege.class)
		{
			controller.gameOver();
		}
		else
		{
			System.out.println("playerboat hit something else!");
		}
	}

	@Override
	void Shoot(){
        Projectile proj = new Projectile(new Vector2(GetCenterX() + position.x, GetCenterY() + position.y),
        								 rotation, controller.projectileHolder.stock, true,
										 projectileDamageMultiplier, projectileSpeedMultiplier);
        controller.NewPhysicsObject(proj); // Add the projectile to the GameController's physics objects list so it receives updates
	}

	@Override
	void Destroy(){
		controller.gameOver();
	}

	/*
		Allows the player to upgrade their boat

		@param	upgrade		The requested upgrade
		@param	amount		the amount to upgrade by
	*/
    public void Upgrade(Upgrades upgrade, float amount){
    	if(upgrade == Upgrades.health) {
    		HP = (int) Math.min(maxHP, HP + amount); // Keeps HP from exceeding max
    	} else if(upgrade == Upgrades.maxhealth) {
    		maxHP += amount;
    		HP += amount; // Also heal the player, we're feeling generous.
    	} else if(upgrade == Upgrades.speed) {
    		speed += amount;
    	} else if(upgrade == Upgrades.turnspeed) {
    		turnSpeed += amount;
    	} else if(upgrade == Upgrades.projectiledamage) {
    		projectileDamageMultiplier += amount;
    	} else if(upgrade == Upgrades.projectilespeed) {
    		projectileSpeedMultiplier += amount;
    	} else if(upgrade == Upgrades.defense) {
    		defense += amount;
    	}
    }

	@Override
	public void Draw(SpriteBatch batch) {
		sprite.draw(batch);
	}

	public void Heal(int amount, float delta)
	{
		timeSinceLastHeal += delta;
		if(amount * timeSinceLastHeal >= 1)
		{
			HP += amount * timeSinceLastHeal;
			timeSinceLastHeal = 0;
			if(HP > maxHP)
			{
				HP = maxHP;
			}
		}
	}
}
