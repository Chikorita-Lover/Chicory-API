package net.chikorita_lover.chicory;

import net.chikorita_lover.chicory.data.ChicoryBlockTagProvider;
import net.chikorita_lover.chicory.data.ChicoryEntityTypeTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ChicoryApiDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(ChicoryBlockTagProvider::new);
        pack.addProvider(ChicoryEntityTypeTagProvider::new);
    }
}
