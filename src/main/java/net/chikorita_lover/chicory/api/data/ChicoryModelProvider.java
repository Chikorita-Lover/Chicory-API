package net.chikorita_lover.chicory.api.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureMap;
import net.minecraft.util.Identifier;

public abstract class ChicoryModelProvider extends FabricModelProvider {
    private static final Identifier SKULL_MODEL_ID = ModelIds.getMinecraftNamespacedBlock("skull");

    public ChicoryModelProvider(FabricDataOutput output) {
        super(output);
    }

    public static void registerSkull(BlockStateModelGenerator generator, Block block, Block wallBlock) {
        generator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, SKULL_MODEL_ID));
        generator.excludeFromSimpleItemModelGeneration(wallBlock);
        generator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(wallBlock, SKULL_MODEL_ID));
        Models.TEMPLATE_SKULL.upload(ModelIds.getItemModelId(block.asItem()), TextureMap.particle(block), generator.modelCollector);
    }
}
