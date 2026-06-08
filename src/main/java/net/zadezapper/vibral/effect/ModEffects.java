package net.zadezapper.vibral.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.zadezapper.vibral.Vibral;

public class ModEffects {
    public static final RegistryEntry<StatusEffect> SILENCE = register("silence", new SilenceEffect(StatusEffectCategory.BENEFICIAL, 0x052A32));

    private static RegistryEntry<StatusEffect> register(String name, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Vibral.MOD_ID, name), statusEffect);
    }

    public static void registerEffects() {
        Vibral.LOGGER.info("Registering Effects for " + Vibral.MOD_ID);
    }
}
