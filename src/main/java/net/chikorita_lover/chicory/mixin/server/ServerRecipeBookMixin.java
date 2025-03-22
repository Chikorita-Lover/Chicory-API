package net.chikorita_lover.chicory.mixin.server;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.chikorita_lover.chicory.api.resource.ToggleableFeatureRegistry;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.book.RecipeBook;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerRecipeBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerRecipeBook.class)
public class ServerRecipeBookMixin extends RecipeBook {
    @ModifyExpressionValue(method = "unlockRecipes", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Recipe;isIgnoredInRecipeBook()Z"))
    private boolean isRecipeNotUnlockable(boolean ignored, @Local(argsOnly = true) ServerPlayerEntity player, @Local RecipeEntry<?> recipe) {
        return ignored || !ToggleableFeatureRegistry.isRecipeEnabled(recipe, player.getRegistryManager());
    }
}
