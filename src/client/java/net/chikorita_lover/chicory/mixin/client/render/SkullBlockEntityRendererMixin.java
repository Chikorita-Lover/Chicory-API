package net.chikorita_lover.chicory.mixin.client.render;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import net.chikorita_lover.chicory.api.render.SkullBlockModelRegistry;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(SkullBlockEntityRenderer.class)
public class SkullBlockEntityRendererMixin {
    @Shadow
    @Final
    private static Map<SkullBlock.SkullType, Identifier> TEXTURES;

    @ModifyReceiver(method = "getModels", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"))
    private static ImmutableMap.Builder<SkullBlock.Type, SkullBlockEntityModel> putChicoryModels(ImmutableMap.Builder<SkullBlock.Type, SkullBlockEntityModel> builder, EntityModelLoader modelLoader) {
        for (SkullBlock.Type type : SkullBlockModelRegistry.getTypes()) {
            TEXTURES.put(type, SkullBlockModelRegistry.getTexture(type));
            builder.put(type, SkullBlockModelRegistry.createModel(type, modelLoader));
        }
        return builder;
    }
}
