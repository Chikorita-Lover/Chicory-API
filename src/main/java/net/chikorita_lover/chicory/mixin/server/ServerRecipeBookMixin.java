package net.chikorita_lover.chicory.mixin.server;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.chikorita_lover.chicory.api.recipe.RecipeBookTypeRegistry;
import net.chikorita_lover.chicory.api.resource.ToggleableFeatureRegistry;
import net.chikorita_lover.chicory.network.ChicoryRecipeBookSettingsS2CPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.book.RecipeBook;
import net.minecraft.recipe.book.RecipeBookOptions;
import net.minecraft.recipe.book.RecipeBookType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerRecipeBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(ServerRecipeBook.class)
public class ServerRecipeBookMixin extends RecipeBook {
    @ModifyExpressionValue(method = "unlockRecipes", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Recipe;isIgnoredInRecipeBook()Z"))
    private boolean isRecipeNotUnlockable(boolean ignored, @Local(argsOnly = true) ServerPlayerEntity player, @Local RecipeEntry<?> recipe) {
        return ignored || ToggleableFeatureRegistry.isRecipeDisabled(recipe);
    }

    @Inject(method = "sendInitRecipesPacket", at = @At("TAIL"))
    private void sendChicoryOptions(ServerPlayerEntity player, CallbackInfo ci) {
        Map<RecipeBookType, RecipeBookOptions.CategoryOption> options = new HashMap<>();
        for (RecipeBookType category : RecipeBookTypeRegistry.TYPES) {
            options.put(category, this.getOptions().getOption(category));
        }
        ServerPlayNetworking.send(player, new ChicoryRecipeBookSettingsS2CPacket(options));
    }
}
