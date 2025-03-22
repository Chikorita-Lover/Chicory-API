package net.chikorita_lover.chicory.api.resource;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.featuretoggle.ToggleableFeature;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.function.Supplier;

public final class ToggleableFeatureRegistry {
    private static final Map<ToggleableFeature, List<Supplier<Boolean>>> FEATURE_TO_REQUIRED_MODS = new HashMap<>();
    private static final List<Identifier> DISABLED_RECIPES = new ArrayList<>();

    /**
     * Adds a {@code condition} that must be passed for the specified {@code feature} to be enabled in-game.
     *
     * @param feature   the feature to make toggleable
     * @param condition the condition that must be passed to be enabled
     */
    public static void add(ToggleableFeature feature, Supplier<Boolean> condition) {
        FEATURE_TO_REQUIRED_MODS.computeIfAbsent(feature, ignored -> new ArrayList<>()).add(condition);
    }

    /**
     * Requires that a mod with the specified {@code namespace} is loaded for the specified {@code feature} to be enabled.
     *
     * @param feature   the feature to make toggleable
     * @param namespace the namespace of the required mod
     */
    public static void requireMod(ToggleableFeature feature, final String namespace) {
        add(feature, () -> FabricLoader.getInstance().isModLoaded(namespace));
    }

    /**
     * Completely disables the specified {@code feature}.
     *
     * @param feature the feature to disable
     */
    public static void disable(ToggleableFeature feature) {
        add(feature, () -> false);
    }

    /**
     * Completely disables the recipe with the specified {@code id}, making it unusable and hiding it from recipe books.
     *
     * @param id the identifier of the recipe to disable
     */
    public static void disable(Identifier id) {
        DISABLED_RECIPES.add(id);
    }

    @ApiStatus.Internal
    public static boolean isEnabled(ToggleableFeature feature) {
        if (!FEATURE_TO_REQUIRED_MODS.containsKey(feature)) {
            return true;
        }
        return FEATURE_TO_REQUIRED_MODS.get(feature).stream().allMatch(Supplier::get);
    }

    @ApiStatus.Internal
    public static boolean isRecipeEnabled(RecipeEntry<?> entry, RegistryWrapper.WrapperLookup registries) {
        Recipe<?> recipe = entry.value();
        return !DISABLED_RECIPES.contains(entry.id()) && isEnabled(recipe.getResult(registries).getItem()) && recipe.getIngredients().stream().allMatch(ingredient -> ingredient.isEmpty() || Arrays.stream(ingredient.getMatchingStacks()).anyMatch(stack -> isEnabled(stack.getItem())));
    }
}
