package net.chikorita_lover.chicory.mixin.client.recipe;

import net.chikorita_lover.chicory.api.recipe.RecipeBookTypeRegistry;
import net.chikorita_lover.chicory.api.recipe.RecipeScreenHelper;
import net.minecraft.client.recipebook.RecipeBookType;
import net.minecraft.recipe.book.RecipeBookCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(RecipeBookType.class)
public class ClientRecipeBookTypeMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void setClientRegistryConsumer(CallbackInfo ci) {
        RecipeBookTypeRegistry.setClientRegistryConsumer(ClientRecipeBookTypeMixin::register);
    }

    @Unique
    @SuppressWarnings("UnreachableCode")
    private static void register(net.minecraft.recipe.book.RecipeBookType type, RecipeBookCategory[] categories) {
        List<RecipeBookType> values = new ArrayList<>(Arrays.asList(ClientRecipeBookTypeAccessor.getValues()));
        int ordinal = values.size();
        RecipeBookType clientType = ClientRecipeBookTypeAccessor.create(type.name(), ordinal);
        values.add(clientType);
        ClientRecipeBookTypeAccessor.setValues(values.toArray(RecipeBookType[]::new));
        RecipeScreenHelper.RECIPE_TYPE_TO_GROUP.put(type, clientType);
    }
}
