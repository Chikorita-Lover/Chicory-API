package net.chikorita_lover.chicory.mixin.recipe;

import com.mojang.datafixers.util.Pair;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.book.RecipeBookOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RecipeBookOptions.class)
public interface RecipeBookOptionsAccessor {
    @Accessor("CATEGORY_OPTION_NAMES")
    static Map<RecipeBookCategory, Pair<String, String>> getCategoryOptionNames() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("CATEGORY_OPTION_NAMES")
    static void setCategoryOptionNames(Map<RecipeBookCategory, Pair<String, String>> categoryOptionNames) {
        throw new AssertionError();
    }
}
