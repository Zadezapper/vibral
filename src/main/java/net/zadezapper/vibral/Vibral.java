package net.zadezapper.vibral;

import net.fabricmc.api.ModInitializer;
import net.zadezapper.vibral.block.ModBlocks;
import net.zadezapper.vibral.item.ModItems;
import net.zadezapper.vibral.sound.ModSoundEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vibral implements ModInitializer {
	public static final String MOD_ID = "vibral";
	public static final Logger LOGGER = LoggerFactory.getLogger("Vibral");

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModSoundEvents.registerSounds();
		ModBlocks.registerModBlocks();
	}
}