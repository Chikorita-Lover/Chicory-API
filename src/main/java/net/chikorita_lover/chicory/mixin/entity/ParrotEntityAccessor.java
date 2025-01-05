package net.chikorita_lover.chicory.mixin.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ParrotEntity.class)
public interface ParrotEntityAccessor {
    @Accessor("MOB_SOUNDS")
    static Map<EntityType<?>, SoundEvent> getMobSounds() {
        throw new AssertionError("Untransformed @Accessor");
    }

    @Accessor("MOB_SOUNDS")
    @Mutable
    static void setMobSounds(Map<EntityType<?>, SoundEvent> mobSounds) {
        throw new AssertionError("Untransformed @Accessor");
    }
}
