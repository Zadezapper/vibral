package net.zadezapper.vibral.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.zadezapper.vibral.Vibral;
import net.zadezapper.vibral.block.advanced.RawVibralBlock;

public class ModBlocks {
    public static final Block VIBRAL_BLOCK = registerBlock(true, "vibral_block",
            new Block(AbstractBlock.Settings.create()
                    .strength(5f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL))
    );
    public static final Block RAW_VIBRAL_BLOCK = registerBlock(true, "raw_vibral_block",
            new Block(AbstractBlock.Settings.create()
                    .strength(0.6f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL))
    );
    /* public static final Block RAW_VIBRAL = registerBlock(true, "raw_vibral",
            new Block(AbstractBlock.Settings.create()
                    .strength(0.6f)
                    .sounds(BlockSoundGroup.SCULK))
    ); */
    public static final Block RAW_VIBRAL = registerBlock(true, "raw_vibral",
            new RawVibralBlock(AbstractBlock.Settings.create()
                    .strength(0.6f)
                    .sounds(BlockSoundGroup.SCULK))
    );

    private static Block registerBlock(boolean shouldCreateItem, String name, Block block) {
        if (shouldCreateItem) {
            registerBlockItem(name, block);
        }
        return Registry.register(Registries.BLOCK, Identifier.of(Vibral.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Vibral.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        Vibral.LOGGER.info("Registering Mod Blocks for " + Vibral.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(VIBRAL_BLOCK);
            entries.add(RAW_VIBRAL_BLOCK);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.add(RAW_VIBRAL);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(RAW_VIBRAL);
        });
    }
}
