package net.zadezapper.vibral;

import net.fabricmc.api.ModInitializer;
import net.zadezapper.vibral.block.VibralBlocks;
import net.zadezapper.vibral.block.entity.VibralBlockEntities;
import net.zadezapper.vibral.effect.VibralEffects;
import net.zadezapper.vibral.item.VibralItems;
import net.zadezapper.vibral.sound.VibralSoundEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vibral implements ModInitializer {
	public static final String MOD_ID = "vibral";
	public static final Logger LOGGER = LoggerFactory.getLogger("Vibral");

	@Override
	public void onInitialize() {
		VibralItems.registerClass();
		VibralSoundEvents.registerClass();
		VibralBlocks.registerClass();
		VibralBlockEntities.registerClass();
		VibralEffects.registerClass();
	}
}