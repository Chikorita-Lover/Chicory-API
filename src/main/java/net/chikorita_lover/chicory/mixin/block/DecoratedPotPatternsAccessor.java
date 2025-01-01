package net.chikorita_lover.chicory.mixin.block;

import net.minecraft.block.DecoratedPotPattern;
import net.minecraft.block.DecoratedPotPatterns;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(DecoratedPotPatterns.class)
public interface DecoratedPotPatternsAccessor {
    @Accessor("SHERD_TO_PATTERN")
    static Map<Item, RegistryKey<DecoratedPotPattern>> getPotterySherds() {
        throw new AssertionError("Untransformed @Accessor");
    }

    @Accessor("SHERD_TO_PATTERN")
    @Mutable
    static void setPotterySherds(Map<Item, RegistryKey<DecoratedPotPattern>> potterySherds) {
        throw new AssertionError("Untransformed @Accessor");
    }
}
