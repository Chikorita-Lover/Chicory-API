package net.chikorita_lover.chicory.mixin.item;

import net.chikorita_lover.chicory.item.ChicoryItem;
import net.minecraft.item.GoatHornItem;
import net.minecraft.item.Instrument;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(GoatHornItem.class)
public abstract class GoatHornItemMixin extends Item implements ChicoryItem {
    @Shadow
    @Final
    private TagKey<Instrument> instrumentTag;

    public GoatHornItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public List<ItemStack> getGhostSlotStacks() {
        return Registries.INSTRUMENT.getOrCreateEntryList(this.instrumentTag).stream().map(instrument -> GoatHornItem.getStackForInstrument(this, instrument)).toList();
    }
}
