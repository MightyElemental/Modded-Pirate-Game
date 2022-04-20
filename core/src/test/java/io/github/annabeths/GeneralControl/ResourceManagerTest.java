package io.github.annabeths.GeneralControl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.assets.AssetManager;

public class ResourceManagerTest {

	@BeforeEach
	public void setup() {
		TestHelper.setupEnv();
	}

	@Test
	public void testInit() {
		assertDoesNotThrow(() -> ResourceManager.init(mock(AssetManager.class)));
	}

}
