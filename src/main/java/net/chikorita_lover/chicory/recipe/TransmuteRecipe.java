package net.chikorita_lover.chicory.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class TransmuteRecipe implements CraftingRecipe {
    final String group;
    final CraftingRecipeCategory category;
    final ItemStack result;
    final Ingredient input;
    final DefaultedList<Ingredient> materials;

    public TransmuteRecipe(String group, CraftingRecipeCategory category, ItemStack result, Ingredient input, DefaultedList<Ingredient> materials) {
        this.group = group;
        this.category = category;
        this.result = result;
        this.input = input;
        this.materials = materials;
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return this.category;
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        if (input.getStackCount() != this.getIngredients().size()) {
            return false;
        }
        return input.getRecipeMatcher().match(this, null);
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack stack = ItemStack.EMPTY;
        for (ItemStack slotStack : input.getStacks()) {
            if (slotStack.isEmpty() || !this.input.test(slotStack)) {
                continue;
            }
            stack = slotStack;
        }
        ItemStack result = this.result.copy();
        result.applyChanges(stack.getComponentChanges());
        return result;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= this.getIngredients().size();
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return this.result;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> ingredients = DefaultedList.of();
        ingredients.add(this.input);
        ingredients.addAll(this.materials);
        return ingredients;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ChicoryRecipeSerializers.TRANSMUTE;
    }

    public static class Serializer implements RecipeSerializer<TransmuteRecipe> {
        public static final PacketCodec<RegistryByteBuf, TransmuteRecipe> PACKET_CODEC = PacketCodec.ofStatic(Serializer::write, Serializer::read);
        private static final MapCodec<TransmuteRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group), CraftingRecipeCategory.CODEC.fieldOf("category").orElse(CraftingRecipeCategory.MISC).forGetter(recipe -> recipe.category), ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(recipe -> recipe.result), Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("input").forGetter(recipe -> recipe.input), Ingredient.DISALLOW_EMPTY_CODEC.listOf().fieldOf("materials").flatXmap(materials -> {
            Ingredient[] ingredients = materials.stream().filter(ingredient -> !ingredient.isEmpty()).toArray(Ingredient[]::new);
            if (ingredients.length == 0) {
                return DataResult.error(() -> "No ingredients for transmute recipe");
            }
            if (ingredients.length > 8) {
                return DataResult.error(() -> "Too many ingredients for transmute recipe");
            }
            return DataResult.success(DefaultedList.copyOf(Ingredient.EMPTY, ingredients));
        }, DataResult::success).forGetter(recipe -> recipe.materials)).apply(instance, TransmuteRecipe::new));

        private static TransmuteRecipe read(final RegistryByteBuf buf) {
            String group = buf.readString();
            CraftingRecipeCategory category = buf.readEnumConstant(CraftingRecipeCategory.class);
            ItemStack result = ItemStack.PACKET_CODEC.decode(buf);
            Ingredient input = Ingredient.PACKET_CODEC.decode(buf);
            int size = buf.readVarInt();
            DefaultedList<Ingredient> materials = DefaultedList.ofSize(size, Ingredient.EMPTY);
            materials.replaceAll(empty -> Ingredient.PACKET_CODEC.decode(buf));
            return new TransmuteRecipe(group, category, result, input, materials);
        }

        private static void write(RegistryByteBuf buf, TransmuteRecipe recipe) {
            buf.writeString(recipe.group);
            buf.writeEnumConstant(recipe.category);
            ItemStack.PACKET_CODEC.encode(buf, recipe.result);
            Ingredient.PACKET_CODEC.encode(buf, recipe.input);
            buf.writeVarInt(recipe.materials.size());
            for (Ingredient ingredient : recipe.materials) {
                Ingredient.PACKET_CODEC.encode(buf, ingredient);
            }
        }

        @Override
        public MapCodec<TransmuteRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, TransmuteRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
