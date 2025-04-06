package net.chikorita_lover.chicory.api.render;

import net.chikorita_lover.chicory.ChicoryApi;
import net.chikorita_lover.chicory.api.block.SkullTypeRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class SkullTypeRendererRegistry {
    private static final Map<SkullBlock.Type, Identifier> TEXTURES = new HashMap<>();
    private static final Map<SkullBlock.Type, ModelFactory> FACTORIES = new HashMap<>();
    private static final Map<SkullBlock.Type, EntityModelLayerRegistry.TexturedModelDataProvider> PROVIDERS = new HashMap<>();
    private static final Identifier DEFAULT_TEXTURE = Identifier.ofVanilla("textures/entity/skeleton/skeleton.png");
    private static final ModelFactory DEFAULT_FACTORY = SkullEntityModel::new;
    private static final EntityModelLayerRegistry.TexturedModelDataProvider DEFAULT_PROVIDER = SkullEntityModel::getSkullTexturedModelData;

    public static SkullBlock.Type register(SkullBlock.Type type, Identifier texture, ModelFactory factory, EntityModelLayerRegistry.TexturedModelDataProvider provider) {
        TEXTURES.put(type, texture.withPath(path -> "textures/" + path + ".png"));
        FACTORIES.put(type, factory);
        PROVIDERS.put(type, provider);
        return type;
    }

    @ApiStatus.Internal
    public static Set<SkullBlock.Type> getTypes() {
        return TEXTURES.keySet();
    }

    @ApiStatus.Internal
    public static Identifier getTexture(SkullBlock.Type type) {
        if (!FACTORIES.containsKey(type)) {
            ChicoryApi.LOGGER.warn("No registered render data for skull type: {}", SkullTypeRegistry.getId(type));
        }
        return TEXTURES.getOrDefault(type, DEFAULT_TEXTURE);
    }

    @ApiStatus.Internal
    public static SkullBlockEntityModel createModel(SkullBlock.Type type, ModelPart modelPart) {
        return FACTORIES.getOrDefault(type, DEFAULT_FACTORY).create(modelPart);
    }

    @ApiStatus.Internal
    public static EntityModelLayerRegistry.TexturedModelDataProvider getProvider(SkullBlock.Type type) {
        return PROVIDERS.getOrDefault(type, DEFAULT_PROVIDER);
    }

    @FunctionalInterface
    public interface ModelFactory {
        SkullBlockEntityModel create(ModelPart modelPart);
    }
}
