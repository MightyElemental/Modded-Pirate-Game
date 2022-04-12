package io.github.annabeths.Obstacles;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import io.github.annabeths.Boats.Boat;
import io.github.annabeths.Boats.PlayerBoat;
import io.github.annabeths.GameGenerics.IHealth;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Level.GameMap;
import io.github.annabeths.Projectiles.Projectile;
import io.github.annabeths.Projectiles.ProjectileData;

import java.util.Random;

/**
 * Weather. The player gets more points for sailing through it, but they risk taking damage from lightning
 * @author Hector Woods
 */
public class Weather extends ObstacleEntity {
    final float timeBetweenDirectionChanges = 0.5f;
    final float speed = 75;
    String frame1 = "img/entity/weather1.png";
    String frame2 = "img/entity/weather2.png";

    Vector2 direction;
    float timeOnCurrentDirection = 0;
    int directionTrend; //The weather starts on one side of the map and "trends" to the other side.
                        // 0 means it starts North, trends south, 1 means south-north, 2 means east-west, 3 means west-east


    Boolean damageActive = false;
    double averageTimeBetweenLightningStrikes = 10;
    double timeUntilNextLightningStrike;
    float timeSinceLastStrike = 0;
    float timeStrikeActive = 0.75f;
    float timeSinceStrikeStarted = 0;

    public Weather(GameController controller, Vector2 position, int dir) {
        super(controller, position, "img/entity/weather1.png",  new Vector2(100, 100));
        Polygon poly = new Polygon(new float[] { 0, 50, 50, 100, 100, 50, 50, 0 });
        poly.setPosition(position.x - getLocalCenterX(),
                position.y);
        poly.setOrigin(0, 0);
        poly.setRotation(rotation - 90);
        setCenter(position);
        directionTrend = dir;
        this.collisionPolygon = poly;
        ChangeDirection();
        timeUntilNextLightningStrike = (averageTimeBetweenLightningStrikes * Math.random());
    }

    public void toggleLightning(){
        if(damageActive){
            setSprite(frame1, position, new Vector2(100, 100));
            timeSinceStrikeStarted = 0;
        }else{
            setSprite(frame2, position, new Vector2(100, 100));
            timeUntilNextLightningStrike = (averageTimeBetweenLightningStrikes * Math.random());
            timeSinceLastStrike = 0;
        }
        damageActive = !damageActive;
    }


    @Override
    public void OnCollision(PhysicsObject other) {
        if(!damageActive){
            return; // Only deal damage when lightning frames are showing
        }
        if(other instanceof Boat){
            ((Boat) other).damage(0.1f);
        }
    }


    public void ChangeDirection(){
        switch (directionTrend){
            case 0: // North -> South
                direction = new Vector2(Math.random() < 0.5 ? -1 : 1, -1);
                break;
            case 1: // South -> North
                direction = new Vector2(Math.random() < 0.5 ? -1 : 1, 1);
                break;
            case 2: // East -> West
                direction = new Vector2(1, Math.random() < 0.5 ? -1 : 1);
                break;
            case 3: // West -> East
                direction = new Vector2(-1, Math.random() < 0.5 ? -1 : 1);
        }
    }

    public void Move(float delta){
        timeOnCurrentDirection = timeOnCurrentDirection + delta;
        if(timeOnCurrentDirection >= timeBetweenDirectionChanges){
            timeOnCurrentDirection = 0;
            ChangeDirection();
        }


        position = new Vector2(position.x + (direction.x * delta * speed), position.y + (direction.y * delta * speed));

        sprite.setPosition(position.x-50, position.y-70);
        collisionPolygon.setPosition(position.x - getLocalCenterX(),
                position.y);
        collisionPolygon.setRotation(rotation - 90);
        if(position.x < -650 || position.x > GameMap.getMapWidth() + 650 || position.y < -650 || position.y > GameMap.getMapHeight() + 650){
            kill();
        }

        // Update the sprite and check state of lightning
        if(damageActive){
            timeSinceStrikeStarted = timeSinceStrikeStarted + delta;
            if(timeSinceStrikeStarted >= timeStrikeActive){
                toggleLightning();
            }
        }else{
            timeSinceLastStrike = timeSinceLastStrike + delta;
            if(timeSinceLastStrike >= timeUntilNextLightningStrike){
                toggleLightning();
            }
        }
    }

    @Override
    public void Update(float delta) {
        Move(delta);
    }
    @Override
    public void Destroy() {

    }
}
