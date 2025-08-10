package net.chikorita_lover.chicory.api.registry;

import net.chikorita_lover.chicory.api.resource.ToggleableFeatureRegistry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.featuretoggle.ToggleableFeature;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An interface containing utility methods for registering items.
 */
public interface ItemRegistry {
    /**
     * Registers a provided {@code item} with a provided {@code id}.
     *
     * @param key     the registry key to assign
     * @param factory a factory for creating the item
     * @return the registered item
     */
    static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
        Item item = factory.apply(settings.registryKey(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }
        return Registry.register(Registries.ITEM, key, item);
    }

    /**
     * Registers a provided {@code item} with a provided {@code id} that is enabled based on {@code condition}.
     *
     * @see ToggleableFeatureRegistry#add(ToggleableFeature, Supplier)
     */
    static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings, Supplier<Boolean> condition) {
        Item item = register(key, factory, settings);
        ToggleableFeatureRegistry.add(item, condition);
        return item;
    }
}
