package net.chikorita_lover.chicory.api.registry;

import net.chikorita_lover.chicory.api.resource.ToggleableFeatureRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.WallSkullBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.ToggleableFeature;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

/**
 * An interface containing utility methods for registering blocks.
 */
public interface BlockRegistry {
    /**
     * Registers a provided {@code block} with a provided {@code id}.
     *
     * @param id    the identifier to assign
     * @param block the block to register
     * @return the registered block
     */
    static Block register(Identifier id, Block block) {
        return Registry.register(Registries.BLOCK, id, block);
    }

    /**
     * Registers a provided {@code block} with a provided {@code id} that is enabled based on {@code condition}.
     *
     * @param id        the identifier to assign
     * @param block     the block to register
     * @param condition the condition under which the block is enabled
     * @return the registered block
     * @see ToggleableFeatureRegistry#add(ToggleableFeature, Supplier)
     */
    static Block register(Identifier id, Block block, Supplier<Boolean> condition) {
        ToggleableFeatureRegistry.add(block, condition);
        return register(id, block);
    }

    /**
     * Registers a provided {@code block} with a provided {@code id} and creates and registers an associated item.
     *
     * @param id    the identifier to assign
     * @param block the block to register
     * @return the registered block
     */
    static Block registerWithItem(Identifier id, Block block) {
        ItemRegistry.register(id, new BlockItem(block, new Item.Settings()));
        return register(id, block);
    }

    /**
     * Registers a provided {@code block} with a provided {@code id} and creates and registers an associated item.
     * Both the block and item are enabled based on {@code condition}.
     *
     * @param id        the identifier to assign
     * @param block     the block to register
     * @param condition the condition under which the block and item are enabled
     * @return the registered block
     * @see ToggleableFeatureRegistry#add(ToggleableFeature, Supplier)
     */
    static Block registerWithItem(Identifier id, Block block, Supplier<Boolean> condition) {
        ItemRegistry.register(id, new BlockItem(block, new Item.Settings()), condition);
        return register(id, block, condition);
    }

    static Block createWallSkullBlock(Block block) {
        return new WallSkullBlock(block instanceof SkullBlock skull ? skull.getSkullType() : SkullBlock.Type.SKELETON, AbstractBlock.Settings.copy(block).dropsLike(block));
    }
}
