package net.chikorita_lover.chicory.registry.tag;

import net.chikorita_lover.chicory.ChicoryApi;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public final class ChicoryBlockTags {
    /**
     * Blocks that can be used in the frame around a conduit to activate it.
     */
    public static final TagKey<Block> CONDUIT_ACTIVATING_BLOCKS = of("conduit_activating_blocks");
    /**
     * Blocks that can be placed beneath a campfire to create a signal fire.
     * Removing the hay bale from this block tag does not have any effect.
     */
    public static final TagKey<Block> SIGNAL_FIRE_BASE_BLOCKS = of("signal_fire_base_blocks");
    public static final TagKey<Block> SHEARS_MINEABLE = of("mineable/shears");

    private static TagKey<Block> of(String path) {
        return TagKey.of(RegistryKeys.BLOCK, ChicoryApi.of(path));
    }
}
