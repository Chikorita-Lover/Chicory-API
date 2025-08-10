package net.chikorita_lover.chicory.mixin.recipe;

import net.minecraft.recipe.book.RecipeBookType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RecipeBookType.class)
public interface RecipeBookTypeAccessor {
    @Accessor("field_25767")
    static RecipeBookType[] getValues() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("field_25767")
    static void setValues(RecipeBookType[] values) {
        throw new AssertionError();
    }

    @Invoker("<init>")
    static RecipeBookType create(String name, int ordinal) {
        throw new AssertionError();
    }
}
