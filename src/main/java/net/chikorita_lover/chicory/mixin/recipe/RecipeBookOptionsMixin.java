package net.chikorita_lover.chicory.mixin.recipe;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.chikorita_lover.chicory.api.recipe.RecipeBookTypeRegistry;
import net.chikorita_lover.chicory.impl.recipe.ChicoryRecipeBookOptionsHolder;
import net.minecraft.recipe.book.RecipeBookOptions;
import net.minecraft.recipe.book.RecipeBookType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

@Mixin(RecipeBookOptions.class)
public class RecipeBookOptionsMixin implements ChicoryRecipeBookOptionsHolder {
    @Unique
    private final Map<RecipeBookType, RecipeBookOptions.CategoryOption> categoryOptions = new HashMap<>();

    @Inject(method = "getOption", at = @At("HEAD"), cancellable = true)
    private void getChicoryOption(RecipeBookType type, CallbackInfoReturnable<RecipeBookOptions.CategoryOption> cir) {
        if (!RecipeBookTypeRegistry.TYPES.contains(type)) {
            return;
        }
        cir.setReturnValue(this.chicory$getOption(type));
    }

    @Inject(method = "apply", at = @At("HEAD"))
    private void applyChicory(RecipeBookType type, UnaryOperator<RecipeBookOptions.CategoryOption> modifier, CallbackInfo ci) {
        if (!RecipeBookTypeRegistry.TYPES.contains(type)) {
            return;
        }
        this.chicory$setOption(type, modifier.apply(this.chicory$getOption(type)));
    }

    @ModifyReturnValue(method = "copy", at = @At("RETURN"))
    private RecipeBookOptions copyChicoryOptions(RecipeBookOptions original) {
        this.categoryOptions.forEach((type, option) -> ((ChicoryRecipeBookOptionsHolder) (Object) original).chicory$setOption(type, option));
        return original;
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void copyChicoryOptionsFrom(RecipeBookOptions other, CallbackInfo ci) {
        for (RecipeBookType type : RecipeBookTypeRegistry.TYPES) {
            this.chicory$setOption(type, ((ChicoryRecipeBookOptionsHolder) (Object) other).chicory$getOption(type));
        }
    }

    @Override
    public RecipeBookOptions.CategoryOption chicory$getOption(RecipeBookType type) {
        if (!RecipeBookTypeRegistry.TYPES.contains(type)) {
            return null;
        }
        return this.categoryOptions.computeIfAbsent(type, typex -> RecipeBookOptions.CategoryOption.DEFAULT);
    }

    @Override
    public void chicory$setOption(RecipeBookType type, RecipeBookOptions.CategoryOption option) {
        if (!RecipeBookTypeRegistry.TYPES.contains(type)) {
            return;
        }
        this.categoryOptions.put(type, option);
    }
}
