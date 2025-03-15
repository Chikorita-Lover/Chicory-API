package net.chikorita_lover.chicory.api.registry;

import net.chikorita_lover.chicory.api.resource.ToggleableFeatureRegistry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.ToggleableFeature;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

/**
 * An interface containing utility methods for registering items.
 */
public interface ItemRegistry {
    /**
     * Registers a provided {@code item} with a provided {@code id}.
     *
     * @param id   the identifier to assign
     * @param item the item to register
     * @return the registered item
     */
    static Item register(Identifier id, Item item) {
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }
        return Registry.register(Registries.ITEM, id, item);
    }

    /**
     * Registers a provided {@code item} with a provided {@code id} that is enabled based on {@code condition}.
     *
     * @param id        the identifier to assign
     * @param item      the item to register
     * @param condition the condition under which the item is enabled
     * @return the registered item
     * @see ToggleableFeatureRegistry#add(ToggleableFeature, Supplier)
     */
    static Item register(Identifier id, Item item, Supplier<Boolean> condition) {
        ToggleableFeatureRegistry.add(item, condition);
        return register(id, item);
    }
}
