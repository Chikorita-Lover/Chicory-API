package net.chikorita_lover.chicory.api.recipe;

import com.mojang.datafixers.util.Pair;
import net.chikorita_lover.chicory.mixin.recipe.RecipeBookTypeAccessor;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.book.RecipeBookType;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * A registry for creating recipe book types.
 */
public final class RecipeBookTypeRegistry {
    public static final List<RecipeBookType> TYPES = new ArrayList<>();
    private static BiConsumer<RecipeBookType, RecipeBookCategory[]> clientRegistryConsumer;

    /**
     * Creates and returns a new recipe book type.
     *
     * @param name the internal name
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
        RecipeBookTypeAccessor.setValues(values.toArray(RecipeBookType[]::new));
        clientRegistryConsumer.accept(type, categories);
        return type;
    }

    @ApiStatus.Internal
    public static void setClientRegistryConsumer(BiConsumer<RecipeBookType, RecipeBookCategory[]> consumer) {
        if (clientRegistryConsumer == null) {
            clientRegistryConsumer = consumer;
        }
    }
}
