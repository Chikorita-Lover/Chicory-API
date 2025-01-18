package net.chikorita_lover.chicory.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.chikorita_lover.chicory.api.entity.MinecartTypeRegistry;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin {
    @ModifyVariable(method = "create", at = @At("STORE"))
    private static AbstractMinecartEntity createChicoryMinecart(AbstractMinecartEntity minecart, ServerWorld world, double x, double y, double z, AbstractMinecartEntity.Type type) {
        if (MinecartTypeRegistry.contains(type)) {
            minecart = MinecartTypeRegistry.getFactory(type).create(world, x, y, z);
        }
        return minecart;
    }

    @Shadow
    public abstract AbstractMinecartEntity.Type getMinecartType();

    @ModifyReturnValue(method = "getPickBlockStack", at = @At("RETURN"))
    private ItemStack modifyPickBlockStack(ItemStack stack) {
        AbstractMinecartEntity.Type type = this.getMinecartType();
        return MinecartTypeRegistry.contains(type) ? new ItemStack(MinecartTypeRegistry.getItem(type)) : stack;
    }
}
