package net.chikorita_lover.chicory.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface ChicoryItem {
    static List<ItemStack> getGhostSlotStacksOf(Item item) {
        if (item instanceof ChicoryItem chicoryItem) {
            return chicoryItem.getGhostSlotStacks();
        } else {
            return List.of(new ItemStack(item));
        }
    }

    /**
     * Returns the item stacks to display when this item is present in a ghost slot for a recipe.
     * In vanilla, all items show their default stack when present in a ghost slot.
     * <p>If more than one stack is returned, the ghost slot will cycle through all returned stacks one at a time.
     *
     * @return the item stacks to display
     */
    default List<ItemStack> getGhostSlotStacks() {
        return List.of(new ItemStack((Item) this));
    }
}
