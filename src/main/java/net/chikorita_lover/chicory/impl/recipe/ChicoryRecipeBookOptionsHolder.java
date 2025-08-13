package net.chikorita_lover.chicory.impl.recipe;

import net.chikorita_lover.chicory.api.recipe.RecipeBookTypeRegistry;
import net.minecraft.recipe.book.RecipeBookOptions;
import net.minecraft.recipe.book.RecipeBookType;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

public interface ChicoryRecipeBookOptionsHolder {
    RecipeBookOptions.CategoryOption chicory$getOption(RecipeBookType type);

    void chicory$setOption(RecipeBookType type, RecipeBookOptions.CategoryOption option);

    default void chicory$readData(ReadView view) {
        ReadView recipeView = view.getReadView("options");
        for (RecipeBookType type : RecipeBookTypeRegistry.TYPES) {
            ReadView typeView = recipeView.getReadView(type.name().toLowerCase());
            boolean open = typeView.getBoolean("open", false);
            boolean filtering = typeView.getBoolean("filtering", false);
            this.chicory$setOption(type, new RecipeBookOptions.CategoryOption(open, filtering));
        }
    }

    default void chicory$writeData(WriteView view) {
        WriteView recipeView = view.get("options");
        for (RecipeBookType type : RecipeBookTypeRegistry.TYPES) {
            WriteView typeView = recipeView.get(type.name().toLowerCase());
            RecipeBookOptions.CategoryOption option = this.chicory$getOption(type);
            typeView.putBoolean("open", option.guiOpen());
            typeView.putBoolean("filtering", option.filteringCraftable());
        }
    }

    // RecipeBookOptions.CategoryOption option = this.chicory$getOption(type);
}
