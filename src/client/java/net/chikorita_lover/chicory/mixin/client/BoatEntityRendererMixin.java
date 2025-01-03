package net.chikorita_lover.chicory.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.chikorita_lover.chicory.api.entity.BoatTypeRegistry;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BoatEntityRenderer.class)
public class BoatEntityRendererMixin {
    @ModifyReturnValue(method = "getTexture(Lnet/minecraft/entity/vehicle/BoatEntity$Type;Z)Lnet/minecraft/util/Identifier;", at = @At("RETURN"))
    private static Identifier modifyTexture(Identifier texture, BoatEntity.Type type, boolean chest) {
        if (!BoatTypeRegistry.contains(type)) {
            return texture;
        }
        Identifier id = BoatTypeRegistry.getId(type);
        String dir = chest ? "chest_boat" : "boat";
        return Identifier.of(id.getNamespace(), "textures/entity/" + dir + '/' + id.getPath() + ".png");
    }
}
