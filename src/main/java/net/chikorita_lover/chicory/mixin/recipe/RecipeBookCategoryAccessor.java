package net.chikorita_lover.chicory.mixin.recipe;

import net.minecraft.recipe.book.RecipeBookCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RecipeBookCategory.class)
public interface RecipeBookCategoryAccessor {
    @Accessor("field_25767")
    static RecipeBookCategory[] getValues() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("field_25767")
    static void setValues(RecipeBookCategory[] values) {
        throw new AssertionError();
    }

    @Invoker("<init>")
    static RecipeBookCategory create(String name, int ordinal) {
        throw new AssertionError();
    }
}
