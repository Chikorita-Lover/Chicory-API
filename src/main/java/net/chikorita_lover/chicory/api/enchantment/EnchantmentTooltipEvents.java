package net.chikorita_lover.chicory.api.enchantment;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Holds an event used to modify the tooltip order of enchantments.
 */
public class EnchantmentTooltipEvents {
    /**
     * This event allows the entries of the tooltip order for enchantments to be modified.
     */
    public static final Event<ModifyEntries> MODIFY_ENTRIES = EventFactory.createArrayBacked(ModifyEntries.class, callbacks -> entries -> {
        for (ModifyEntries callback : callbacks) {
            callback.modifyEntries(entries);
        }
    });

    @FunctionalInterface
    public interface ModifyEntries {
        /**
         * Modifies the enchantment tooltip order.
         *
         * @param entries the entries
         * @see EnchantmentTooltipEntries
         */
        void modifyEntries(EnchantmentTooltipEntries entries);
    }
}
