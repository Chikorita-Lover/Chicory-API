package net.chikorita_lover.chicory.api.block;

import net.chikorita_lover.chicory.mixin.block.SkullBlockTypeAccessor;
import net.minecraft.block.SkullBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * A registry for creating and modifying skull types.
 */
public final class SkullTypeRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(SkullTypeRegistry.class);
    private static final Map<Identifier, SkullBlock.Type> TYPES = new HashMap<>();
    private static final Map<SkullBlock.Type, Item> ITEMS = new HashMap<>();
    private static final Map<EntityType<?>, SkullBlock.Type> ENTITY_TO_SKULL = new HashMap<>();
    private static final Identifier DEFAULT_ID = Identifier.ofVanilla("skeleton");

    /**
     * Creates and returns a new skull type.
     * <p>To register the item associated with the skull type, call {@link #registerItem(SkullBlock.Type, Item)}.
     *
     * @param id an identifier representing the skull type
     * @return the new skull type
     * @see #registerItem(SkullBlock.Type, Item)
     * @see #registerEntities(SkullBlock.Type, EntityType[])
     */
    @SuppressWarnings("UnreachableCode")
    public static SkullBlock.Type register(Identifier id) {
        try {
            List<SkullBlock.Type> values = new ArrayList<>(Arrays.asList(SkullBlockTypeAccessor.getValues()));
            int ordinal = values.size();
            String name = id.toString().replaceFirst("\\W", "_");
            final SkullBlock.Type type = SkullBlockTypeAccessor.create(name.toUpperCase(), ordinal, name);
            values.add(type);
            SkullBlockTypeAccessor.setValues(values.toArray(SkullBlock.Type[]::new));
            TYPES.put(id, type);
            return type;
        } catch (Exception e) {
            LOGGER.error("Could not register skull type {}: {}", id, e.getMessage());
            return SkullBlock.Type.SKELETON;
        }
    }

    public static void registerItem(SkullBlock.Type type, Item item) {
        if (!ITEMS.containsKey(type)) {
            ITEMS.put(type, item);
        }
    }

    public static void registerEntities(SkullBlock.Type type, EntityType<?>... entities) {
        for (EntityType<?> entity : entities) {
            ENTITY_TO_SKULL.put(entity, type);
        }
    }

    @ApiStatus.Internal
    public static Identifier getId(final SkullBlock.Type type) {
        return TYPES.keySet().stream().filter(id -> TYPES.get(id) == type).findFirst().orElse(DEFAULT_ID);
    }

    @ApiStatus.Internal
    public static Item getSkull(EntityType<?> entity) {
        return ITEMS.getOrDefault(ENTITY_TO_SKULL.get(entity), Items.AIR);
    }

    @ApiStatus.Internal
    public static boolean contains(SkullBlock.Type type) {
        return TYPES.containsValue(type);
    }

    @ApiStatus.Internal
    public static boolean hasSkull(EntityType<?> entity) {
        return ENTITY_TO_SKULL.containsKey(entity) && ITEMS.containsKey(ENTITY_TO_SKULL.get(entity));
    }
}
