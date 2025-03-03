package net.chikorita_lover.chicory.mixin.client.gui;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.chikorita_lover.chicory.api.resource.ToggleableFeatureRegistry;
import net.chikorita_lover.chicory.item.ChicoryItem;
import net.minecraft.client.gui.screen.recipebook.RecipeBookGhostSlots;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(RecipeBookGhostSlots.GhostInputSlot.class)
public class GhostInputSlotMixin {
    @Shadow
    @Final
    RecipeBookGhostSlots field_3085;
    @Unique
    @Nullable
    private ItemStack[] displayStacks;

    @ModifyExpressionValue(method = "getCurrentItemStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Ingredient;getMatchingStacks()[Lnet/minecraft/item/ItemStack;"))
    private ItemStack[] modifyIngredientStacks(ItemStack[] matchingStacks) {
        if (this.displayStacks == null) {
            List<ItemStack> stacks = new ArrayList<>();
            // noinspection EqualsBetweenInconvertibleTypes
            if (this.field_3085.getSlot(0).equals(this)) {
                stacks.addAll(List.of(matchingStacks));
            } else {
                for (ItemStack stack : matchingStacks) {
                    if (ToggleableFeatureRegistry.isEnabled(stack.getItem())) {
                        stacks.addAll(ChicoryItem.getGhostSlotStacksOf(stack.getItem()));
                    }
                }
            }
            this.displayStacks = stacks.toArray(ItemStack[]::new);
        }
        return this.displayStacks;
    }
}
