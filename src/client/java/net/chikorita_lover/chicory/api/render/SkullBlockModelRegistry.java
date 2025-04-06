package net.chikorita_lover.chicory.api.render;

import net.chikorita_lover.chicory.api.block.SkullTypeRegistry;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A registry for setting the texture and model of skull types registered through Chicory API.
 *
 * @see SkullTypeRegistry
 */
public final class SkullBlockModelRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(SkullBlockModelRegistry.class);
    private static final Map<SkullBlock.Type, Identifier> TEXTURES = new HashMap<>();
    private static final Map<SkullBlock.Type, ModelFactory> FACTORIES = new HashMap<>();
    private static final Identifier DEFAULT_TEXTURE = Identifier.ofVanilla("textures/entity/skeleton/skeleton.png");
    private static final ModelFactory DEFAULT_FACTORY = modelLoader -> new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL));

    /**
     * Sets the texture and model factory for {@code type}.
     *
     * @param type    the skull type
     * @param texture a texture resource location
     * @param factory a function that creates a model
     * @implNote Only skull types created through Chicory API may be passed as {@code type}.
     */
    public static void register(SkullBlock.Type type, Identifier texture, ModelFactory factory) {
        if (!SkullTypeRegistry.contains(type)) {
            LOGGER.warn("Attempted to register texture and model for invalid skull type: {}", type);
            return;
        }
        TEXTURES.put(type, texture.withPath(path -> "textures/" + path + ".png"));
        FACTORIES.put(type, factory);
    }

    @ApiStatus.Internal
    public static Set<SkullBlock.Type> getTypes() {
        return TEXTURES.keySet();
    }

    @ApiStatus.Internal
    public static Identifier getTexture(SkullBlock.Type type) {
        if (!FACTORIES.containsKey(type)) {
            LOGGER.warn("No registered texture or model for skull type: {}", SkullTypeRegistry.getId(type));
        }
        return TEXTURES.getOrDefault(type, DEFAULT_TEXTURE);
    }

    @ApiStatus.Internal
    public static SkullBlockEntityModel createModel(SkullBlock.Type type, EntityModelLoader modelLoader) {
        return FACTORIES.getOrDefault(type, DEFAULT_FACTORY).create(modelLoader);
    }

    @FunctionalInterface
    public interface ModelFactory {
        SkullBlockEntityModel create(EntityModelLoader modelLoader);
    }
}
