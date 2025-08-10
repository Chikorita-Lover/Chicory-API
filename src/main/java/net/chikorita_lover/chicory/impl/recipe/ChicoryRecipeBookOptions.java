package net.chikorita_lover.chicory.impl.recipe;

import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.chikorita_lover.chicory.api.recipe.RecipeBookTypeRegistry;
import net.minecraft.recipe.book.RecipeBookType;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

import java.util.Optional;

public class ChicoryRecipeBookOptions {
    private final Object2BooleanMap<RecipeBookType> guiOpen = new Object2BooleanArrayMap<>();
    private final Object2BooleanMap<RecipeBookType> filteringCraftable = new Object2BooleanArrayMap<>();

    public boolean isGuiOpen(RecipeBookType type) {
        return this.guiOpen.getOrDefault(type, false);
    }

    public void setGuiOpen(RecipeBookType type, boolean open) {
        this.guiOpen.put(type, open);
    }

    public boolean isFilteringCraftable(RecipeBookType type) {
        return this.filteringCraftable.getOrDefault(type, false);
    }

    public void setFilteringCraftable(RecipeBookType type, boolean filter) {
        this.filteringCraftable.put(type, filter);
    }

    public void readData(ReadView view) {
        Optional<ReadView> recipeView = view.getOptionalReadView("chicory_recipe_book");
        if (recipeView.isEmpty()) {
            return;
        }
        for (RecipeBookType type : RecipeBookTypeRegistry.TYPES) { // TODO use Chicory registry
            Optional<ReadView> typeView = recipeView.get().getOptionalReadView(type.name().toUpperCase());
            if (typeView.isPresent()) {
                this.setGuiOpen(type, typeView.get().getBoolean("gui_open", false));
                this.setFilteringCraftable(type, typeView.get().getBoolean("filtering_craftable", false));
            }
        }
    }

    public void writeData(WriteView view) {
        WriteView recipeView = view.get("chicory_recipe_book");
        for (RecipeBookType type : RecipeBookTypeRegistry.TYPES) {
            WriteView typeView = recipeView.get(type.name().toLowerCase());
            typeView.putBoolean("gui_open", this.isGuiOpen(type));
            typeView.putBoolean("filtering_craftable", this.isFilteringCraftable(type));
        }
    }
}
