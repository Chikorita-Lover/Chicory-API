package net.chikorita_lover.chicory.data;

import net.chikorita_lover.chicory.registry.tag.ChicoryBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ChicoryBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ChicoryBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries) {
        this.getOrCreateTagBuilder(ChicoryBlockTags.CONDUIT_ACTIVATING_BLOCKS).add(Blocks.PRISMARINE, Blocks.PRISMARINE_BRICKS, Blocks.SEA_LANTERN, Blocks.DARK_PRISMARINE);
        this.getOrCreateTagBuilder(ChicoryBlockTags.SIGNAL_FIRE_BASE_BLOCKS).add(Blocks.HAY_BLOCK);
    }
}
