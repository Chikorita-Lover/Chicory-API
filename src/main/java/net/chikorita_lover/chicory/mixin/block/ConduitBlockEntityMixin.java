package net.chikorita_lover.chicory.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.chikorita_lover.chicory.registry.tag.ChicoryBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ConduitBlockEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(ConduitBlockEntity.class)
public class ConduitBlockEntityMixin extends BlockEntity {
    @Mutable
    @Shadow
    @Final
    private static Block[] ACTIVATING_BLOCKS;

    public ConduitBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clearActivatingBlocks(CallbackInfo ci) {
        List<Block> activatingBlocks = Arrays.stream(ACTIVATING_BLOCKS).collect(Collectors.toCollection(ArrayList::new));
        activatingBlocks.removeAll(List.of(Blocks.PRISMARINE, Blocks.PRISMARINE_BRICKS, Blocks.SEA_LANTERN, Blocks.DARK_PRISMARINE));
        ACTIVATING_BLOCKS = activatingBlocks.toArray(Block[]::new);
    }

    @ModifyExpressionValue(method = "updateActivatingBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
    private static BlockState checkForActivatingBlock(BlockState state, @Local(argsOnly = true) List<BlockPos> activatingBlocks, @Local(ordinal = 1) BlockPos blockPos2) {
        if (state.isIn(ChicoryBlockTags.CONDUIT_ACTIVATING_BLOCKS) || Arrays.stream(ACTIVATING_BLOCKS).toList().contains(state.getBlock())) {
            activatingBlocks.add(blockPos2);
        }
        return state;
    }

    @ModifyExpressionValue(method = "updateActivatingBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    private static boolean canAddActivatingBlock(boolean isOf, @Local(argsOnly = true) List<BlockPos> activatingBlocks, @Local(ordinal = 1) BlockPos blockPos2) {
        return isOf && !activatingBlocks.contains(blockPos2);
    }
}
