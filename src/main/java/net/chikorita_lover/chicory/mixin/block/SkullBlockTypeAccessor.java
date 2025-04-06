package net.chikorita_lover.chicory.mixin.block;

import net.minecraft.block.SkullBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SkullBlock.Type.class)
public interface SkullBlockTypeAccessor {
    @Accessor("field_11509")
    static SkullBlock.Type[] getValues() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("field_11509")
    static void setValues(SkullBlock.Type[] values) {
        throw new AssertionError();
    }

    @Invoker("<init>")
    static SkullBlock.Type create(String name, int ordinal, String id) {
        throw new AssertionError();
    }
}
