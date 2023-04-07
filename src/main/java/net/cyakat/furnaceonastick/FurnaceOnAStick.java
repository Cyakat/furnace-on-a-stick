package net.cyakat.furnaceonastick;

import net.cyakat.furnaceonastick.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FurnaceOnAStick implements ModInitializer {

	public static final String MOD_ID = "furnaceonastick";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		LOGGER.info("Initializing Furnace On a Stick :o");

		ModItems.registerModItems();
	}
}
