package net.zadezapper.vibral.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.zadezapper.vibral.block.VibralBlocks;

import java.util.concurrent.CompletableFuture;

public class VibralBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public VibralBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
            .add(VibralBlocks.VIBRAL_BLOCK)
            .add(VibralBlocks.VIBRAL_PANEL)
            .add(VibralBlocks.RAW_VIBRAL_BLOCK);
        getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL)
            .add(VibralBlocks.VIBRAL_BLOCK)
            .add(VibralBlocks.VIBRAL_PANEL)
            .add(VibralBlocks.RAW_VIBRAL_BLOCK);
        getOrCreateTagBuilder(BlockTags.HOE_MINEABLE)
            .add(VibralBlocks.RAW_VIBRAL);
        getOrCreateTagBuilder(BlockTags.DAMPENS_VIBRATIONS)
            .add(VibralBlocks.VIBRAL_PANEL)
            .add(VibralBlocks.VIBRAL_BLOCK);
    }
}
