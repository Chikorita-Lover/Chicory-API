package net.chikorita_lover.chicory.api.recipe;

import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.recipe.book.RecipeBookType;

import java.util.HashMap;
import java.util.Map;

public final class RecipeScreenHelper {
    public static final Map<RecipeBookType, net.minecraft.client.recipebook.RecipeBookType> RECIPE_TYPE_TO_GROUP = new HashMap<>();

    /**
     * Creates and returns a {@code RecipeBookWidget.Tab} from the provided recipe book type for use in a {@code RecipeBookScreen}.
     *
     * @param type a recipe book type registered through Chicory API
     * @throws IllegalArgumentException if the type is not registered through Chicory API
     * @see net.chikorita_lover.chicory.api.recipe.RecipeBookTypeRegistry
     */
    public static RecipeBookWidget.Tab createTab(RecipeBookType type) {
        if (!RECIPE_TYPE_TO_GROUP.containsKey(type)) {
            throw new IllegalArgumentException("RecipeScreenHelper class only supports recipe book types registered through Chicory API");
        }
        return new RecipeBookWidget.Tab(RECIPE_TYPE_TO_GROUP.get(type));
    }
}
