package net.zadezapper.vibral.enchantment;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.zadezapper.vibral.Vibral;

public class VibralEnchantments {
    public static final RegistryKey<Enchantment> COLLECTING = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Vibral.MOD_ID, "collecting"));
    public static final RegistryKey<Enchantment> OBSCURING = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Vibral.MOD_ID, "obscuring"));
    public static final RegistryKey<Enchantment> ECHO_MINING = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Vibral.MOD_ID, "echo_mining"));

    public static void bootstrap(Registerable<Enchantment> registerable) {
        var enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        register(registerable, COLLECTING, Enchantment.builder(Enchantment.definition(
            items.getOrThrow(TagKey.of(RegistryKeys.ITEM, Identifier.of(Vibral.MOD_ID, "vibral_tools"))),
            items.getOrThrow(TagKey.of(RegistryKeys.ITEM, Identifier.of(Vibral.MOD_ID, "vibral_tools"))),
            6,
            3,
            Enchantment.leveledCost(9, 11),
            Enchantment.leveledCost(22, 11),
            2,
            AttributeModifierSlot.MAINHAND
        )));

        register(registerable, OBSCURING, Enchantment.builder(Enchantment.definition(
            items.getOrThrow(TagKey.of(RegistryKeys.ITEM, Identifier.of(Vibral.MOD_ID, "vibral_armor"))),
            items.getOrThrow(TagKey.of(RegistryKeys.ITEM, Identifier.of(Vibral.MOD_ID, "vibral_armor"))),
            3,
            3,
            Enchantment.leveledCost(12, 12),
            Enchantment.leveledCost(26, 16),
            2,
            AttributeModifierSlot.ARMOR
        )));

        register(registerable, ECHO_MINING, Enchantment.builder(Enchantment.definition(
            items.getOrThrow(TagKey.of(RegistryKeys.ITEM, Identifier.of(Vibral.MOD_ID, "vibral_tools"))),
            items.getOrThrow(TagKey.of(RegistryKeys.ITEM, Identifier.of(Vibral.MOD_ID, "vibral_tools"))),
            8,
            3,
            Enchantment.leveledCost(8, 10),
            Enchantment.leveledCost(19, 11),
            1,
            AttributeModifierSlot.MAINHAND
        )));
    }

    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }
}
