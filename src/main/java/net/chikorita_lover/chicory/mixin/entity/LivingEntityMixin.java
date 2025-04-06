package net.chikorita_lover.chicory.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.chikorita_lover.chicory.api.block.SkullTypeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(method = "dropEquipment", at = @At("TAIL"))
    private void dropHead(ServerWorld world, DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        EntityType<?> type = this.getType();
        if (source.getAttacker() instanceof CreeperEntity creeper && creeper.shouldDropHead() && SkullTypeRegistry.hasSkull(type)) {
            creeper.onHeadDropped();
            this.dropStack(new ItemStack(SkullTypeRegistry.getSkull(type)));
        }
    }
}
