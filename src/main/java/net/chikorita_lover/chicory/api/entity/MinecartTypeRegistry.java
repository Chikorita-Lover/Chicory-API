package net.chikorita_lover.chicory.api.entity;

import net.chikorita_lover.chicory.ChicoryApi;
import net.chikorita_lover.chicory.mixin.entity.AbstractMinecartEntityTypeAccessor;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

/**
 * A registry for creating minecart types.
 */
public final class MinecartTypeRegistry {
    private static final Map<AbstractMinecartEntity.Type, Item> MINECART_TYPES_TO_ITEMS = new HashMap<>();
    private static final Map<AbstractMinecartEntity.Type, MinecartFactory<?>> MINECART_FACTORY_MAP = new HashMap<>();

    /**
     * Creates and returns a new minecart type.
     *
     * @param name    the internal name
     * @param item    the associated item
     * @param factory a function instantiating a minecart entity
     * @return the new minecart type
     */
    @SuppressWarnings("UnreachableCode")
    public static AbstractMinecartEntity.Type register(String name, Item item, MinecartFactory<?> factory) {
        List<AbstractMinecartEntity.Type> values = new ArrayList<>(Arrays.asList(AbstractMinecartEntityTypeAccessor.getValues()));
        int ordinal = values.size();
        AbstractMinecartEntity.Type type = AbstractMinecartEntityTypeAccessor.create(name.toUpperCase(), ordinal);
        values.add(type);
        AbstractMinecartEntityTypeAccessor.setValues(values.toArray(AbstractMinecartEntity.Type[]::new));
        MINECART_TYPES_TO_ITEMS.put(type, item);
        MINECART_FACTORY_MAP.put(type, factory);
        return type;
    }

    @ApiStatus.Internal
    public static Item getItem(AbstractMinecartEntity.Type type) {
        if (!contains(type)) {
            ChicoryApi.LOGGER.warn("Attempted to get item of minecart type {} not registered through Chicory API", type);
        }
        return MINECART_TYPES_TO_ITEMS.getOrDefault(type, Items.MINECART);
    }

    @ApiStatus.Internal
    public static MinecartFactory<?> getFactory(AbstractMinecartEntity.Type type) {
        if (!contains(type)) {
            ChicoryApi.LOGGER.warn("Attempted to get factory of minecart type {} not registered through Chicory API", type);
        }
        return MINECART_FACTORY_MAP.getOrDefault(type, MinecartEntity::new);
    }

    @ApiStatus.Internal
    public static boolean contains(AbstractMinecartEntity.Type type) {
        return MINECART_TYPES_TO_ITEMS.containsKey(type);
    }

    @FunctionalInterface
    public interface MinecartFactory<T extends AbstractMinecartEntity> {
        T create(ServerWorld world, double x, double y, double z);
    }
}
