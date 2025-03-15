package net.chikorita_lover.chicory.api.config;

import net.chikorita_lover.chicory.api.config.property.ConfigProperty;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@ApiStatus.Internal
public class ConfigValues {
    public static final PacketCodec<RegistryByteBuf, ConfigValues> PACKET_CODEC = PacketCodec.ofStatic(ConfigValues::write, ConfigValues::read);
    private final Config config;
    @SuppressWarnings("rawtypes")
    private final Map<ConfigProperty, ConfigProperty.Value> values = new HashMap<>();

    public ConfigValues(Config config) {
        this.config = config;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static ConfigValues read(RegistryByteBuf buf) {
        ConfigValues values = new ConfigValues(Config.getConfig(buf.readString()));
        int size = buf.readInt();
        for (int i = 0; i < size; ++i) {
            ConfigProperty property = values.config.getProperty(buf.readString());
            values.put(property.createValue(property.packetCodec().decode(buf)));
        }
        return values;
    }

    @SuppressWarnings({"unchecked"})
    private static void write(final RegistryByteBuf buf, final ConfigValues instance) {
        buf.writeString(instance.config.getName());
        buf.writeInt(instance.values.size());
        instance.values.values().forEach(value -> {
            buf.writeString(value.property().getName());
            value.property().packetCodec().encode(buf, value.value());
        });
    }

    public Config getConfig() {
        return this.config;
    }

    public boolean contains(final ConfigProperty<?> property) {
        return this.values.containsKey(property);
    }

    @SuppressWarnings("unchecked")
    public <T> ConfigProperty.Value<T> get(final ConfigProperty<T> property) {
        return (ConfigProperty.Value<T>) this.values.get(property);
    }

    public <T> void put(ConfigProperty.Value<T> value) {
        this.values.put(value.property(), value);
    }

    public void putAllIfPresent(ConfigValues other) {
        other.values.values().stream().filter(value -> this.config.contains(value.property())).forEach(this::put);
    }

    @SuppressWarnings("rawtypes")
    public void forEach(final Consumer<ConfigProperty.Value> action) {
        this.values.values().forEach(action);
    }
}
