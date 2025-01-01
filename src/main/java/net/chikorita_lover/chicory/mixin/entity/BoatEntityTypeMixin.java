package net.chikorita_lover.chicory.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;
import java.util.Objects;

@Mixin(BoatEntity.Type.class)
public abstract class BoatEntityTypeMixin {
    @Final
    @Shadow
    public static BoatEntity.Type OAK;
    @Shadow
    @Final
    @Mutable
    private static BoatEntity.Type[] field_7724;

    @ModifyReturnValue(method = "getType(I)Lnet/minecraft/entity/vehicle/BoatEntity$Type;", at = @At("RETURN"))
    private static BoatEntity.Type getTypeById(BoatEntity.Type ignored, int type) {
        return field_7724[type];
    }

    @ModifyReturnValue(method = "getType(Ljava/lang/String;)Lnet/minecraft/entity/vehicle/BoatEntity$Type;", at = @At("RETURN"))
    private static BoatEntity.Type getTypeByName(BoatEntity.Type ignored, final String name) {
        return Arrays.stream(field_7724).filter(type -> Objects.equals(type.getName(), name)).findFirst().orElse(OAK);
    }
}
