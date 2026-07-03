package net.zadezapper.vibral.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.zadezapper.vibral.block.VibralBlocks;
import net.zadezapper.vibral.item.VibralItems;

import java.util.Optional;

public class VibralModelProvider extends FabricModelProvider { // I Deleted some of the things ChatGPT gave me
    public VibralModelProvider(FabricDataOutput output) {
        super(output);
    }

    public static final Model SCULK_VEIN = newModel("sculk_vein", "", TextureKey.TOP, TextureKey.BOTTOM); // Attempt at making a new method for BlockStateModelGenerator

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(VibralBlocks.VIBRAL_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(VibralBlocks.RAW_VIBRAL_BLOCK);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(VibralItems.VIBRAL, Models.GENERATED);

        itemModelGenerator.register(VibralItems.VIBRAL_SWORD, Models.HANDHELD);
        itemModelGenerator.register(VibralItems.VIBRAL_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(VibralItems.VIBRAL_AXE, Models.HANDHELD);
        itemModelGenerator.register(VibralItems.VIBRAL_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(VibralItems.VIBRAL_HOE, Models.HANDHELD);

        itemModelGenerator.register(VibralItems.VIBRAL_HELMET, Models.GENERATED);
        itemModelGenerator.register(VibralItems.VIBRAL_CHESTPLATE, Models.GENERATED);
        itemModelGenerator.register(VibralItems.VIBRAL_LEGGINGS, Models.GENERATED);
        itemModelGenerator.register(VibralItems.VIBRAL_BOOTS, Models.GENERATED);
    }

    private static Model newModel(String parent, String variant, TextureKey... requiredTextureKeys) { // Attempt at making a new method for BlockStateModelGenerator
        return new Model(Optional.of(Identifier.ofVanilla("block/" + parent)), Optional.of(variant), requiredTextureKeys);
    }
}
