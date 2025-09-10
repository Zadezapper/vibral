package net.zadezapper.vibral.item;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zadezapper.vibral.Vibral;
import net.zadezapper.vibral.block.ModBlocks;

/// ## Planned Features
///
/// Flashbang
///
/// Smoke bomb
///
/// Echo Potion
///
/// Deafness Potion
///
/// Rebound Bundle
///


public class ModItems {
    public static final Item VIBRAL = registerItem("vibral", new Item(new Item.Settings()));
    public static final Item VIBRAL_SHOVEL = registerItem("vibral_shovel",
            new ShovelItem(ModToolMaterials.VIBRAL, new Item.Settings()
                    .attributeModifiers(ShovelItem.createAttributeModifiers(ModToolMaterials.VIBRAL, 1.5f, -3))));

    public static final Item VIBRAL_PICKAXE = registerItem("vibral_pickaxe",
            new PickaxeItem(ModToolMaterials.VIBRAL, new Item.Settings()
                    .attributeModifiers(PickaxeItem.createAttributeModifiers(ModToolMaterials.VIBRAL, 1, -2.8f))));

    public static final Item VIBRAL_AXE = registerItem("vibral_axe",
            new AxeItem(ModToolMaterials.VIBRAL, new Item.Settings()
                    .attributeModifiers(AxeItem.createAttributeModifiers(ModToolMaterials.VIBRAL, 6, -3.2f))));

    public static final Item VIBRAL_HOE = registerItem("vibral_hoe",
            new HoeItem(ModToolMaterials.VIBRAL, new Item.Settings()
                    .attributeModifiers(HoeItem.createAttributeModifiers(ModToolMaterials.VIBRAL, 0, -3))));

    public static final Item VIBRAL_SWORD = registerItem("vibral_sword",
            new SwordItem(ModToolMaterials.VIBRAL, new Item.Settings()
                    .attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.VIBRAL, 3, -2.4f))));

    public static final Item VIBRAL_HELMET = registerItem("vibral_helmet",
            new ArmorItem(ModArmorMaterials.VIBRAL_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                    .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(30))));

    public static final Item VIBRAL_CHESTPLATE = registerItem("vibral_chestplate",
            new ArmorItem(ModArmorMaterials.VIBRAL_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                    .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(30))));

    public static final Item VIBRAL_LEGGINGS = registerItem("vibral_leggings",
            new ArmorItem(ModArmorMaterials.VIBRAL_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(30))));

    public static final Item VIBRAL_BOOTS = registerItem("vibral_boots",
            new ArmorItem(ModArmorMaterials.VIBRAL_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(30))));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Vibral.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Vibral.LOGGER.info("Registering Items for " + Vibral.MOD_ID);
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RAW_VIBRAL, RenderLayer.getCutout());

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(VIBRAL_SHOVEL);
            entries.add(VIBRAL_PICKAXE);
            entries.add(VIBRAL_AXE);
            entries.add(VIBRAL_HOE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(VIBRAL_SWORD);
            entries.add(VIBRAL_HELMET);
            entries.add(VIBRAL_CHESTPLATE);
            entries.add(VIBRAL_LEGGINGS);
            entries.add(VIBRAL_BOOTS);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(VIBRAL);
        });
    }
}
