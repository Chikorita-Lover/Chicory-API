package net.chikorita_lover.chicory.registry.tag;

import net.chikorita_lover.chicory.ChicoryApi;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ChicoryEntityTypeTags {
    public static final TagKey<EntityType<?>> MONSTERS = of("monsters");

    private static TagKey<EntityType<?>> of(String path) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, ChicoryApi.of(path));
    }
}
