package net.chikorita_lover.chicory.recipe;

import net.chikorita_lover.chicory.ChicoryApi;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class ChicoryRecipeSerializers {
    public static final RecipeSerializer<TransmuteRecipe> TRANSMUTE = register("crafting_transmute", new TransmuteRecipe.Serializer());

    public static void register() {
    }

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, ChicoryApi.of(id), serializer);
    }
}
