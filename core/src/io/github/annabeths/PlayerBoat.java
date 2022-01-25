package io.github.annabeths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

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
		shotDelay -= delta;

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

        if(((Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && !controller.hoveringOverButton) // make sure we don't fire when hovering over a button and clicking
		|| Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) // doesn't matter if we're over a button or not when pressing space
		&& shotDelay <= 0){
            Shoot();
        }

	}
	
	@Override
	public boolean OnCollision(PhysicsObject other) {
		if(other.getClass() == Projectile.class)
		{
			Projectile p = (Projectile) other;
			if(p.isPlayerProjectile)
				return false;
			else
				return true;
		}
		else if(other.getClass() == EnemyCollege.class)
		{
			System.out.println("enemy college hit");
		}
		return false;
	}

	@Override
	void Shoot(){
        Projectile proj = new Projectile(new Vector2(GetCenterX() + position.x, GetCenterY() + position.y),
        								 rotation, controller.projectileHolder.stock, true);
        controller.NewPhysicsObject(proj); // Add the projectile to the GameController's physics objects list so it receives updates
		shotDelay = controller.projectileHolder.stock.shotDelay;
	}

	@Override
	void Destroy(){
		controller.gameOver();
	}

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
    	}
    }

	@Override
	public void Draw(SpriteBatch batch) {
		sprite.draw(batch);
	}

	public void Heal(int amount)
	{
		HP += amount;
		if(HP > maxHP)
		{
			HP = maxHP;
		}
	}
}
