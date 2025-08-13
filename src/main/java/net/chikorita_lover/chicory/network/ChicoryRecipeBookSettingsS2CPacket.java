package net.chikorita_lover.chicory.network;

import net.chikorita_lover.chicory.ChicoryApi;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.recipe.book.RecipeBookOptions;
import net.minecraft.recipe.book.RecipeBookType;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

@ApiStatus.Internal
public record ChicoryRecipeBookSettingsS2CPacket(Map<RecipeBookType, RecipeBookOptions.CategoryOption> options) implements CustomPayload {
    public static final Id<ChicoryRecipeBookSettingsS2CPacket> PACKET_ID = new Id<>(ChicoryApi.of("chicory_recipe_book_settings"));
    public static final PacketCodec<RegistryByteBuf, ChicoryRecipeBookSettingsS2CPacket> PACKET_CODEC = PacketCodec.of(ChicoryRecipeBookSettingsS2CPacket::write, ChicoryRecipeBookSettingsS2CPacket::new);

    public ChicoryRecipeBookSettingsS2CPacket(RegistryByteBuf buf) {
        this(readOptions(buf));
    }

    private static Map<RecipeBookType, RecipeBookOptions.CategoryOption> readOptions(RegistryByteBuf buf) {
        Map<RecipeBookType, RecipeBookOptions.CategoryOption> options = new HashMap<>();
        int size = buf.readInt();
        for (int i = 0; i < size; ++i) {
            RecipeBookType category = buf.readEnumConstant(RecipeBookType.class);
            RecipeBookOptions.CategoryOption option = new RecipeBookOptions.CategoryOption(buf.readBoolean(), buf.readBoolean());
            options.put(category, option);
        }
        return options;
    }

    public void write(final RegistryByteBuf buf) {
        buf.writeInt(this.options.size());
        this.options.forEach((type, option) -> {
            buf.writeEnumConstant(type);
            buf.writeBoolean(option.guiOpen());
            buf.writeBoolean(option.filteringCraftable());
        });
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
