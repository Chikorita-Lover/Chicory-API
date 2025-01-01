package net.chikorita_lover.chicory.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

/**
 * A block interface that allows a block to be grown by bees that have already pollinated a flower.
 * The only other requirement for a block to be grown by a bee is being in the {@link net.minecraft.registry.tag.BlockTags#BEE_GROWABLES} tag.
 * <p>Blocks that instantiate off of the {@link net.minecraft.block.CropBlock} or {@link net.minecraft.block.StemBlock} classes already receive this functionality through the vanilla method.
 */
public interface BeeGrowable {
    /**
     * Determines the block state that the provided {@code state} should become as a result of being grown by {@code bee}.
     * <p>This method should not set the block state in {@code world}; this is handled by the API.
     *
     * @return the new block state as a result of the interaction, or {@code null} if the block should not be modified (e.g. it is already fully grown)
     */
    @Nullable
    BlockState getBeeGrownState(ServerWorld world, Random random, BlockPos pos, BlockState state, BeeEntity bee);
}
