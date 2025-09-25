package net.zadezapper.vibral.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.zadezapper.vibral.Vibral;

public class ModSoundEvents {
    public static final SoundEvent SILENT = registerSoundEvent("silent");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(Vibral.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        Vibral.LOGGER.info("Registering Sounds for " + Vibral.MOD_ID);
    }
}
