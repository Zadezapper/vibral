package net.zadezapper.vibral.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.zadezapper.vibral.Vibral;

public class ModTags {
    public static class Blocks {
        public static TagKey<Block> NEEDS_VIBRAL_TOOL = createTag("needs_vibral_tool");
        public static TagKey<Block> INCORRECT_FOR_VIBRAL_TOOL = createTag("incorrect_for_vibral_tool");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(Vibral.MOD_ID, name));
        }
    }

    public static class Items {

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(Vibral.MOD_ID, name));
        }
    }
}
