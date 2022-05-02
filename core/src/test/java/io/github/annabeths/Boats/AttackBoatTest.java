package io.github.annabeths.Boats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import io.github.annabeths.Colleges.College;
import io.github.annabeths.GameGenerics.PhysicsObject;
import io.github.annabeths.GameScreens.GameController;
import io.github.annabeths.Level.GameMap;
import io.github.annabeths.Projectiles.Projectile;
import io.github.annabeths.Projectiles.ProjectileRay;

public class AttackBoatTest {

	public GameController gc;
	public AttackBoat b;

	@BeforeEach
	public void setup() {
		gc = mock(GameController.class);
		gc.map = mock(GameMap.class);
		gc.colleges = new ArrayList<>();
		gc.physicsObjects = new ArrayList<>();
		gc.rays = new ArrayList<>();
		doCallRealMethod().when(gc).NewPhysicsObject(any(PhysicsObject.class));
		gc.playerBoat = new PlayerBoat(gc, new Vector2(0, 0));

		b = mock(AttackBoat.class, withSettings().defaultAnswer(CALLS_REAL_METHODS)
				.useConstructor(gc, new Vector2(0, 0), ""));
		b.maxHP = 100;
		b.HP = b.maxHP;
	}

	@Test
	public void testShoot() {
		b.Shoot();
		// Shoot 2 projectiles
		assertEquals(2, gc.physicsObjects.size());
		// Ensure projectiles are 180 degrees apart
		Projectile p1 = (Projectile) gc.physicsObjects.get(0);
		Projectile p2 = (Projectile) gc.physicsObjects.get(1);
		assertEquals(180, Math.abs(p1.rotation - p2.rotation));
	}

	@Test
	public void testDestroy() {
		assertFalse(b.removeOnNextTick());
		b.Destroy();
		assertTrue(b.removeOnNextTick());
	}

	@Test
	public void testUpdateDestroy() {
		b.Update(1);
		verify(b, never()).Destroy();

		b.HP = -1;
		b.Update(1);
		verify(b, atLeast(1)).Destroy();
	}

	@Test
	public void testUpdateCallsAIUpdate() {
		b.Update(1);
		verify(b, atLeast(1)).updateAIState();
	}

	@Test
	public void testUpdateSwitchAIStates() {
		doNothing().when(b).updateAIState();
		doNothing().when(b).idle();
		doNothing().when(b).attack(anyFloat());
		doNothing().when(b).approach(anyFloat());

		b.state = AIBoat.AIState.IDLE;
		b.Update(1);
		verify(b, atLeast(1)).idle();

		b.state = AIBoat.AIState.ATTACK;
		b.Update(1);
		verify(b, atLeast(1)).attack(anyFloat());

		b.state = AIBoat.AIState.APPROACH;
		b.Update(1);
		verify(b, atLeast(1)).approach(anyFloat());
	}

	@Test
	public void testUpdateHasDest() {
		doNothing().when(b).MoveToDestination(anyFloat());
		// Destination will be set by the approach state
		b.destination = null;
		b.Update(1);
		verify(b, times(1)).MoveToDestination(anyFloat());

	}

	@Test
	public void testUpdateHasNoDest() {
		doNothing().when(b).MoveToDestination(anyFloat());
		// Destination is set so it should move
		b.destination = mock(Vector2.class);
		b.Update(1);
		verify(b, times(1)).MoveToDestination(anyFloat());
	}

	@Test
	public void testApproach() {
		b.target = mock(Boat.class);
		when(b.target.getCenter()).thenReturn(new Vector2(0, 0));

		b.destination = null;
		when(b.isDestValid(b.target.getCenter())).thenReturn(true);
		b.approach(1);
		assertNotNull(b.destination);

		b.destination = null;
		when(b.isDestValid(b.target.getCenter())).thenReturn(false);
		b.approach(1);
		verify(b, atLeast(1)).idle();
	}

	@Test
	public void testUpdateAIStateFoundTarget() {
		Boat boat = mock(Boat.class);
		when(b.getNearestTarget()).thenReturn(boat);
		when(boat.getCenter()).thenReturn(mock(Vector2.class));

		when(boat.getCenter().dst(any())).thenReturn(b.attackRange - 1);
		b.updateAIState();
		assertEquals(AIBoat.AIState.ATTACK, b.state);

		when(boat.getCenter().dst(any())).thenReturn(b.approachRange - 1);
		b.updateAIState();
		assertEquals(AIBoat.AIState.APPROACH, b.state);

		when(boat.getCenter().dst(any())).thenReturn(b.approachRange + 1);
		b.updateAIState();
		assertEquals(AIBoat.AIState.IDLE, b.state);
	}

	@Test
	public void testUpdateAIStateNotFoundTarget() {
		when(b.getNearestTarget()).thenReturn(null);
		b.updateAIState();
		assertEquals(AIBoat.AIState.IDLE, b.state);
	}

	@Test
	public void testAttackNoShoot() {
		b.target = mock(Boat.class);
		when(b.target.getCenter()).thenReturn(new Vector2(0, 0));
		b.attack(1);
		verify(b, never()).Shoot();
	}

	@Test
	public void testAttackShoot() {
		b.target = mock(Boat.class);
		b.timeSinceLastShot = b.shotDelay + 1;
		b.rotation = 120;
		when(b.target.getCenter()).thenReturn(new Vector2(0, 0));
		doNothing().when(b).moveTowardsDesiredAngle(anyFloat(), anyFloat());
		b.attack(1);
		verify(b, atLeast(1)).Shoot();
	}

	@Test
	public void testAttackShootWrongAngle() {
		b.target = mock(Boat.class);
		b.timeSinceLastShot = b.shotDelay + 1;
		b.rotation = 200;
		when(b.target.getCenter()).thenReturn(new Vector2(0, 0));
		doNothing().when(b).moveTowardsDesiredAngle(anyFloat(), anyFloat());
		b.attack(1);
		// boat should not shoot if it is at the wrong angle
		verify(b, never()).Shoot();

		b.timeSinceLastShot = b.shotDelay + 1;
		b.rotation = 230;
		when(b.target.getCenter()).thenReturn(new Vector2(0, 0));
		doNothing().when(b).moveTowardsDesiredAngle(anyFloat(), anyFloat());
		b.attack(1);
		// boat should not shoot if it is at the wrong angle
		verify(b, never()).Shoot();
	}

}
