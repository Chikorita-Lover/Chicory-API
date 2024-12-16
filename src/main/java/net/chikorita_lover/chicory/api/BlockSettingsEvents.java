package net.chikorita_lover.chicory.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BlockSettingsEvents {
    /**
     * Event used to modify settings of known blocks.
     */
    public static final Event<ModifyCallback> MODIFY = EventFactory.createArrayBacked(ModifyCallback.class, listeners -> context -> {
        for (ModifyCallback listener : listeners) {
            listener.modify(context);
        }
    });

    public interface ModifyContext {
        /**
         * Modify the settings of any block matching the specified predicate.
         *
         * @param blockPredicate a predicate to match blocks to modify
         * @param consumer a consumer that provides {@link AbstractBlock.Settings} to modify the blocks's settings
         */
        void modify(Predicate<Block> blockPredicate, BiConsumer<AbstractBlock.Settings, Block> consumer);

        /**
         * Modify the settings of the specified block.
         *
         * @param block the block to modify
         * @param consumer a consumer that provides an {@link AbstractBlock.Settings} instance to modify the block's settings
         */
        default void modify(Block block, final Consumer<AbstractBlock.Settings> consumer) {
            this.modify(Predicate.isEqual(block), (builder, blockx) -> consumer.accept(builder));
        }

        /**
         * Modify the settings of the specified blocks.
         *
         * @param blocks the blocks to modify
         * @param consumer a consumer that provides an {@link AbstractBlock.Settings} instance to modify the block's settings
         */
        default void modify(Collection<Block> blocks, BiConsumer<AbstractBlock.Settings, Block> consumer) {
            this.modify(blocks::contains, consumer);
        }
    }

    @FunctionalInterface
    public interface ModifyCallback {
        /**
         * Modify the settings of blocks using the provided {@link ModifyContext} instance.
         *
         * @param context the context to modify blocks
         */
        void modify(ModifyContext context);
    }
}
