package net.chikorita_lover.chicory.api.recipe;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;

/**
 * Events for manipulating recipes in the client environment.
 */
public final class ClientRecipeEvents {
    /**
     * An event that allows the recipe book group of a recipe to be modified.
     * <p>If {@code null} is returned rather than a recipe book group, the event will pass without modifying the recipe book group.
     */
    public static final Event<GroupRecipe> GROUP_RECIPE = EventFactory.createArrayBacked(GroupRecipe.class, callbacks -> recipe -> {
        for (GroupRecipe callback : callbacks) {
            RecipeBookGroup group = callback.groupRecipe(recipe);
            if (group != null) {
                return group;
            }
        }
        return null;
    });

    @FunctionalInterface
    public interface GroupRecipe {
        /**
         * {@return the group to assign to the provided recipe, or {@code null} to pass the behavior}
         *
         * @param recipe the recipe to group
         */
        RecipeBookGroup groupRecipe(Recipe<?> recipe);
    }
}
