package net.chikorita_lover.chicory.mixin.client.recipe;

import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Map;

@Mixin(RecipeBookGroup.class)
public interface RecipeBookGroupAccessor {
    @Accessor("field_1805")
    static RecipeBookGroup[] getValues() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("field_1805")
    static void setValues(RecipeBookGroup[] values) {
        throw new AssertionError();
    }

    @Accessor("SEARCH_MAP")
    static Map<RecipeBookGroup, List<RecipeBookGroup>> getSearchMap() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("SEARCH_MAP")
    static void setSearchMap(Map<RecipeBookGroup, List<RecipeBookGroup>> searchMap) {
        throw new AssertionError();
    }

    @Invoker("<init>")
    static RecipeBookGroup create(String name, int ordinal, ItemStack... entries) {
        throw new AssertionError();
    }
}
