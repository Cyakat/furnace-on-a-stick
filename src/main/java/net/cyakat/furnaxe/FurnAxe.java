package net.cyakat.furnaxe;

import net.cyakat.furnaxe.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FurnAxe implements ModInitializer {

	public static final String MOD_ID = "furnaxe";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {



		LOGGER.info("Initializing Furn-Axe :o");

		ModItems.registerModItems();
	}
}
