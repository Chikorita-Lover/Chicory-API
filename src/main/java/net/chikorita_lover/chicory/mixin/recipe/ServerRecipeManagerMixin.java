package net.chikorita_lover.chicory.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import net.chikorita_lover.chicory.api.recipe.RecipeEvents;
import net.chikorita_lover.chicory.api.resource.ToggleableFeatureRegistry;
import net.minecraft.recipe.PreparedRecipes;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ServerRecipeManager.class)
public class ServerRecipeManagerMixin {
    @Shadow
    @Final
    private RegistryWrapper.WrapperLookup registries;

    @Inject(method = "prepare(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)Lnet/minecraft/recipe/PreparedRecipes;", at = @At(value = "INVOKE", target = "Ljava/util/SortedMap;forEach(Ljava/util/function/BiConsumer;)V", shift = At.Shift.AFTER))
    private void onRecipesLoaded(ResourceManager resourceManager, Profiler profiler, CallbackInfoReturnable<PreparedRecipes> cir, @Local List<RecipeEntry<Recipe<?>>> recipes) {
        RecipeEvents.ALL_LOADED.invoker().onRecipesLoaded(resourceManager, recipes, this.registries);
        recipes.removeIf(ToggleableFeatureRegistry::isRecipeDisabled);
    }
}
