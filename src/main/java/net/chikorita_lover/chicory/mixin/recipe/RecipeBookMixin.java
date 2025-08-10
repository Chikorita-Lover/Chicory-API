package net.chikorita_lover.chicory.mixin.recipe;

import net.chikorita_lover.chicory.api.recipe.RecipeBookTypeRegistry;
import net.chikorita_lover.chicory.impl.recipe.ChicoryRecipeBookHolder;
import net.chikorita_lover.chicory.impl.recipe.ChicoryRecipeBookOptions;
import net.minecraft.recipe.book.RecipeBook;
import net.minecraft.recipe.book.RecipeBookType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecipeBook.class)
public class RecipeBookMixin implements ChicoryRecipeBookHolder {
    @Unique
    private final ChicoryRecipeBookOptions chicoryOptions = new ChicoryRecipeBookOptions();

    @Inject(method = "isGuiOpen", at = @At("HEAD"), cancellable = true)
    private void isChicoryGuiOpen(RecipeBookType category, CallbackInfoReturnable<Boolean> cir) {
        if (RecipeBookTypeRegistry.TYPES.contains(category)) {
            cir.setReturnValue(this.chicoryOptions.isGuiOpen(category));
        }
    }

    @Inject(method = "setGuiOpen", at = @At("HEAD"))
    private void setChicoryGuiOpen(RecipeBookType category, boolean open, CallbackInfo ci) {
        if (RecipeBookTypeRegistry.TYPES.contains(category)) {
            this.chicoryOptions.setGuiOpen(category, open);
        }
    }

    @Inject(method = "isFilteringCraftable", at = @At("HEAD"), cancellable = true)
    private void isChicoryFilteringCraftable(RecipeBookType category, CallbackInfoReturnable<Boolean> cir) {
        if (RecipeBookTypeRegistry.TYPES.contains(category)) {
            cir.setReturnValue(this.chicoryOptions.isFilteringCraftable(category));
        }
    }

    @Inject(method = "setFilteringCraftable", at = @At("HEAD"))
    private void setChicoryFilteringCraftable(RecipeBookType category, boolean filtering, CallbackInfo ci) {
        if (RecipeBookTypeRegistry.TYPES.contains(category)) {
            this.chicoryOptions.setFilteringCraftable(category, filtering);
        }
    }

    @Override
    public ChicoryRecipeBookOptions chicory$getOptions() {
        return this.chicoryOptions;
    }
}
