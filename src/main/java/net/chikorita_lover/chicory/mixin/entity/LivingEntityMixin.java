package net.chikorita_lover.chicory.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.chikorita_lover.chicory.api.block.SkullTypeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    @ModifyReturnValue(method = "getAttackDistanceScalingFactor", at = @At("RETURN"))
    private double modifyAttackDistanceScalingFactor(double factor) {
        ItemStack stack = this.getEquippedStack(EquipmentSlot.HEAD);
        if (SkullTypeRegistry.hasSkull(this.getType()) && stack.isOf(SkullTypeRegistry.getSkull(this.getType()))) {
            factor *= 0.5;
        }
        return factor;
    }
}
