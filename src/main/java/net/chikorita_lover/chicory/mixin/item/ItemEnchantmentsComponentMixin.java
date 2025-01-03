package net.chikorita_lover.chicory.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.chikorita_lover.chicory.api.enchantment.EnchantmentTooltipEntries;
import net.chikorita_lover.chicory.api.enchantment.EnchantmentTooltipEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;
import java.util.List;

@Mixin(ItemEnchantmentsComponent.class)
public class ItemEnchantmentsComponentMixin {
    @Unique
    private static RegistryEntryList<Enchantment> computedTooltipOrder;

    @ModifyReturnValue(method = "getTooltipOrderList", at = @At("RETURN"))
    private static <T> RegistryEntryList<T> modifyTooltipOrderList(RegistryEntryList<T> list, RegistryWrapper.WrapperLookup registryLookup, RegistryKey<Registry<T>> registryRef) {
        if (!registryRef.equals(RegistryKeys.ENCHANTMENT)) {
            return list;
        }
        if (computedTooltipOrder == null) {
            EnchantmentTooltipEntries entries = new EnchantmentTooltipEntries(registryLookup, castEntries(list));
            EnchantmentTooltipEvents.MODIFY_ENTRIES.invoker().modifyEntries(entries);
            computedTooltipOrder = RegistryEntryList.of(entries.enchantments());
        }
        return (RegistryEntryList<T>) computedTooltipOrder;
    }

    @Unique
    private static List<RegistryEntry<Enchantment>> castEntries(RegistryEntryList<?> list) {
        final List<RegistryEntry<Enchantment>> entries = new LinkedList<>();
        list.forEach(entry -> entries.add((RegistryEntry<Enchantment>) entry));
        return entries;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerClearEvent(CallbackInfo ci) {
        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> computedTooltipOrder = null);
    }
}
