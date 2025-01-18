package net.chikorita_lover.chicory.mixin.client.recipe;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.chikorita_lover.chicory.api.recipe.RecipeBookGroupRegistry;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.book.RecipeBookCategory;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collections;
import java.util.List;

@Mixin(RecipeBookGroup.class)
public class RecipeBookGroupMixin {
    @WrapMethod(method = "getGroups")
    private static List<RecipeBookGroup> modifyGroups(RecipeBookCategory category, Operation<List<RecipeBookGroup>> original) {
        if (RecipeBookGroupRegistry.contains(category)) {
            return RecipeBookGroupRegistry.getGroups(category);
        }
        try {
            return original.call(category);
        } catch (MatchException exception) {
            return Collections.emptyList();
        }
    }
}
