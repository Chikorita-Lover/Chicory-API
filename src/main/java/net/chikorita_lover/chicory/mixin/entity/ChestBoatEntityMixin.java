package net.chikorita_lover.chicory.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.chikorita_lover.chicory.api.BoatTypeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChestBoatEntity.class)
public abstract class ChestBoatEntityMixin extends BoatEntity {
    public ChestBoatEntityMixin(EntityType<? extends BoatEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyReturnValue(method = "asItem", at = @At("RETURN"))
    private Item modifyItem(Item item) {
        BoatEntity.Type type = this.getVariant();
        return BoatTypeRegistry.contains(type) ? BoatTypeRegistry.getBoatItem(true, type) : item;
    }
}
