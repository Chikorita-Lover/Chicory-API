package net.chikorita_lover.chicory.registry.tag;

import net.chikorita_lover.chicory.ChicoryApi;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public final class ChicoryItemTags {
    private static TagKey<Item> of(String path) {
        return TagKey.of(RegistryKeys.ITEM, ChicoryApi.of(path));
    }
}
