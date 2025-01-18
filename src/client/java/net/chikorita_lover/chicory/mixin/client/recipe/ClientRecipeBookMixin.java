package net.chikorita_lover.chicory.mixin.client.recipe;

import net.chikorita_lover.chicory.api.recipe.ClientRecipeEvents;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.RecipeEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    @Inject(method = "getGroupForRecipe", at = @At("HEAD"), cancellable = true)
    private static void modifyGroupForRecipe(RecipeEntry<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir) {
        RecipeBookGroup group = ClientRecipeEvents.GROUP_RECIPE.invoker().groupRecipe(recipe.value());
        if (group != null) {
            cir.setReturnValue(group);
        }
    }
}
