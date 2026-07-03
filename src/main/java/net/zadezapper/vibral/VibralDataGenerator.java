package net.zadezapper.vibral;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.zadezapper.vibral.datagen.VibralBlockTagProvider;
import net.zadezapper.vibral.datagen.VibralItemTagProvider;
import net.zadezapper.vibral.datagen.VibralLootTableProvider;
import net.zadezapper.vibral.datagen.VibralRegistryDataGenerator;
import net.zadezapper.vibral.enchantment.VibralEnchantments;

public class VibralDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(VibralBlockTagProvider::new);
		pack.addProvider(VibralItemTagProvider::new);
		pack.addProvider(VibralLootTableProvider::new);
		// pack.addProvider(VibralModelProvider::new);
		pack.addProvider(VibralRegistryDataGenerator::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, VibralEnchantments::bootstrap);
	}
}
