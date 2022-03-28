package io.github.annabeths.UI;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class HUDTest {

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

}
