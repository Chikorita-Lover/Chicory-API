package net.chikorita_lover.chicory.api.entity;

import net.chikorita_lover.chicory.ChicoryApi;
import net.chikorita_lover.chicory.mixin.entity.BoatEntityTypeAccessor;
import net.minecraft.block.Block;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

/**
 * A registry for creating and modifying boat types.
 */
public final class BoatTypeRegistry {
    private static final Map<BoatEntity.Type, Item> BOAT_TYPES_TO_ITEMS = new HashMap<>();
    private static final Map<BoatEntity.Type, Item> CHEST_BOAT_TYPES_TO_ITEMS = new HashMap<>();
    private static final Map<Identifier, BoatEntity.Type> BOAT_TYPES_BY_ID = new HashMap<>();
    private static final Identifier DEFAULT_ID = Identifier.ofVanilla("oak");

    /**
     * Creates and returns a new boat type.
     * The base block is the block dropped when the boat is destroyed in a way that drops blocks and sticks (e.g. falling from a specific height) and is typically the block used to craft the boat item.
     *
     * @param id        the identifier used for internal API functions
     * @param baseBlock the base block
     * @return the new boat type
     * @implNote The textures for the boat type are located at {@code {namespace}:textures/entity/boat/{path}.png} and {@code {namespace}:textures/entity/chest_boat/{path}.png}.
     */
    @SuppressWarnings("UnreachableCode")
    public static BoatEntity.Type register(Identifier id, Block baseBlock) {
        List<BoatEntity.Type> values = new ArrayList<>(Arrays.asList(BoatEntityTypeAccessor.getValues()));
        int ordinal = values.size();
        String path = id.toString().replaceFirst("\\W", "_");
        BoatEntity.Type type = BoatEntityTypeAccessor.create(path.toUpperCase(), ordinal, baseBlock, path);
        values.add(type);
        BoatEntityTypeAccessor.setValues(values.toArray(BoatEntity.Type[]::new));
        BOAT_TYPES_BY_ID.put(id, type);
        return type;
    }

    /**
     * Assigns a boat item and chest boat item to the provided boat type.
     * The boat and chest boat items will be dropped when the boat is destroyed or given if the boat is picked.
     *
     * @param type      the boat type
     * @param boat      the boat item
     * @param chestBoat the chest boat item
     */
    public static void registerBoatItems(BoatEntity.Type type, Item boat, Item chestBoat) {
        BOAT_TYPES_TO_ITEMS.put(type, boat);
        CHEST_BOAT_TYPES_TO_ITEMS.put(type, chestBoat);
    }

    @ApiStatus.Internal
    public static Identifier getId(final BoatEntity.Type type) {
        if (!contains(type)) {
            ChicoryApi.LOGGER.warn("Attempted to get identifier of boat type {} not registered through Chicory API", type.name());
            return DEFAULT_ID;
        }
        return BOAT_TYPES_BY_ID.keySet().stream().filter(id -> BOAT_TYPES_BY_ID.get(id) == type).findFirst().orElse(DEFAULT_ID);
    }

    @ApiStatus.Internal
    public static Item getBoatItem(boolean chest, BoatEntity.Type type) {
        Map<BoatEntity.Type, Item> map = chest ? CHEST_BOAT_TYPES_TO_ITEMS : BOAT_TYPES_TO_ITEMS;
        return map.getOrDefault(type, chest ? Items.OAK_CHEST_BOAT : Items.OAK_BOAT);
    }

    @ApiStatus.Internal
    public static boolean contains(BoatEntity.Type type) {
        return BOAT_TYPES_BY_ID.containsValue(type);
    }
}
