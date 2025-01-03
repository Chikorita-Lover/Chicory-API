package net.chikorita_lover.chicory.api.item;

import net.chikorita_lover.chicory.mixin.block.DecoratedPotPatternsAccessor;
import net.fabricmc.fabric.impl.content.registry.util.ImmutableCollectionUtils;
import net.minecraft.block.DecoratedPotPattern;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * A registry for pairing decorated pot patterns with items.
 */
public final class PotterySherdsRegistry {
    /**
     * Registers an item and decorated pot pattern pair.
     *
     * @param item    the pottery sherd item
     * @param pattern the decorated pot pattern
     */
    public static void registerPotterySherdItem(@NotNull Item item, @NotNull RegistryKey<DecoratedPotPattern> pattern) {
        getRegistry().put(item, pattern);
    }

    private static Map<Item, RegistryKey<DecoratedPotPattern>> getRegistry() {
        return ImmutableCollectionUtils.getAsMutableMap(DecoratedPotPatternsAccessor::getPotterySherds, DecoratedPotPatternsAccessor::setPotterySherds);
    }
}
