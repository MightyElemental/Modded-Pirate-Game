package io.github.annabeths.UI;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class HUDTest {

//	public static GameController gc;
//	public static HUD hud;
//	public static SpriteBatch sb;
//
//	@BeforeAll
//	public static void init() {
//		Gdx.graphics = mock(Graphics.class);
//		ResourceManager.font = mock(BitmapFont.class);
//		gc = mock(GameController.class);
//		gc.map = mock(GameMap.class);
//		gc.map.camera = new OrthographicCamera();
//		gc.colleges = new ArrayList<College>();
//
//		sb = mock(SpriteBatch.class);
//		hud = mock(HUD.class);
//		doCallRealMethod().when(hud).Draw(sb);
//		hud.hpTextLayout = mock(GlyphLayout.class);
//	}

	@Test
	public void testTimeStringFormatting() {
		HashMap<Integer, String> cases = new HashMap<>();
		cases.put(0, "00 seconds");
		cases.put(17, "17 seconds");
		cases.put(60, "1'00\"");
		cases.put(600, "10'00\"");
		cases.put(659, "10'59\"");

		for (HashMap.Entry<Integer, String> entry : cases.entrySet()) {
			Integer key = entry.getKey();
			String val = entry.getValue();

			assertEquals(val, HUD.generateTimeString(key));
		}
	}

//	@Test
//	public void testDraw() {
//		assertDoesNotThrow(() -> hud.Draw(sb));
//	}

}
