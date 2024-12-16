package net.chikorita_lover.chicory.impl;

import net.chikorita_lover.chicory.api.BlockSettingsEvents;
import net.chikorita_lover.chicory.api.BlockSettingsHolder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class BlockSettingsImpl {
    public static void modifyBlockSettings() {
        BlockSettingsEvents.MODIFY.invoker().modify(ModifyContextImpl.INSTANCE);
    }

    public static class ModifyContextImpl implements BlockSettingsEvents.ModifyContext {
        public static final ModifyContextImpl INSTANCE = new ModifyContextImpl();

        @Override
        public void modify(Predicate<Block> blockPredicate, BiConsumer<AbstractBlock.Settings, Block> consumer) {
            for (Block block : Registries.BLOCK) {
                if (blockPredicate.test(block)) {
                    AbstractBlock.Settings settings = block.getSettings();
                    consumer.accept(settings, block);
                    ((BlockSettingsHolder) block).chicory$updateSettings();
                    block.getStateManager().getStates().forEach(state -> ((BlockSettingsHolder) state).chicory$updateSettings());
                }
            }
        }
    }
}
