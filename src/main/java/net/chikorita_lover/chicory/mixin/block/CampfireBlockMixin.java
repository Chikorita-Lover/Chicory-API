package net.chikorita_lover.chicory.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.chikorita_lover.chicory.registry.tag.ChicoryBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {
    @ModifyReturnValue(method = "isSignalFireBaseBlock", at = @At("RETURN"))
    private boolean isSignalFireBaseBlock(boolean signalFireBaseBlock, BlockState state) {
        return state.isIn(ChicoryBlockTags.SIGNAL_FIRE_BASE_BLOCKS) || signalFireBaseBlock;
    }
}
