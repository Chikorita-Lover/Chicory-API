package net.chikorita_lover.chicory.mixin.client.render;

import net.chikorita_lover.chicory.api.render.SkullBlockModelRegistry;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(SkullBlockEntityRenderer.class)
public class SkullBlockEntityRendererMixin {
    @Shadow
    @Final
    private static Map<SkullBlock.SkullType, Identifier> TEXTURES;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void putChicoryTextures(CallbackInfo ci) {
        for (SkullBlock.Type type : SkullBlockModelRegistry.getTypes()) {
            TEXTURES.put(type, SkullBlockModelRegistry.getTexture(type));
        }
    }

    @Inject(method = "getModels", at = @At("HEAD"), cancellable = true)
    private static void getChicoryModel(LoadedEntityModels models, SkullBlock.SkullType type, CallbackInfoReturnable<SkullBlockEntityModel> cir) {
        if (type instanceof SkullBlock.Type skullType && SkullBlockModelRegistry.getTypes().contains(type)) {
            cir.setReturnValue(SkullBlockModelRegistry.createModel(skullType, models));
        }
    }
}
