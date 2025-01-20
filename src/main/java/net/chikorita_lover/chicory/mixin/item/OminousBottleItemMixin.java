package net.chikorita_lover.chicory.mixin.item;

import net.chikorita_lover.chicory.item.ChicoryItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.OminousBottleItem;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(OminousBottleItem.class)
public abstract class OminousBottleItemMixin extends Item implements ChicoryItem {
    public OminousBottleItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public List<ItemStack> getGhostSlotStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            ItemStack stack = new ItemStack(this);
            stack.set(DataComponentTypes.OMINOUS_BOTTLE_AMPLIFIER, i);
            stacks.add(stack);
        }
        return stacks;
    }
}
