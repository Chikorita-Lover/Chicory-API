package net.chikorita_lover.chicory.mixin.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BoatEntity.Type.class)
public interface BoatEntityTypeAccessor {
    @Accessor("field_7724")
    static BoatEntity.Type[] getValues() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("field_7724")
    static void setValues(BoatEntity.Type[] values) {
        throw new AssertionError();
    }

    @Invoker("<init>")
    static BoatEntity.Type create(String internalName, int ordinal, Block baseBlock, String name) {
        throw new AssertionError();
    }
}
