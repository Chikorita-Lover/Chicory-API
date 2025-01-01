package net.chikorita_lover.chicory.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.chikorita_lover.chicory.block.BeeGrowable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.entity.passive.BeeEntity$GrowCropsGoal")
public class GrowCropsGoalMixin {
    @Shadow
    @Final
    BeeEntity field_20373;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z", shift = At.Shift.AFTER))
    public void tryGrowByBee(CallbackInfo ci, @Local int i, @Local BlockPos blockPos, @Local(ordinal = 0) BlockState blockState, @Local Block block, @Local(ordinal = 1) LocalRef<BlockState> blockState2) {
        World world = this.field_20373.getWorld();
        if (world instanceof ServerWorld serverWorld && block instanceof BeeGrowable beeGrowable) {
            blockState2.set(beeGrowable.getBeeGrownState(serverWorld, this.field_20373.getRandom(), blockPos, blockState, this.field_20373));
        }
    }
}
