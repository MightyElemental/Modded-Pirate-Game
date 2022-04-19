package io.github.annabeths.Level;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import io.github.annabeths.GeneralControl.TestHelper;

public class WaterBackgroundTest {

	WaterBackground wb;

	@BeforeEach
	public void setup() {
		TestHelper.setupEnv();
		wb = mock(WaterBackground.class,
				withSettings().useConstructor(100, 100).defaultAnswer(CALLS_REAL_METHODS));
	}

	@Test
	public void testConstructor() {
		assertDoesNotThrow(() -> new WaterBackground(100, 100));
	}

	@Test
	public void testUpdate() {
		assertEquals(0, wb.waterTextureNumber);
		wb.Update(1);
		assertEquals(1, wb.waterTextureNumber);
		wb.Update(1);
		assertEquals(2, wb.waterTextureNumber);
		wb.Update(1);
		assertEquals(0, wb.waterTextureNumber);
		wb.Update(0.1f);
		assertEquals(0, wb.waterTextureNumber);
	}

	@Test
	public void testDraw() {
		wb.grassTextureRegionDrawable = mock(TextureRegionDrawable.class);
		wb.waterTextureRegionDrawable = mock(TextureRegionDrawable.class);
		wb.Draw(mock(SpriteBatch.class));
		verify(wb.grassTextureRegionDrawable, atLeastOnce()).draw(any(SpriteBatch.class),
				anyFloat(), anyFloat(), anyFloat(), anyFloat());
		verify(wb.waterTextureRegionDrawable, atLeastOnce()).draw(any(SpriteBatch.class),
				anyFloat(), anyFloat(), anyFloat(), anyFloat());
	}

}
