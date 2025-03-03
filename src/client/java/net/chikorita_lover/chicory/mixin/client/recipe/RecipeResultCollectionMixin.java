package net.chikorita_lover.chicory.mixin.client.recipe;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.chikorita_lover.chicory.api.resource.ToggleableFeatureRegistry;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RecipeResultCollection.class)
public abstract class RecipeResultCollectionMixin {
    @Shadow
    public abstract DynamicRegistryManager getRegistryManager();

    @ModifyExpressionValue(method = "computeCraftables", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/book/RecipeBook;contains(Lnet/minecraft/recipe/RecipeEntry;)Z"))
    private boolean canDisplayRecipe(boolean contained, @Local RecipeEntry<?> recipeEntry) {
        return contained && ToggleableFeatureRegistry.isRecipeEnabled(recipeEntry.value(), this.getRegistryManager());
    }
}
