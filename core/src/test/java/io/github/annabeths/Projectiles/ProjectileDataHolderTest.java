package io.github.annabeths.Projectiles;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

public class ProjectileDataHolderTest {

	/** Ensure all projectile types are assigned values */
	@Test
	public void ensureNoNullTypes() {
		Field[] fields = ProjectileDataHolder.class.getDeclaredFields();
		for (Field f : fields) {
			// Ensure we're only selecting projectiles
			if (f.getType() == Projectile.class) {
				try {
					assertNotNull(f.get(null));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
