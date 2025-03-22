package net.chikorita_lover.chicory.mixin.recipe;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.chikorita_lover.chicory.ChicoryApi;
import net.chikorita_lover.chicory.api.recipe.RecipeEvents;
import net.chikorita_lover.chicory.api.resource.ToggleableFeatureRegistry;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Shadow
    private Multimap<RecipeType<?>, RecipeEntry<?>> recipesByType;

    @Shadow
    @Final
    private RegistryWrapper.WrapperLookup registryLookup;

    @Shadow
    private Map<Identifier, RecipeEntry<?>> recipesById;

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At("TAIL"))
    private void onRecipesLoaded(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
        List<RecipeEntry<?>> entries = new ArrayList<>(this.recipesByType.values());
        RecipeEvents.ALL_LOADED.invoker().onRecipesLoaded(resourceManager, entries, this.registryLookup);
        ImmutableMultimap.Builder<RecipeType<?>, RecipeEntry<?>> recipesByType = ImmutableMultimap.builder();
        ImmutableMap.Builder<Identifier, RecipeEntry<?>> recipesById = ImmutableMap.builder();
        for (RecipeEntry<?> entry : entries) {
            try {
                recipesByType.put(entry.value().getType(), entry);
                recipesById.put(entry.id(), entry);
            } catch (Exception exception) {
                ChicoryApi.LOGGER.error("Error loading recipe {}: {}", entry.id(), exception.getMessage());
            }
        }
        this.recipesByType = recipesByType.build();
        this.recipesById = recipesById.build();
    }

    @ModifyReturnValue(method = "getAllOfType", at = @At("RETURN"))
    private <I extends RecipeInput, T extends Recipe<I>> Collection<RecipeEntry<T>> getAllEnabled(Collection<RecipeEntry<T>> recipes) {
        return recipes.stream().filter(recipe -> ToggleableFeatureRegistry.isRecipeEnabled(recipe, this.registryLookup)).toList();
    }
}
