package net.chikorita_lover.chicory.api.entity;

import net.chikorita_lover.chicory.mixin.entity.ParrotEntityAccessor;
import net.fabricmc.fabric.impl.content.registry.util.ImmutableCollectionUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.sound.SoundEvent;

import java.util.Map;

/**
 * A registry for parrot imitation sounds.
 */
public class ParrotSoundRegistry {
    /**
     * Registers a parrot imitation interaction.
     *
     * @param entityType the entity type that can be imitated
     * @param sound      the imitation sound event
     */
    public static void register(EntityType<?> entityType, SoundEvent sound) {
        getRegistry().put(entityType, sound);
    }

    private static Map<EntityType<?>, SoundEvent> getRegistry() {
        return ImmutableCollectionUtils.getAsMutableMap(ParrotEntityAccessor::getMobSounds, ParrotEntityAccessor::setMobSounds);
    }
}
