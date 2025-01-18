package net.chikorita_lover.chicory.mixin.entity;

import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractMinecartEntity.Type.class)
public interface AbstractMinecartEntityTypeAccessor {
    @Accessor("field_7673")
    static AbstractMinecartEntity.Type[] getValues() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("field_7673")
    static void setValues(AbstractMinecartEntity.Type[] values) {
        throw new AssertionError();
    }

    @Invoker("<init>")
    static AbstractMinecartEntity.Type create(String name, int ordinal) {
        throw new AssertionError();
    }
}
