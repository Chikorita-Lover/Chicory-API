package net.chikorita_lover.chicory.mixin.client.gui;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.chikorita_lover.chicory.ChicoryApi;
import net.chikorita_lover.chicory.MathUtil;
import net.chikorita_lover.chicory.item.ChicoryItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(RecipeBookWidget.class)
public class RecipeBookWidgetMixin {
    @Shadow
    protected AbstractRecipeScreenHandler<?, ?> craftingScreenHandler;

    @Shadow
    protected MinecraftClient client;

    @ModifyExpressionValue(method = "showGhostRecipe", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Ingredient;ofStacks([Lnet/minecraft/item/ItemStack;)Lnet/minecraft/recipe/Ingredient;"))
    private Ingredient getAllResults(Ingredient original, RecipeEntry<?> recipe) {
        if (!(recipe.value() instanceof CraftingRecipe craftingRecipe)) {
            return original;
        }
        List<List<ItemStack>> ingredients = craftingRecipe.getIngredients().stream().map(ingredient -> {
            if (ingredient.isEmpty()) {
                return List.of(ItemStack.EMPTY);
            }
            List<ItemStack> stacks = new ArrayList<>();
            ingredient.getMatchingItemIds().stream().map(Item::byRawId).map(ChicoryItem::getGhostSlotStacksOf).forEach(stacks::addAll);
            return stacks;
        }).toList();
        int lcm = MathUtil.lcm(ingredients.stream().map(List::size).toArray(Integer[]::new));
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < lcm; ++i) {
            final int index = i;
            List<ItemStack> inputs = ingredients.stream().map(_stacks -> _stacks.get(index % _stacks.size())).toList();
            int width = Math.min(inputs.size(), this.craftingScreenHandler.getCraftingWidth());
            CraftingRecipeInput input = CraftingRecipeInput.create(width, inputs.size() / width, inputs);
            stacks.add(craftingRecipe.craft(input, this.client.world.getRegistryManager()));
        }
        return Ingredient.ofStacks(stacks.stream());
    }
}
