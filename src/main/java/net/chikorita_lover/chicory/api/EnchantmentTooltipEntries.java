package net.chikorita_lover.chicory.api;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * This class allows the tooltip order of enchantments to be modified by the events in {@link EnchantmentTooltipEvents}.
 */
public record EnchantmentTooltipEntries(RegistryWrapper.WrapperLookup registryLookup,
                                        List<RegistryEntry<Enchantment>> enchantments) {
    /**
     * See {@link #add(Collection)}.
     */
    @SafeVarargs
    public final void add(RegistryKey<Enchantment>... enchantments) {
        this.add(this.asRegistryEntryList(enchantments));
    }

    /**
     * Adds enchantments to the end of the tooltip order.
     *
     * @param enchantments the enchantments to be added
     */
    public void add(Collection<RegistryEntry<Enchantment>> enchantments) {
        this.enchantments.removeAll(enchantments);
        this.enchantments.addAll(enchantments);
    }

    /**
     * See {@link #prepend(Collection)}.
     */
    @SafeVarargs
    public final void prepend(RegistryKey<Enchantment>... enchantments) {
        this.prepend(this.asRegistryEntryList(enchantments));
    }

    /**
     * Adds enchantments to the beginning of the tooltip order.
     *
     * @param enchantments the enchantments to be added
     */
    public void prepend(Collection<RegistryEntry<Enchantment>> enchantments) {
        this.enchantments.removeAll(enchantments);
        this.enchantments.addAll(0, enchantments);
    }

    /**
     * See {@link #addAfter(RegistryEntry, Collection)}.
     */
    @SafeVarargs
    public final void addAfter(RegistryKey<Enchantment> afterLast, RegistryKey<Enchantment>... enchantments) {
        this.addAfter(this.getEntry(afterLast), this.asRegistryEntryList(enchantments));
    }

    /**
     * Adds enchantments to the tooltip order after an existing enchantment or at the end if the enchantment is not in the tooltip order.
     *
     * @param afterLast    the enchantment to add more enchantments after
     * @param enchantments the enchantments to be added
     */
    public void addAfter(RegistryEntry<Enchantment> afterLast, Collection<RegistryEntry<Enchantment>> enchantments) {
        this.enchantments.removeAll(enchantments);
        for (int i = this.enchantments.size() - 1; i >= 0; i--) {
            if (this.enchantments.get(i).matches(afterLast)) {
                this.enchantments.subList(i + 1, i + 1).addAll(enchantments);
                return;
            }
        }
        this.enchantments.addAll(enchantments);
    }

    /**
     * See {@link #addBefore(RegistryEntry, Collection)}.
     */
    @SafeVarargs
    public final void addBefore(RegistryKey<Enchantment> beforeFirst, RegistryKey<Enchantment>... enchantments) {
        this.addBefore(this.getEntry(beforeFirst), this.asRegistryEntryList(enchantments));
    }

    /**
     * Adds enchantments to the tooltip order before an existing enchantment or at the end if the enchantment is not in the tooltip order.
     *
     * @param beforeFirst  the enchantment to add more enchantments before
     * @param enchantments the enchantments to be added
     */
    public void addBefore(RegistryEntry<Enchantment> beforeFirst, Collection<RegistryEntry<Enchantment>> enchantments) {
        this.enchantments.removeAll(enchantments);
        for (int i = 0; i < this.enchantments.size(); i++) {
            if (this.enchantments.get(i).matches(beforeFirst)) {
                this.enchantments.subList(i, i).addAll(enchantments);
                return;
            }
        }
        this.enchantments.addAll(enchantments);
    }

    private RegistryEntry<Enchantment> getEntry(RegistryKey<Enchantment> enchantment) {
        return this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(enchantment);
    }

    private List<RegistryEntry<Enchantment>> asRegistryEntryList(RegistryKey<Enchantment>[] enchantments) {
        return Arrays.stream(enchantments).map(this::getEntry).toList();
    }
}
