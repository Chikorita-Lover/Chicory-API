package net.chikorita_lover.chicory.mixin.item;

import net.chikorita_lover.chicory.item.ChicoryItem;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(PotionItem.class)
public class PotionItemMixin extends Item implements ChicoryItem {
    public PotionItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public List<ItemStack> getGhostSlotStacks() {
        return Registries.POTION.streamEntries().map(potion -> PotionContentsComponent.createStack(this, potion)).toList();
    }
}
