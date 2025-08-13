package net.chikorita_lover.chicory.mixin.entity;

import net.chikorita_lover.chicory.impl.recipe.ChicoryRecipeBookOptionsHolder;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerRecipeBook;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Shadow
    @Final
    private ServerRecipeBook recipeBook;

    @Inject(method = "readCustomData", at = @At("TAIL"))
    private void readChicoryData(ReadView view, CallbackInfo ci) {
        Optional<ReadView> recipeView = view.getOptionalReadView("chicory_recipe_book");
        if (recipeView.isEmpty()) {
            return;
        }
        ((ChicoryRecipeBookOptionsHolder) (Object) this.recipeBook.getOptions()).chicory$readData(recipeView.get());
    }

    @Inject(method = "writeCustomData", at = @At("TAIL"))
    private void writeChicoryData(WriteView view, CallbackInfo ci) {
        ((ChicoryRecipeBookOptionsHolder) (Object) this.recipeBook.getOptions()).chicory$writeData(view.get("chicory_recipe_book"));
    }
}
