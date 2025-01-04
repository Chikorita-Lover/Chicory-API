package net.chikorita_lover.chicory.data;

import net.chikorita_lover.chicory.registry.tag.ChicoryEntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ChicoryEntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
    public ChicoryEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries) {
        this.getOrCreateTagBuilder(ChicoryEntityTypeTags.MONSTERS);
    }
}
