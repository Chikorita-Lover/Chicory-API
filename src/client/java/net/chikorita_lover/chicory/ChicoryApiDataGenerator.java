package net.chikorita_lover.chicory;

import net.chikorita_lover.chicory.data.ChicoryBlockTagProvider;
import net.chikorita_lover.chicory.data.ChicoryEntityTypeTagProvider;
import net.chikorita_lover.chicory.data.ChicoryItemTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

public class ChicoryApiDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        final FabricTagProvider.BlockTagProvider blockTagProvider = pack.addProvider(ChicoryBlockTagProvider::new);
        pack.addProvider(ChicoryEntityTypeTagProvider::new);
        pack.addProvider((output, completableFuture) -> new ChicoryItemTagProvider(output, completableFuture, blockTagProvider));
    }
}
