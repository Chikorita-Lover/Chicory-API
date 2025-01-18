package net.chikorita_lover.chicory.api.recipe;

import com.mojang.datafixers.util.Pair;
import net.chikorita_lover.chicory.mixin.recipe.RecipeBookCategoryAccessor;
import net.chikorita_lover.chicory.mixin.recipe.RecipeBookOptionsAccessor;
import net.fabricmc.fabric.impl.content.registry.util.ImmutableCollectionUtils;
import net.minecraft.recipe.book.RecipeBookCategory;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A registry for creating recipe book categories.
 */
public final class RecipeBookCategoryRegistry {
    /**
     * Creates and returns a new recipe book category.
     * This also creates the keys necessary for storing player NBT related to recipe book categories.
     *
     * @param name the internal name of the recipe book category
     * @return the new recipe book category
     */
    @SuppressWarnings("UnreachableCode")
    public static RecipeBookCategory register(String name) {
        List<RecipeBookCategory> values = new ArrayList<>(Arrays.asList(RecipeBookCategoryAccessor.getValues()));
        int ordinal = values.getLast().ordinal() + 1;
        RecipeBookCategory category = RecipeBookCategoryAccessor.create(name.toUpperCase(), ordinal);
        values.add(category);
        RecipeBookCategoryAccessor.setValues(values.toArray(RecipeBookCategory[]::new));
        name = "is" + WordUtils.capitalize(category.name().toLowerCase(), '_').replace("_", "");
        getOptionNames().put(category, Pair.of(name.concat("GuiOpen"), name.concat("FilteringCraftable")));
        return category;
    }

    @SuppressWarnings("UnstableApiUsage")
    private static Map<RecipeBookCategory, Pair<String, String>> getOptionNames() {
        return ImmutableCollectionUtils.getAsMutableMap(RecipeBookOptionsAccessor::getCategoryOptionNames, RecipeBookOptionsAccessor::setCategoryOptionNames);
    }
}
