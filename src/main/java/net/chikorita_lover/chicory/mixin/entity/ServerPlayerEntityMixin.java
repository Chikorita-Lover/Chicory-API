package net.chikorita_lover.chicory.mixin.entity;

import net.chikorita_lover.chicory.impl.recipe.ChicoryRecipeBookHolder;
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

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Shadow
    @Final
    private ServerRecipeBook recipeBook;

    @Inject(method = "readCustomData", at = @At("TAIL"))
    private void readChicoryRecipeBook(ReadView view, CallbackInfo ci) {
        ((ChicoryRecipeBookHolder) this.recipeBook).chicory$getOptions().readData(view);
    }

    @Inject(method = "writeCustomData", at = @At("TAIL"))
    private void writeChicoryRecipeBook(WriteView view, CallbackInfo ci) {
        ((ChicoryRecipeBookHolder) this.recipeBook).chicory$getOptions().writeData(view);
    }
}
