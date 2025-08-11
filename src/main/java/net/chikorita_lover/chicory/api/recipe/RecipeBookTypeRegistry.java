package net.chikorita_lover.chicory.api.recipe;

import net.chikorita_lover.chicory.mixin.recipe.RecipeBookTypeAccessor;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.book.RecipeBookType;

import java.util.*;

/**
 * A registry for creating recipe book types.
 */
public final class RecipeBookTypeRegistry {
    public static final List<RecipeBookType> TYPES = new ArrayList<>();
    public static final Map<RecipeBookType, RecipeBookCategory[]> TYPE_TO_CATEGORIES = new HashMap<>();

    /**
     * Creates and returns a new recipe book type.
     *
     * @param name       the internal name
     * @param categories the type's categories to display as tabs
     * @return the new recipe book type
     */
    @SuppressWarnings("UnreachableCode")
    public static RecipeBookType register(String name, RecipeBookCategory... categories) {
        List<RecipeBookType> values = new ArrayList<>(Arrays.asList(RecipeBookTypeAccessor.getValues()));
        int ordinal = values.size();
        RecipeBookType type = RecipeBookTypeAccessor.create(name.toUpperCase(), ordinal);
        values.add(type);
        TYPES.add(type);
        TYPE_TO_CATEGORIES.put(type, categories);
        RecipeBookTypeAccessor.setValues(values.toArray(RecipeBookType[]::new));
        return type;
    }
}
