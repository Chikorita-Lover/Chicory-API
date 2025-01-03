package net.chikorita_lover.chicory.api.entity;

import net.chikorita_lover.chicory.ChicoryApi;
import net.chikorita_lover.chicory.mixin.entity.BoatEntityTypeAccessor;
import net.minecraft.block.Block;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.*;

public final class BoatTypeRegistry {
    private static final Map<BoatEntity.Type, Item> BOAT_TYPES_TO_ITEMS = new HashMap<>();
    private static final Map<BoatEntity.Type, Item> CHEST_BOAT_TYPES_TO_ITEMS = new HashMap<>();
    private static final Map<Identifier, BoatEntity.Type> BOAT_TYPES_BY_ID = new HashMap<>();
    private static final Identifier DEFAULT_ID = Identifier.ofVanilla("oak");

    public static Identifier getId(final BoatEntity.Type type) {
        if (!contains(type)) {
            ChicoryApi.LOGGER.warn("Attempted to get the identifier of a boat type not registered through Chicory API");
            return DEFAULT_ID;
        }
        return BOAT_TYPES_BY_ID.keySet().stream().filter(id -> BOAT_TYPES_BY_ID.get(id) == type).findFirst().orElse(DEFAULT_ID);
    }

    public static Item getBoatItem(boolean chest, BoatEntity.Type type) {
        Map<BoatEntity.Type, Item> map = chest ? CHEST_BOAT_TYPES_TO_ITEMS : BOAT_TYPES_TO_ITEMS;
        return map.getOrDefault(type, chest ? Items.OAK_CHEST_BOAT : Items.OAK_BOAT);
    }

    public static boolean contains(BoatEntity.Type type) {
        return BOAT_TYPES_BY_ID.containsValue(type);
    }

    public static BoatEntity.Type create(Identifier id, Block baseBlock) {
        List<BoatEntity.Type> values = new ArrayList<>(Arrays.asList(BoatEntityTypeAccessor.getValues()));
        int ordinal = values.getLast().ordinal() + 1;
        String path = id.toString().replaceFirst("\\W", "_");
        BoatEntity.Type type = BoatEntityTypeAccessor.create(path.toUpperCase(), ordinal, baseBlock, path);
        values.add(type);
        BoatEntityTypeAccessor.setValues(values.toArray(BoatEntity.Type[]::new));
        BOAT_TYPES_BY_ID.put(id, type);
        return type;
    }

    public static void registerBoatItems(BoatEntity.Type type, Item boat, Item chestBoat) {
        BOAT_TYPES_TO_ITEMS.put(type, boat);
        CHEST_BOAT_TYPES_TO_ITEMS.put(type, chestBoat);
    }
}
