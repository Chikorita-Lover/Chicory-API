package net.chikorita_lover.chicory.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.chikorita_lover.chicory.api.resource.ToggleableFeatureRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.village.TradeOffer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MerchantEntity.class)
public abstract class MerchantEntityMixin extends PassiveEntity {
    protected MerchantEntityMixin(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(method = "fillRecipesFromPool", at = @At(value = "INVOKE", target = "Lnet/minecraft/village/TradeOffers$Factory;create(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/random/Random;)Lnet/minecraft/village/TradeOffer;"))
    private TradeOffer modifyTradeOffer(TradeOffer trade) {
        if (!ToggleableFeatureRegistry.isEnabled(trade.getDisplayedFirstBuyItem().getItem()) || !ToggleableFeatureRegistry.isEnabled(trade.getDisplayedSecondBuyItem().getItem()) || !ToggleableFeatureRegistry.isEnabled(trade.getSellItem().getItem())) {
            return null;
        }
        return trade;
    }
}
