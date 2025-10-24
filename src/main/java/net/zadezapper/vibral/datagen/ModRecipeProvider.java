package net.zadezapper.vibral.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.zadezapper.vibral.Vibral;
import net.zadezapper.vibral.block.ModBlocks;
import net.zadezapper.vibral.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.VIBRAL, 9)
                .input(ModBlocks.VIBRAL_BLOCK)
                .criterion(hasItem(ModBlocks.RAW_VIBRAL), conditionsFromItem(ModBlocks.RAW_VIBRAL))
                .offerTo(recipeExporter, Identifier.of(Vibral.MOD_ID, "vibral_from_vibral_block"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModBlocks.VIBRAL_BLOCK)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .input('#', ModItems.VIBRAL)
                .criterion(hasItem(ModItems.VIBRAL), conditionsFromItem(ModItems.VIBRAL))
                .offerTo(recipeExporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.VIBRAL)
                .input(ModBlocks.RAW_VIBRAL, 2)
                .input(Items.ECHO_SHARD, 2)
                .criterion(hasItem(ModBlocks.RAW_VIBRAL), conditionsFromItem(ModBlocks.RAW_VIBRAL))
                .offerTo(recipeExporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.VIBRAL_SWORD)
                .pattern(" # ")
                .pattern(" # ")
                .pattern(" | ")
                .input('#', ModItems.VIBRAL)
                .input('|', Items.STICK)
                .criterion(hasItem(ModItems.VIBRAL), conditionsFromItem(ModItems.VIBRAL))
                .offerTo(recipeExporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.VIBRAL_PICKAXE)
                .pattern("###")
                .pattern(" | ")
                .pattern(" | ")
                .input('#', ModItems.VIBRAL)
                .input('|', Items.STICK)
                .criterion(hasItem(ModItems.VIBRAL), conditionsFromItem(ModItems.VIBRAL))
                .offerTo(recipeExporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.VIBRAL_AXE)
                .pattern("## ")
                .pattern("#| ")
                .pattern(" | ")
                .input('#', ModItems.VIBRAL)
                .input('|', Items.STICK)
                .criterion(hasItem(ModItems.VIBRAL), conditionsFromItem(ModItems.VIBRAL))
                .offerTo(recipeExporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.VIBRAL_SHOVEL)
                .pattern(" # ")
                .pattern(" | ")
                .pattern(" | ")
                .input('#', ModItems.VIBRAL)
                .input('|', Items.STICK)
                .criterion(hasItem(ModItems.VIBRAL), conditionsFromItem(ModItems.VIBRAL))
                .offerTo(recipeExporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.VIBRAL_HOE)
                .pattern("## ")
                .pattern(" | ")
                .pattern(" | ")
                .input('#', ModItems.VIBRAL)
                .input('|', Items.STICK)
                .criterion(hasItem(ModItems.VIBRAL), conditionsFromItem(ModItems.VIBRAL))
                .offerTo(recipeExporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.VIBRAL_HELMET)
                .pattern("###")
                .pattern("# #")
                .pattern("   ")
                .input('#', ModItems.VIBRAL)
                .criterion(hasItem(ModItems.VIBRAL), conditionsFromItem(ModItems.VIBRAL))
                .offerTo(recipeExporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.VIBRAL_CHESTPLATE)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .input('#', ModItems.VIBRAL)
                .criterion(hasItem(ModItems.VIBRAL), conditionsFromItem(ModItems.VIBRAL))
                .offerTo(recipeExporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.VIBRAL_LEGGINGS)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .input('#', ModItems.VIBRAL)
                .criterion(hasItem(ModItems.VIBRAL), conditionsFromItem(ModItems.VIBRAL))
                .offerTo(recipeExporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.VIBRAL_BOOTS)
                .pattern("# #")
                .pattern("# #")
                .pattern("   ")
                .input('#', ModItems.VIBRAL)
                .criterion(hasItem(ModItems.VIBRAL), conditionsFromItem(ModItems.VIBRAL))
                .offerTo(recipeExporter);
    }
}
