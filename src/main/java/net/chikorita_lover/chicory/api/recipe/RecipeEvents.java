package net.chikorita_lover.chicory.api.recipe;

import net.chikorita_lover.chicory.ChicoryApi;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.List;

public final class RecipeEvents {
    /**
     * The identifier for the recipe-dependent phase.
     * Events registered to said phase will run after events registered to the default phase and are designed to add dynamically-created recipes based on other existing recipes.
     */
    public static final Identifier RECIPE_DEPENDENT_PHASE = ChicoryApi.of("recipe_dependent");
    /**
     * The identifier for the final processing phase.
     * Events registered to said phase will run after events registered to {@link #RECIPE_DEPENDENT_PHASE} and are for absolute final processing.
     */
    public static final Identifier FINAL_PROCESSING_PHASE = ChicoryApi.of("final_processing");
    /**
     * This event can be used for post-processing after all recipes have been loaded and modified by Chicory API.
     */
    public static final Event<Loaded> ALL_LOADED = EventFactory.createWithPhases(Loaded.class, listeners -> (resourceManager, entries, registries) -> {
        for (Loaded listener : listeners) {
            listener.onRecipesLoaded(resourceManager, entries, registries);
        }
    }, Event.DEFAULT_PHASE, RECIPE_DEPENDENT_PHASE, FINAL_PROCESSING_PHASE);

    @FunctionalInterface
    public interface Loaded {
        /**
         * Called when all recipes have been loaded.
         */
        void onRecipesLoaded(ResourceManager resourceManager, List<RecipeEntry<?>> entries, RegistryWrapper.WrapperLookup registries);
    }
}
