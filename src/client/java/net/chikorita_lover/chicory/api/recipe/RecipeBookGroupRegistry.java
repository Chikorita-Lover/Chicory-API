package net.chikorita_lover.chicory.api.recipe;

import net.chikorita_lover.chicory.mixin.client.recipe.RecipeBookGroupAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.content.registry.util.ImmutableCollectionUtils;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.book.RecipeBookCategory;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

/**
 * A registry for creating and modifying recipe book groups.
 */
@Environment(EnvType.CLIENT)
public final class RecipeBookGroupRegistry {
    private static final Map<RecipeBookCategory, List<RecipeBookGroup>> RECIPE_CATEGORY_GROUPS = new HashMap<>();

    /**
     * Creates and returns a new recipe book group.
     *
     * @param name    the internal name
     * @param entries one or more {@link ItemStack} to display for the recipe book group's icon
     * @return the new recipe book group
     */
    @SuppressWarnings("UnreachableCode")
    public static RecipeBookGroup register(String name, ItemStack... entries) {
        List<RecipeBookGroup> values = new ArrayList<>(Arrays.asList(RecipeBookGroupAccessor.getValues()));
        int ordinal = values.size();
        RecipeBookGroup group = RecipeBookGroupAccessor.create(name.toUpperCase(), ordinal, entries);
        values.add(group);
        RecipeBookGroupAccessor.setValues(values.toArray(RecipeBookGroup[]::new));
        return group;
    }

    /**
     * Assigns the provided recipe book groups to the provided recipe book category.
     * <p>If the recipe book category does not yet have a search group, the first group assigned to the category will be set as the search group.
     *
     * @param category the recipe book category
     * @param groups   recipe book groups to assign to {@code category}
     */
    public static void addGroups(RecipeBookCategory category, RecipeBookGroup... groups) {
        List<RecipeBookGroup> list = getGroups(category);
        list.addAll(Arrays.asList(groups));
        getSearchMap().put(list.getFirst(), list.subList(1, list.size()));
    }

    @ApiStatus.Internal
    public static boolean contains(RecipeBookCategory category) {
        return RECIPE_CATEGORY_GROUPS.containsKey(category);
    }

    @ApiStatus.Internal
    public static List<RecipeBookGroup> getGroups(RecipeBookCategory category) {
        return RECIPE_CATEGORY_GROUPS.computeIfAbsent(category, _category -> new ArrayList<>(RecipeBookGroup.getGroups(_category)));
    }

    @SuppressWarnings("UnstableApiUsage")
    private static Map<RecipeBookGroup, List<RecipeBookGroup>> getSearchMap() {
        return ImmutableCollectionUtils.getAsMutableMap(RecipeBookGroupAccessor::getSearchMap, RecipeBookGroupAccessor::setSearchMap);
    }
}
