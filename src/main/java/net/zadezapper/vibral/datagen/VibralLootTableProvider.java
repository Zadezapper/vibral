package net.zadezapper.vibral.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;
import net.zadezapper.vibral.block.VibralBlocks;

import java.util.concurrent.CompletableFuture;

public class VibralLootTableProvider extends FabricBlockLootTableProvider {
    public VibralLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(VibralBlocks.VIBRAL_BLOCK);
        addDrop(VibralBlocks.RAW_VIBRAL_BLOCK);
        addDrop(VibralBlocks.RAW_VIBRAL, dropsWithSilkTouch(VibralBlocks.RAW_VIBRAL));
        addDrop(VibralBlocks.VIBRAL_PANEL);
    }
}
