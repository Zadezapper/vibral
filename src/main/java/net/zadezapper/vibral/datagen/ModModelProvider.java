package net.zadezapper.vibral.datagen;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.zadezapper.vibral.Vibral;
import net.zadezapper.vibral.block.ModBlocks;
import net.zadezapper.vibral.item.ModItems;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ModModelProvider extends FabricModelProvider { // I Deleted some of the things ChatGPT gave me
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    public static final Model SCULK_VEIN = newModel("sculk_vein", "", TextureKey.TOP, TextureKey.BOTTOM); // Attempt at making a new method for BlockStateModelGenerator

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.VIBRAL_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.RAW_VIBRAL_BLOCK);
        blockStateModelGenerator.registerWallPlant(ModBlocks.RAW_VIBRAL); // Registers blockstate file, but no model

        Identifier modelId = ModelIds.getBlockModelId(ModBlocks.RAW_VIBRAL);
        Identifier parent = Identifier.of("minecraft", "block/sculk_vein");
        Identifier texture = Identifier.of(Vibral.MOD_ID, "block/raw_vibral");

        // Create and register the custom model as a Supplier
        // blockStateModelGenerator.modelCollector.accept(modelId, (Supplier<JsonElement>) createCustomModel(texture, parent)); // ChatGPT

        /* SCULK_VEIN.upload( // ChatGPT
                ModBlocks.RAW_VIBRAL,
                TextureMap::all,
        );
        blockStateModelGenerator.createSubModel( // ChatGPT
                ModBlocks.RAW_VIBRAL,
                "vibral:block/raw_vibral", // new Identifier(Vibral.MOD_ID, "block/raw_vibral"), // Reference your custom model file
                TexturedModel.of(ModBlocks.RAW_VIBRAL).getTextures()
        );
        Function<Identifier, TextureMap> identifierToTextureMap;
        blockStateModelGenerator.createSubModel( // ChatGPT
                ModBlocks.RAW_VIBRAL,
                "vibral:block/raw_vibral",
                SCULK_VEIN,
                identifierToTextureMap
        );
        blockStateModelGenerator.modelCollector.accept(ModelIds.getBlockModelId(ModBlocks.RAW_VIBRAL), // ChatGPT
                blockStateModelGenerator.modelProvider.models().withExistingParent(
                                ModBlocks.RAW_VIBRAL.getRegistryName().getPath(),
                                new Identifier("block/template_vein")) // Use the template_vein parent
                        .texture("vein", new Identifier(Vibral.MOD_ID, "block/raw_vibral"))
        ); */
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.VIBRAL, Models.GENERATED);

        itemModelGenerator.register(ModItems.VIBRAL_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.VIBRAL_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.VIBRAL_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.VIBRAL_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.VIBRAL_HOE, Models.HANDHELD);

        itemModelGenerator.register(ModItems.VIBRAL_HELMET, Models.GENERATED);
        itemModelGenerator.register(ModItems.VIBRAL_CHESTPLATE, Models.GENERATED);
        itemModelGenerator.register(ModItems.VIBRAL_LEGGINGS, Models.GENERATED);
        itemModelGenerator.register(ModItems.VIBRAL_BOOTS, Models.GENERATED);
    }

    public static void generateTexturedModels() {// Attempt at making a new method for BlockStateModelGenerator
        TexturedModel.Factory WALL_PLANT = TexturedModel.makeFactory(TextureMap::topBottom, SCULK_VEIN);
    }

    private static Model newModel(String parent, String variant, TextureKey... requiredTextureKeys) { // Attempt at making a new method for BlockStateModelGenerator
        return new Model(Optional.of(Identifier.ofVanilla("block/" + parent)), Optional.of(variant), requiredTextureKeys);
    }

    private JsonUnbakedModel createCustomModel(Identifier texture, Identifier parent) { // ChatGPT
        // Define your custom block model here using JsonUnbakedModel
        SpriteIdentifier spriteId = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture);
        Map<String, Either<SpriteIdentifier, String>> textures = new HashMap<>();
        textures.put("all", Either.left(spriteId)); // using "all" for every face

        return new JsonUnbakedModel(
                parent,
                List.of(),
                textures,
                null,
                JsonUnbakedModel.GuiLight.ITEM,
                ModelTransformation.NONE,
                List.of()
        );
    }
}
