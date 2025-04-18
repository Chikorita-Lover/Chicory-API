package net.chikorita_lover.chicory.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.chikorita_lover.chicory.api.entity.BoatTypeRegistry;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin {
    @Shadow
    public abstract BoatEntity.Type getVariant();

    @ModifyReturnValue(method = "asItem", at = @At("RETURN"))
    private Item modifyItem(Item item) {
        BoatEntity.Type type = this.getVariant();
        return BoatTypeRegistry.contains(type) ? BoatTypeRegistry.getBoatItem(false, type) : item;
    }
}
