package net.chikorita_lover.chicory.mixin.client.recipe;

import net.minecraft.client.recipebook.RecipeBookType;
import net.minecraft.recipe.book.RecipeBookCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RecipeBookType.class)
public interface ClientRecipeBookTypeAccessor {
    @Accessor("field_54842")
    static RecipeBookType[] getValues() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("field_54842")
    static void setValues(RecipeBookType[] values) {
        throw new AssertionError();
    }

    @Invoker("<init>")
    static RecipeBookType create(String name, int ordinal, RecipeBookCategory... categories) {
        throw new AssertionError();
    }
}
